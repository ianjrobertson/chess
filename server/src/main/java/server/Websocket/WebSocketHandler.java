package server.Websocket;

import chess.ChessGame;
import chess.MovesCalculator.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.*;
import service.*;
import service.ServiceRecords.ListGamesRequest;
import service.ServiceRecords.ListGamesResponse;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;
import org.eclipse.jetty.websocket.api.Session;

import java.util.ArrayList;

@WebSocket
public class WebSocketHandler {
    private final WebSocketSessions sessions = new WebSocketSessions();
    private final ListGamesService listGamesService = new ListGamesService();
    private final JoinService joinService = new JoinService();
    private final MakeMoveService makeMoveService = new MakeMoveService();
    private final LeaveGameService leaveGameService = new LeaveGameService();
    private final ResignService resignService = new ResignService();

    public WebSocketHandler() {

    }

    /**
     * @OnWebSocketConnect
     *     public void onConnect(Session session) {
     *
     *     }
     *     @OnWebSocketClose
     *     public void onClose(Session session) {
     *
     *     }
     *     @OnWebSocketError
     *     public void onError(Throwable exception) {
     *
     *     }
     *
     */


    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        System.out.println("Received message");
        System.out.println(message);
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case UserGameCommand.CommandType.JOIN_PLAYER -> this.joinPlayer(new Gson().fromJson(message, JoinPlayerMessage.class), session);
            case UserGameCommand.CommandType.JOIN_OBSERVER -> this.joinObserver(new Gson().fromJson(message, JoinObserverMessage.class), session);
            case UserGameCommand.CommandType.MAKE_MOVE -> this.makeMove(new Gson().fromJson(message, MakeMoveMessage.class));
            case UserGameCommand.CommandType.LEAVE -> this.leaveGame(new Gson().fromJson(message, LeaveMessage.class), session);
            case UserGameCommand.CommandType.RESIGN -> this.resignGame(new Gson().fromJson(message, ResignMessage.class), session);
        }
    }

    public void sendMessage(Integer gameID, ServerMessage message, String authToken) throws Exception {
        System.out.println("Sending Message from server");
        var gameSession = sessions.getSessionsForGame(gameID);
        //System.out.println(gameSession.size());
        var removeList = new ArrayList<Session>();
        for (var s: gameSession.keySet()) {
            if (gameSession.get(s).isOpen()) {
                if (s.equals(authToken)) {
                    System.out.println("Sending message to " + authToken);
                    gameSession.get(s).getRemote().sendString(new Gson().toJson(message));
                }
            }
            else {
                removeList.add(gameSession.get(s));
            }
        }
        for (var s: removeList) {
            sessions.removeSession(s);
        }
    }

    private GameData getGame(Integer gameID, String authToken) throws Exception{
        try {
            ListGamesResponse res = listGamesService.listGames(new ListGamesRequest(authToken));
            for (GameData game: res.games()) {
                if (game.gameID() == gameID) {
                    return game;
                }
            }
            throw new Exception("Error: Invalid GameID");
        }
        catch (DataAccessException d) {
            System.out.println(d.getMessage());
            //onError(d);
        }
        return null;
    }

    public void broadcastMessage(Integer gameID, ServerMessage message, String exceptThisAuthToken) throws Exception {
        var gameSession = sessions.getSessionsForGame(gameID);
        var removeList = new ArrayList<Session>(); //Use eclipse websocket session
        for (var s : gameSession.keySet()) {
            if (gameSession.get(s).isOpen()) {
                if (!s.equals(exceptThisAuthToken)) {
                    gameSession.get(s).getRemote().sendString(new Gson().toJson(message));
                }
            }
            else {
                removeList.add(gameSession.get(s));
            }
        }
        for (var s: removeList) {
            sessions.removeSession(s);
        }
    }

    public void joinPlayer(JoinPlayerMessage joinPlayerMessage, Session session) throws Exception {
        try {
            GameData game = this.getGame(joinPlayerMessage.getGameID(), joinPlayerMessage.getAuthString());

            AuthData auth = joinService.join(joinPlayerMessage.getAuthString(), joinPlayerMessage.getGameID());

            if (joinPlayerMessage.getColor() == ChessGame.TeamColor.WHITE) {
                if (game.whiteUsername() != null && !game.whiteUsername().equals(auth.username())) {
                    throw new Exception("Error: Color already taken");
                }
            }
            else if(joinPlayerMessage.getColor() == ChessGame.TeamColor.BLACK) {
                if(!game.blackUsername().equals(auth.username())) {
                    throw new Exception("Error: Color already taken");
                }
            }

            sessions.addSession(joinPlayerMessage.getGameID(), joinPlayerMessage.getAuthString(), session);
            //1. Server sends a loadGame message back to the root client


            LoadGameMessage loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game.game());
            this.sendMessage(joinPlayerMessage.getGameID(), loadGameMessage, joinPlayerMessage.getAuthString() ); //review everything we have done

            //2. Server sends a notification message to all users in the session.
            var message = String.format("%s joined the game as %s", auth.username(), joinPlayerMessage.getColor());
            var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            this.broadcastMessage(joinPlayerMessage.getGameID(), notification, joinPlayerMessage.getAuthString());
        }
        catch (DataAccessException d) {
            Integer gameID = sessions.getGameID(joinPlayerMessage.getAuthString());
            this.sendMessage(gameID, new ErrorMessage(ServerMessage.ServerMessageType.ERROR, d.getMessage()), joinPlayerMessage.getAuthString());
        }
        catch (Exception e) {
            this.sendMessage(joinPlayerMessage.getGameID(), new ErrorMessage(ServerMessage.ServerMessageType.ERROR, e.getMessage()), joinPlayerMessage.getAuthString());
        }
    }

    public void joinObserver(JoinObserverMessage joinObserverMessage, Session session) throws Exception {
        try {
            sessions.addSession(joinObserverMessage.getGameID(), joinObserverMessage.getAuthString(), session);
            // Server sends a LOAD_GAME message back to the root client.
            GameData game = this.getGame(joinObserverMessage.getGameID(), joinObserverMessage.getAuthString());

            LoadGameMessage loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game.game());
            this.sendMessage(joinObserverMessage.getGameID(), loadGameMessage, joinObserverMessage.getAuthString());

            // Server sends a Notification message to all other clients in that game informing them the root client joined as an observer.
            String username = joinService.join(joinObserverMessage.getAuthString(), joinObserverMessage.getGameID()).username(); // the problem we have is the join Observer won't have a team color...
            var message = String.format("%s joined the game as an observer", username);
            var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            this.broadcastMessage(joinObserverMessage.getGameID(), notification, joinObserverMessage.getAuthString());

        }
        catch(Exception e) {
            this.sendMessage(joinObserverMessage.getGameID(), new ErrorMessage(ServerMessage.ServerMessageType.ERROR, e.getMessage()), joinObserverMessage.getAuthString());
            //onError(e);
        }
    }

    public void makeMove(MakeMoveMessage makeMoveMessage) throws Exception {
        try {
            // We need to validate that it is the current players turn
            String username;
            GameData game = makeMoveService.makeMove(makeMoveMessage.getGameID(), makeMoveMessage.getAuthString(), makeMoveMessage.getMove()); // -> makeMoveService validates move through makeMove in ChessGame class. Throws invalidMoveException
            if (game.game().isGameOver())
                throw new Exception("Error: game is over"); // if game is over -- don't make a move.
            if (game.game().getTeamTurn() == ChessGame.TeamColor.WHITE) {
                username = game.blackUsername();
            }
            else {
                username = game.whiteUsername();
            }
            //Server sends a LOAD_GAME message to all clients in the game (including the root client) with an updated game.
            LoadGameMessage loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game.game());
            this.sendMessage(makeMoveMessage.getGameID(), loadGameMessage, makeMoveMessage.getAuthString());
            this.broadcastMessage(makeMoveMessage.getGameID(), loadGameMessage, makeMoveMessage.getAuthString());

            //Server sends a Notification message to all other clients in that game informing them what move was made.
            var message = String.format("%s made a move : %s", username, makeMoveMessage.getMove().toString());
            var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            this.broadcastMessage(makeMoveMessage.getGameID(), notification, makeMoveMessage.getAuthString());

            String checkMessage;
            if (game.game().isInCheck(ChessGame.TeamColor.WHITE)) {
                if (game.game().isInCheckmate(ChessGame.TeamColor.WHITE)) {
                    checkMessage = String.format("%s is in checkmate", game.whiteUsername());
                }
                else {
                    checkMessage = String.format("%s is in check", game.whiteUsername());
                }
                var checkNotification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, checkMessage);
                this.sendMessage(makeMoveMessage.getGameID(), checkNotification, makeMoveMessage.getAuthString());
                this.broadcastMessage(makeMoveMessage.getGameID(), checkNotification, makeMoveMessage.getAuthString());
            }
            else if (game.game().isInCheck(ChessGame.TeamColor.BLACK)) {
                if (game.game().isInCheckmate(ChessGame.TeamColor.BLACK)) {
                    checkMessage = String.format("%s in in checkmate", game.blackUsername());
                }
                else {
                    checkMessage = String.format("%s is in check", game.blackUsername());
                }
                var checkNotification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, checkMessage);
                this.sendMessage(makeMoveMessage.getGameID(), checkNotification, makeMoveMessage.getAuthString());
                this.broadcastMessage(makeMoveMessage.getGameID(), checkNotification, makeMoveMessage.getAuthString());
            }
        }
        catch (InvalidMoveException invalidMoveException) {
            this.sendMessage(makeMoveMessage.getGameID(), new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Invalid Move, Please try again"), makeMoveMessage.getAuthString());
        }
        catch  (Exception e) {
            this.sendMessage(makeMoveMessage.getGameID(), new ErrorMessage(ServerMessage.ServerMessageType.ERROR, e.getMessage()), makeMoveMessage.getAuthString());
        }
    }

    public void leaveGame(LeaveMessage leaveMessage, Session session) {
        try {
            String username = leaveGameService.leaveGame(leaveMessage.getGameID(), leaveMessage.getAuthString());
            //If a player is leaving, then the game is updated to remove the root client. Game is updated in the database.

            // Make an updateGame Service. We can use the insertGame method on the DAO
            //Server sends a Notification message to all other clients in that game informing them that the root client left. This applies to both players and observers.
            var message = String.format("%s has left the game", username);
            NotificationMessage notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            this.broadcastMessage(leaveMessage.getGameID(), notificationMessage, leaveMessage.getAuthString());

            // I think we also need to disconnect the user from the session for this one as well.
            sessions.removeSessionFromGame(leaveMessage.getGameID(), leaveMessage.getAuthString(), session);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            //onError(e);
        }
    }

    public void resignGame(ResignMessage resignMessage, Session session) throws Exception {
        try {
            String username = resignService.resign(resignMessage.getGameID(), resignMessage.getAuthString());
            var message = String.format("%s has resigned", username);
            NotificationMessage notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            this.broadcastMessage(resignMessage.getGameID(), notificationMessage, resignMessage.getAuthString());
            this.sendMessage(resignMessage.getGameID(),notificationMessage, resignMessage.getAuthString());
        }
        catch (Exception e) {
            this.sendMessage(resignMessage.getGameID(), new ErrorMessage(ServerMessage.ServerMessageType.ERROR, e.getMessage()), resignMessage.getAuthString());
        }
        //Server marks the game as over (no more moves can be made). Game is updated in the database.
        // How do mark the game as over?
        //Server sends a Notification message to all clients in that game informing them that the root client resigned. This applies to both players and observers.
    }
}
