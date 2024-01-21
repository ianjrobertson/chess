package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator extends PieceMovesCalculator {

    public BishopMovesCalculator(ChessGame.TeamColor color) {
        super(color);
    }

    /**
     * Iterates through the possible positions for a bishop. Terminates if encounters illegal moves along the path.
     *
     * @param board: current ChessBoard
     * @param myPosition: current position
     * @return moves: list of legal moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        // search the board diagonally

        Collection<ChessMove> moves = new ArrayList<>();
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
        return moves;
    }
}
