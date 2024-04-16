package WebSocketFacade;

import com.google.gson.Gson;
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
    private final GameHandler gameHandler;

    public WebSocketFacade(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void connect() throws Exception {
        URI uri = new URI("ws://localhost:8080/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                switch (serverMessage.getServerMessageType()) {
                    case ServerMessage.ServerMessageType.NOTIFICATION: {
                        NotificationMessage notificationMessage = new Gson().fromJson(message, NotificationMessage.class);
                        gameHandler.printMessage(notificationMessage.getMessage());
                        break;
                    }
                    case ServerMessage.ServerMessageType.ERROR: {
                        ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
                        gameHandler.printMessage(errorMessage.getErrorMessage());
                        break;
                    }
                    case ServerMessage.ServerMessageType.LOAD_GAME: {
                        LoadGameMessage loadGameMessage = new Gson().fromJson(message, LoadGameMessage.class);
                        gameHandler.updateGame(loadGameMessage);
                        break;
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


