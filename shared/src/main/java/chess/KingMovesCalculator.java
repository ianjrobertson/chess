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
        ChessPosition potentialPosition;

        if (inBounds(myPosition.getRow() + 1, myPosition.getColumn())) {
            potentialPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
            if (legalMove(potentialPosition, board)) {
                moves.add(new ChessMove(myPosition, potentialPosition, null));
            }
        }
        if (inBounds(myPosition.getRow() + 1, myPosition.getColumn() + 1)) {
            potentialPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
            if (legalMove(potentialPosition, board)) {
                moves.add(new ChessMove(myPosition, potentialPosition, null));
            }
        }
        if (inBounds(myPosition.getRow(), myPosition.getColumn() + 1)) {
            potentialPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1);
            if (legalMove(potentialPosition, board)) {
                moves.add(new ChessMove(myPosition, potentialPosition, null));
            }
        }
        if (inBounds(myPosition.getRow() - 1, myPosition.getColumn() + 1)) {
            potentialPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
            if (legalMove(potentialPosition, board)) {
                moves.add(new ChessMove(myPosition, potentialPosition, null));
            }
        }
        if (inBounds(myPosition.getRow() - 1, myPosition.getColumn())) {
            potentialPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
            if (legalMove(potentialPosition, board)) {
                moves.add(new ChessMove(myPosition, potentialPosition, null));
            }
        }
        if (inBounds(myPosition.getRow() - 1, myPosition.getColumn() - 1)) {
            potentialPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
            if (legalMove(potentialPosition, board)) {
                moves.add(new ChessMove(myPosition, potentialPosition, null));
            }
        }
        if (inBounds(myPosition.getRow() - 1, myPosition.getColumn() - 1)) {
            potentialPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
            if (legalMove(potentialPosition, board)) {
                moves.add(new ChessMove(myPosition, potentialPosition, null));
            }
        }
        if (inBounds(myPosition.getRow(), myPosition.getColumn() - 1)) {
            potentialPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1);
            if (legalMove(potentialPosition, board)) {
                moves.add(new ChessMove(myPosition, potentialPosition, null));
            }
        }
        if (inBounds(myPosition.getRow() + 1, myPosition.getColumn() - 1)) {
            potentialPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
            if (legalMove(potentialPosition, board)) {
                moves.add(new ChessMove(myPosition, potentialPosition, null));
            }
        }

        return moves;
    }
}
