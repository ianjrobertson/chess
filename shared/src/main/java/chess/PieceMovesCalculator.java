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
        //Might be good to call inbounds here. since a move that is out of bounds is never a legal move.
        if (!board.containsPiece(potentialPosition))
            return true;
        else if (board.getPiece(potentialPosition).getTeamColor() != this.getColor())
            return true;
        else
            return false;

        //TODO add attack move handling for pawns
        // If piece type is pawn, and there is an enemy piece row + 1, col + 1, that is legal move. But not a legal move if just empty.
    }

    protected boolean inBounds(int row, int col) {
        return (row <= 8 && row >= 1) && (col <= 8 && col >= 1);
    }

    protected ChessGame.TeamColor getColor() {
        return this.color;
    }

}
