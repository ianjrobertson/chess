package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerMessage extends UserGameCommand{
    private Integer gameID;
    private ChessGame.TeamColor playerColor;
    public JoinPlayerMessage(CommandType commandType, String authToken, Integer gameID, ChessGame.TeamColor playerColor) {
        super(authToken);
        this.gameID = gameID;
        this.playerColor = playerColor;
        this.commandType = commandType;
    }

    public Integer getGameID() {
        return this.gameID;
    }

    public ChessGame.TeamColor getColor() {
        return this.playerColor;
    }
}
