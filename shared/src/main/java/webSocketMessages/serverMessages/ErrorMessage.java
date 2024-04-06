package webSocketMessages.serverMessages;

public class ErrorMessage extends ServerMessage {
    private String errorMessage;
    public ErrorMessage(ServerMessageType messageType, String errorMessage) {
        super(messageType);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}
