package WebSocketFacade;

import com.google.gson.Gson;
import server.Server;
import ui.GameHandler;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import javax.websocket.*;
import java.net.URI;

public class WebSocketFacade extends Endpoint {
    private Session session;
    private GameHandler gameHandler;

    public WebSocketFacade() {

    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void connect() throws Exception {
        URI uri = new URI("ws://localhost:8080/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                switch (serverMessage.getServerMessageType()) {
                    case ServerMessage.ServerMessageType.NOTIFICATION: {
                        NotificationMessage notificationMessage = (NotificationMessage) serverMessage;
                        gameHandler.printMessage(notificationMessage.getMessage());
                    }
                    case ServerMessage.ServerMessageType.ERROR: {
                        ErrorMessage errorMessage = (ErrorMessage) serverMessage;
                        gameHandler.printMessage(errorMessage.getErrorMessage());
                    }
                    case ServerMessage.ServerMessageType.LOAD_GAME : {
                        LoadGameMessage loadGameMessage = (LoadGameMessage) serverMessage;
                        gameHandler.updateGame(loadGameMessage);
                    }
                }
            }
        });

    }

    public void send(String message) throws Exception {
        this.session.getBasicRemote().sendText(message);
    }

    public void disconnect() {

    }

    public void joinPlayer(JoinPlayerMessage message) throws Exception {
        this.send(new Gson().toJson(message));
    }

    public void joinObserver(JoinObserverMessage message) throws Exception {
        this.send(new Gson().toJson(message));
    }

    public void makeMove(MakeMoveMessage message) throws Exception {
        this.send(new Gson().toJson(message));
    }

    public void leaveGame(LeaveMessage message) throws Exception {
        this.send(new Gson().toJson(message));
    }

    public void resignGame(ResignMessage message) throws Exception {
        this.send(new Gson().toJson(message));
    }
}


