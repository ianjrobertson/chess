package webSocketMessages.userCommands;

public class LeaveMessage extends UserGameCommand {
    private Integer gameID;
    public LeaveMessage(String authToken, Integer gameID, UserGameCommand.CommandType commandType) {
        super(authToken);
        this.gameID = gameID;
        this.commandType = commandType;
    }

    public Integer getGameID() {
        return this.gameID;
    }
}
