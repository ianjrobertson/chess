package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerMessage extends UserGameCommand{

    private Integer gameID;
    private ChessGame.TeamColor color;
    public JoinPlayerMessage(String authToken, Integer gameID, ChessGame.TeamColor color) {
        super(authToken);
        this.gameID = gameID;
        this.color = color;
    }
}
