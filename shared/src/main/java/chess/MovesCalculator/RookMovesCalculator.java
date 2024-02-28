package chess.MovesCalculator;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

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
            illegalMove = determineMove(myPosition, moves, board, illegalMove, myPosition.getRow(),col);
        }
        //searching left
        illegalMove = false;
        for (int col = myPosition.getColumn() - 1; col >= 1 && !illegalMove; col--) {
            illegalMove = determineMove(myPosition, moves, board, illegalMove, myPosition.getRow(), col);
        }

        // Search down
        illegalMove = false;
        for (int row = myPosition.getRow() - 1; row >= 1 && !illegalMove; row--) {
            illegalMove = determineMove(myPosition, moves, board, illegalMove, row, myPosition.getColumn());
        }

        // Search up
        illegalMove = false;
        for (int row = myPosition.getRow() + 1; row <= 8 && !illegalMove; row++) {
           illegalMove = determineMove(myPosition, moves, board, illegalMove, row, myPosition.getColumn());
        }
        return moves;
    }

    private boolean determineMove(ChessPosition myPosition, Collection<ChessMove> moves, ChessBoard board, boolean illegalMove, int row, int col) {
        ChessPosition potentialPosition = new ChessPosition(row, col);
        if (legalMove(potentialPosition, board)) {
            ChessMove move = new ChessMove(myPosition, potentialPosition, null);
            moves.add(move);
            if (board.containsEnemyPiece(potentialPosition, this.getColor())) {
                illegalMove = true;
            }
        }
        else
            illegalMove = true;

        return illegalMove;
    }

}



