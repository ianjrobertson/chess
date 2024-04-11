package webSocketMessages.userCommands;

public class ResignMessage extends UserGameCommand {
    private Integer gameID;
    public ResignMessage(String authToken, Integer gameID) {
        super(authToken);
        this.gameID = gameID;
    }

    public Integer getGameID() {
        return this.gameID;
    }
}
