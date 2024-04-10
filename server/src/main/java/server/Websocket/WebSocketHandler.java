package server.Websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.glassfish.tyrus.spi.Connection;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;
import org.eclipse.jetty.websocket.api.Session;

import javax.swing.*;
import java.util.ArrayList;

@WebSocket
public class WebSocketHandler {
    private WebSocketSessions sessions = new WebSocketSessions();

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

    public void sendMessage(Integer gameID, String message, String authToken) {
        //how do we send a message?
        // Send these and they are handled by the WebSocketFacade

        // So we iterate through the session map. Find the game and the authToken
        // And then send the message through gameSession.get(s).getRemote().sendString(message);

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

    public void joinPlayer(UserGameCommand command, Session session) throws Exception {
        JoinPlayerMessage joinPlayerMessage = (JoinPlayerMessage) command;
        sessions.addSession(joinPlayerMessage.getGameID(), joinPlayerMessage.getAuthString(), session);
        //1. Server sends a loadGame message back to the root client

        //this.sendMessage(joinPlayerMessage.getGameID(), ); //review everything we have done

        //How do we get the name of the player

        //2. Server sends a notification message to all users in the session.
        var message = String.format("Joined the game as %s", joinPlayerMessage.getColor());
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        this.broadcastMessage(joinPlayerMessage.getGameID(), notification, joinPlayerMessage.getAuthString());
    }

    public void joinObserver(UserGameCommand command, Session session) {
        JoinObserverMessage joinObserverMessage = (JoinObserverMessage) command;

        // Server sends a LOAD_GAME message back to the root client.
        // Server sends a Notification message to all other clients in that game informing them the root client joined as an observer.
    }

    public void makeMove(UserGameCommand command, Session session) {
        MakeMoveMessage makeMoveMessage = (MakeMoveMessage) command;
        //Server verifies the validity of the move
        //Game is updated to represent the move. Game is updated in the database.
        //Server sends a LOAD_GAME message to all clients in the game (including the root client) with an updated game.
        //Server sends a Notification message to all other clients in that game informing them what move was made.

    }

    public void leaveGame(UserGameCommand command, Session session) {
        LeaveMessage leaveMessage = (LeaveMessage) command;

        //If a player is leaving, then the game is updated to remove the root client. Game is updated in the database.
        //Server sends a Notification message to all other clients in that game informing them that the root client left. This applies to both players and observers.

    }

    public void resignGame(UserGameCommand command, Session session) {
        ResignMessage resignMessage = (ResignMessage) command;

        //Server marks the game as over (no more moves can be made). Game is updated in the database.
        //Server sends a Notification message to all clients in that game informing them that the root client resigned. This applies to both players and observers.
    }
}
