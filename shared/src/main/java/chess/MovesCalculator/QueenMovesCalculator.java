package chess.MovesCalculator;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

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
