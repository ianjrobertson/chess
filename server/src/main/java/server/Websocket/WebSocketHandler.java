package server.Websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.Session;

public class WebSocketHandler {
    private WebSocketSessions sessions;

    public WebSocketHandler() {
        this.sessions = new WebSocketSessions();
    }
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
    public void onMessage(Session session, String message) {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

    }

    public void sendMessage() {

    }
}
