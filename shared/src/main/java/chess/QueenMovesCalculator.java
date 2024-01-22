package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;

public class QueenMovesCalculator extends PieceMovesCalculator {
    public QueenMovesCalculator(ChessGame.TeamColor color) {
        super(color);
    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();

        // The queen can do everything that a bishop and rook can do.

        //------------------Bishop type movements---------------
        ChessPosition potentialPosition;
        boolean illegalMove = false;

        //iterate diagonally up right
        for (int row = myPosition.getRow(); row <= 8 && !illegalMove; row++) {
            for (int col = myPosition.getColumn(); col <= 8 && !illegalMove; col++) {
                potentialPosition = new ChessPosition(row, col);
                if (legalMove(potentialPosition, board)) {
                    moves.add(new ChessMove(myPosition, potentialPosition, null)); }
                else
                    illegalMove = true;
            }
        }

        //iterate diagonally down left
        illegalMove = false;
        for (int row = myPosition.getRow(); row >= 1 && !illegalMove; row--) {
            for (int col = myPosition.getColumn(); col >= 1 && !illegalMove; col--) {
                potentialPosition = new ChessPosition(row, col);
                if (legalMove(potentialPosition, board)) {
                    moves.add(new ChessMove(myPosition, potentialPosition, null));
                }
                else
                    illegalMove = true;

            }
        }

        //iterate diagaonlly up left
        illegalMove = false;
        for (int row = myPosition.getRow(); row <= 8 && !illegalMove; row++) {
            for (int col = myPosition.getColumn(); col >= 1 && !illegalMove; col--) {
                potentialPosition = new ChessPosition(row, col);
                if (legalMove(potentialPosition, board)) {
                    moves.add(new ChessMove(myPosition, potentialPosition, null));
                }
                else
                    illegalMove = true;
            }
        }

        //iterate diagonally down right
        illegalMove = false;
        for (int row = myPosition.getRow(); row >= 1 && !illegalMove; row--) {
            for (int col = myPosition.getColumn(); col <= 8 && !illegalMove; col++) {
                potentialPosition = new ChessPosition(row, col);
                if (legalMove(potentialPosition, board)) {
                    moves.add(new ChessMove(myPosition, potentialPosition, null));
                }
                else
                    illegalMove = true;
            }
        }

        //---------------------Rook type movements ----------------------

        illegalMove = false; //illegal move flag

        //Move in straight lines as far as there is open space.
        //Can capture an enemy at the end of the line.
        //searching right
        for (int col = myPosition.getColumn() + 1; col <= 8 && !illegalMove; col++) {
            potentialPosition = new ChessPosition(myPosition.getRow(), col);
            if (legalMove(potentialPosition, board)) {
                ChessMove move = new ChessMove(myPosition, potentialPosition, null);
                moves.add(move);
            }
            else
                illegalMove = true;
        }
        //searching left
        illegalMove = false;
        for (int col = myPosition.getColumn() - 1; col >= 1 && !illegalMove; col--) {
            potentialPosition = new ChessPosition(myPosition.getRow(), col);
            if (legalMove(potentialPosition, board)) {
                ChessMove move = new ChessMove(myPosition,potentialPosition, null);
                moves.add(move);
            }
            else
                illegalMove = true;
        }
        // Search up
        illegalMove = false;
        for (int row = myPosition.getRow() + 1; row <= 8 && !illegalMove; row++) {
            potentialPosition = new ChessPosition(row, myPosition.getColumn());
            if (legalMove(potentialPosition, board)) {
                ChessMove move = new ChessMove(myPosition, potentialPosition, null);
                moves.add(move);
            }
            else
                illegalMove = true;
        }
        // Search down
        illegalMove = false;
        for (int row = myPosition.getRow() - 1; row >= 1 && !illegalMove; row--) {
            potentialPosition = new ChessPosition(row, myPosition.getColumn());
            if (legalMove(potentialPosition, board)) {
                ChessMove move = new ChessMove(myPosition, potentialPosition, null);
                moves.add(move);
            }
            else illegalMove = true;
        }

        return moves;
    }
}
