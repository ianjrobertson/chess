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
        if (!inBounds(potentialPosition.getRow(), potentialPosition.getColumn()))
            return false;
        if (!board.containsPiece(potentialPosition))
            return true;
        else return board.getPiece(potentialPosition).getTeamColor() != this.getColor();
    }

    protected boolean inBounds(int row, int col) {
        return (row <= 8 && row >= 1) && (col <= 8 && col >= 1);
    }

    protected ChessGame.TeamColor getColor() {
        return this.color;
    }

}
