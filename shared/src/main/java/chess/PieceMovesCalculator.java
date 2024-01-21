package chess;

import java.util.ArrayList;
import java.util.Collection;

public abstract class PieceMovesCalculator {
    protected ChessGame.TeamColor color;
    protected PieceMovesCalculator(ChessGame.TeamColor color) {
        this.color = color;
    }
    public abstract Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);

    protected boolean legalMove(ChessPosition potentialPosition, ChessBoard board){
        if (!board.containsPiece(potentialPosition))
            return true;
        else if (board.getPiece(potentialPosition).getTeamColor() != this.getColor())
            return true;
        else
            return false;
    }

    protected ChessGame.TeamColor getColor() {
        return this.color;
    }

}
