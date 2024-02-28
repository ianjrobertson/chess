package chess.MovesCalculator;

import chess.*;

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
        for (int i = 1; (myPosition.getRow() + i <= 8) && (myPosition.getColumn() + i <= 8) && !illegalMove; i++) {
            potentialPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i);
            if (legalMove(potentialPosition, board)) {
                moves.add(new ChessMove(myPosition, potentialPosition, null));
                if (board.containsEnemyPiece(potentialPosition, this.getColor())) {
                    illegalMove = true;
                }
            }
            else
                illegalMove = true;
        }

        illegalMove = false;
        //iterate diagonnally down right
        for (int i = 1; (myPosition.getRow() - i >= 1) && (myPosition.getColumn() + i <= 8) && !illegalMove; i++) {
            potentialPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + i);
            if (legalMove(potentialPosition, board)) {
                moves.add(new ChessMove(myPosition, potentialPosition, null));
                if (board.containsEnemyPiece(potentialPosition, this.getColor())) {
                    illegalMove = true;
                }
            }
            else
                illegalMove = true;
        }

        illegalMove = false;
        //iterate diagonally down left
        for (int i = 1; (myPosition.getRow() - i >= 1) && (myPosition.getColumn() - i >= 1) && !illegalMove; i++) {
            potentialPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - i);
            if (legalMove(potentialPosition, board)) {
                moves.add(new ChessMove(myPosition, potentialPosition, null));
                if (board.containsEnemyPiece(potentialPosition, this.getColor())) {
                    illegalMove = true;
                }
            }
            else
                illegalMove = true;
        }

        illegalMove = false;
        //iterate diagonally up left
        for (int i = 1; (myPosition.getRow() + i <= 8) && (myPosition.getColumn() - i >= 1) && !illegalMove; i++) {
            potentialPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - i);
            if (legalMove(potentialPosition, board)) {
                moves.add(new ChessMove(myPosition, potentialPosition, null));
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
