package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator extends PieceMovesCalculator {

    public RookMovesCalculator(ChessGame.TeamColor color) {
        super(color);
    }

    /**
     * Iterates through rook paths and adds moves to list until an illegal move is encountered.
     *
     * @param board: current ChessBoard
     * @param myPosition: current Position
     * @return moves: list of legal moves.
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPosition potentialPosition;
        boolean illegalMove = false; //illegal move flag

        //Move in straight lines as far as there is open space.
        //Can capture an enemy at the end of the line.
        //searching right
        for (int col = myPosition.getColumn() + 1; col <= 8 && !illegalMove; col++) {
            potentialPosition = new ChessPosition(myPosition.getRow(), col);
            if (legalMove(potentialPosition, board)) {
                ChessMove move = new ChessMove(myPosition, potentialPosition, null);
                moves.add(move);
                if (board.containsEnemyPiece(potentialPosition, this.getColor())) {
                    illegalMove = true;
                }
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
                if (board.containsEnemyPiece(potentialPosition, this.getColor())) {
                    illegalMove = true;
                }
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
                if (board.containsEnemyPiece(potentialPosition, this.getColor())) {
                    illegalMove = true;
                }
            }
            else illegalMove = true;
        }

        // Search up
        illegalMove = false;
        for (int row = myPosition.getRow() + 1; row <= 8 && !illegalMove; row++) {
            potentialPosition = new ChessPosition(row, myPosition.getColumn());
            if (legalMove(potentialPosition, board)) {
                ChessMove move = new ChessMove(myPosition, potentialPosition, null);
                moves.add(move);
                if (board.containsEnemyPiece(potentialPosition, this.getColor())) {
                    illegalMove = true;
                }
            }
            else
                illegalMove = true;
        }
        return moves;
    }
}
