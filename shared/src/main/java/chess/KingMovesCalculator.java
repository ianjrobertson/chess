package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator extends PieceMovesCalculator {

    public KingMovesCalculator(ChessGame.TeamColor color) {
        super(color);
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();

        // Check the 8 possible positions.

        if (inBounds(myPosition.getRow() + 1, myPosition.getColumn())) {
            //check legal move, then add
        }
        if (inBounds(myPosition.getRow() + 1, myPosition.getColumn() + 1)) {
            //check legal, then add
        }
        if (inBounds(myPosition.getRow(), myPosition.getColumn() + 1)) {

        }
        if (inBounds(myPosition.getRow(), myPosition.getColumn() + 1)) {

        }
        if (inBounds(myPosition.getRow() - 1, myPosition.getColumn() + 1)) {

        }
        if (inBounds(myPosition.getRow() - 1, myPosition.getColumn())) {

        }
        if (inBounds(myPosition.getRow() - 1, myPosition.getColumn() - 1)) {

        }
        if (inBounds(myPosition.getRow(), myPosition.getColumn() - 1)) {

        }
        if (inBounds(myPosition.getRow() + 1, myPosition.getColumn() - 1)) {

        }

        return moves;
    }
}
