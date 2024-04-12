package webSocketMessages.userCommands;

public class ResignMessage extends UserGameCommand {
    private Integer gameID;
    public ResignMessage(CommandType commandType, String authToken, Integer gameID) {
        super(authToken);
        this.gameID = gameID;
        this.commandType = commandType;
    }

    public Integer getGameID() {
        return this.gameID;
    }
}
