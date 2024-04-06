package webSocketMessages.serverMessages;

public class NotificationMessage extends ServerMessage {
    private String message;

    public NotificationMessage(ServerMessageType messageType, String message) {
        super(messageType);
        this.message = message;
    }

    private String getMessage() {
        return this.message;
    }
}
