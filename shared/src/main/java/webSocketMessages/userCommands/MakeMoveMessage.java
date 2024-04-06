package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMoveMessage extends UserGameCommand {
    private Integer gameID;
    private ChessMove move;
    public MakeMoveMessage(CommandType commandType, String authToken, Integer gameID, ChessMove move) {
        super(authToken);
        this.commandType = commandType;
        this.gameID = gameID;
        this.move = move;
    }

    public Integer getGameID() {
        return this.gameID;
    }

    public ChessMove getMove() {
        return this.move;
    }
}
