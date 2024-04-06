package webSocketMessages.userCommands;

public class LeaveMessage extends UserGameCommand {
    private Integer gameID;
    public LeaveMessage(String authToken, Integer gameID) {
        super(authToken);
        this.gameID = gameID;
    }

    public Integer getGameID() {
        return this.gameID;
    }
}
