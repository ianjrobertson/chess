package webSocketMessages.userCommands;

public class JoinObserverMessage extends UserGameCommand {
    private Integer gameID;
    public JoinObserverMessage(String authToken, CommandType commandType, Integer gameID) {
        super(authToken);
        this.commandType = commandType;
        this.gameID = gameID;
    }

    public Integer getGameID() {
        return this.gameID;
    }
}
