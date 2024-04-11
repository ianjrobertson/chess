package server.Websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.MovesCalculator.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.*;
import service.JoinService;
import service.LeaveGameService;
import service.ListGamesService;
import service.MakeMoveService;
import service.ServiceRecords.ListGamesRequest;
import service.ServiceRecords.ListGamesResponse;
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

    @OnWebSocketConnect
    public void onConnect(Session session) {

    }
    @OnWebSocketClose
    public void onClose(Session session) {

    }
    @OnWebSocketError
    public void onError(Throwable exception) {

    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case UserGameCommand.CommandType.JOIN_PLAYER -> this.joinPlayer(command, session);
            case UserGameCommand.CommandType.JOIN_OBSERVER -> this.joinObserver(command, session);
            case UserGameCommand.CommandType.MAKE_MOVE -> this.makeMove(command, session);
            case UserGameCommand.CommandType.LEAVE -> this.leaveGame(command, session);
            case UserGameCommand.CommandType.RESIGN -> this.resignGame(command, session);
        }
    }

    public void sendMessage(Integer gameID, ServerMessage message, String authToken) throws Exception {
        //I think this is the same as the broadcastMessage but it sends on the alternative case?

        // Send these and they are handled by the WebSocketFacade
        // serialize the server message object
        //Should we serialize it before?
        var gameSession = sessions.getSessionsForGame(gameID);
        var removeList = new ArrayList<Session>();
        for (var s: gameSession.keySet()) {
            if (gameSession.get(s).isOpen()) {
                if (s.equals(authToken)) {
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

        // So we iterate through the session map. Find the game and the authToken
        // And then send the message through gameSession.get(s).getRemote().sendString(message);

    }

    private GameData getGame(Integer gameID, String authToken) {
        try {
            ListGamesResponse res = listGamesService.listGames(new ListGamesRequest(authToken));
            for (GameData game: res.games()) {
                if (game.gameID() == gameID) {
                    return game;
                }
            }
            onError(new Throwable("Invalid Game ID"));
        }
        catch (DataAccessException d) {
            onError(d);
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

    public void joinPlayer(UserGameCommand command, Session session) {
        try {
            JoinPlayerMessage joinPlayerMessage = (JoinPlayerMessage) command;
            sessions.addSession(joinPlayerMessage.getGameID(), joinPlayerMessage.getAuthString(), session);
            //1. Server sends a loadGame message back to the root client
            GameData game = this.getGame(joinPlayerMessage.getGameID(), joinPlayerMessage.getAuthString());

            LoadGameMessage loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game.game());
            this.sendMessage(joinPlayerMessage.getGameID(), loadGameMessage, joinPlayerMessage.getAuthString() ); //review everything we have done

            //2. Server sends a notification message to all users in the session.
            String username = joinService.join(joinPlayerMessage.getAuthString());
            var message = String.format("%s joined the game as %s", username, joinPlayerMessage.getColor());
            var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            this.broadcastMessage(joinPlayerMessage.getGameID(), notification, joinPlayerMessage.getAuthString());
        }
        catch (Exception e) {
            onError(e);
        }
    }

    public void joinObserver(UserGameCommand command, Session session) {
        try {
            JoinObserverMessage joinObserverMessage = (JoinObserverMessage) command;
            sessions.addSession(joinObserverMessage.getGameID(), joinObserverMessage.getAuthString(), session);
            // Server sends a LOAD_GAME message back to the root client.
            GameData game = this.getGame(joinObserverMessage.getGameID(), joinObserverMessage.getAuthString());

            LoadGameMessage loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game.game());
            this.sendMessage(joinObserverMessage.getGameID(), loadGameMessage, joinObserverMessage.getAuthString());

            // Server sends a Notification message to all other clients in that game informing them the root client joined as an observer.
            String username = joinService.join(command.getAuthString()); // the problem we have is the join Observer won't have a team color...
            var message = String.format("%s joined the game as an observer", username);
            var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            this.broadcastMessage(joinObserverMessage.getGameID(), notification, joinObserverMessage.getAuthString());

        }
        catch(Exception e) {
            onError(e);
        }
    }

    public void makeMove(UserGameCommand command, Session session) {
        try {
            MakeMoveMessage makeMoveMessage = (MakeMoveMessage) command;
            //Server verifies the validity of the move
            //Game is updated to represent the move. Game is updated in the database.
            //Todo make makeMove service that updates the current gameBoard
            ChessGame game = makeMoveService.makeMove(makeMoveMessage.getGameID(), makeMoveMessage.getAuthString(), makeMoveMessage.getMove()); // -> makeMoveService validates move through makeMove in ChessGame class. Throws invalidMoveException

            //Server sends a LOAD_GAME message to all clients in the game (including the root client) with an updated game.
            LoadGameMessage loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
            this.sendMessage(makeMoveMessage.getGameID(), loadGameMessage, makeMoveMessage.getAuthString());


            //TODO make a toString() method for chess move - could just have the name of the piece and where the now location is
            //Server sends a Notification message to all other clients in that game informing them what move was made.
            var message = String.format(makeMoveMessage.getMove().toString());
            var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            this.broadcastMessage(makeMoveMessage.getGameID(), notification, makeMoveMessage.getAuthString());
        }
        catch  (Exception e) {
            onError(e); //need more specific error handling for invalid move exception
        }
    }

    public void leaveGame(UserGameCommand command, Session session) {
        try {
            LeaveMessage leaveMessage = (LeaveMessage) command;
            String username = leaveGameService.leaveGame(leaveMessage.getGameID(), leaveMessage.getAuthString());
            //If a player is leaving, then the game is updated to remove the root client. Game is updated in the database.

            // Make an updateGame Service. We can use the insertGame method on the DAO

            //TODO make updateGame Service
            //Server sends a Notification message to all other clients in that game informing them that the root client left. This applies to both players and observers.
            var message = String.format("%s has left the game", username);
            NotificationMessage notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            this.broadcastMessage(leaveMessage.getGameID(), notificationMessage, leaveMessage.getAuthString());
        }
        catch (Exception e) {
            onError(e);
        }
    }

    public void resignGame(UserGameCommand command, Session session) {
        ResignMessage resignMessage = (ResignMessage) command;

        //Server marks the game as over (no more moves can be made). Game is updated in the database.
        // How do mark the game as over?
        //Server sends a Notification message to all clients in that game informing them that the root client resigned. This applies to both players and observers.
    }
}
