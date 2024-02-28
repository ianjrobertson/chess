package chess.MovesCalculator;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator extends PieceMovesCalculator {
    public KnightMovesCalculator(ChessGame.TeamColor color) {
        super(color);
    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();

        //Check the 8 possible locations

        //If out of bounds
        ChessPosition potentialPosition;
        int myCol = myPosition.getColumn();
        int myRow = myPosition.getRow();

        if (inBounds(myRow + 2, myCol + 1)) {
            potentialPosition = new ChessPosition(myRow + 2, myCol + 1);
            if (legalMove(potentialPosition, board)) {
                moves.add(new ChessMove(myPosition, potentialPosition, null));
            }
        }
        if (inBounds(myRow + 1, myCol + 2)) {
            potentialPosition = new ChessPosition(myRow + 1, myCol + 2);
            if (legalMove(potentialPosition, board)) {
                moves.add(new ChessMove(myPosition, potentialPosition, null));
            }
        }
        if (inBounds(myRow - 1, myCol + 2)) {
            potentialPosition = new ChessPosition(myRow - 1, myCol + 2);
            if (legalMove(potentialPosition, board)) {
                moves.add(new ChessMove(myPosition, potentialPosition, null));
            }
        }
        if (inBounds(myRow - 2, myCol + 1)) {
            potentialPosition = new ChessPosition(myRow - 2, myCol + 1);
            if (legalMove(potentialPosition, board)) {
                moves.add(new ChessMove(myPosition, potentialPosition, null));
            }
        }
        if (inBounds(myRow - 2, myCol - 1)) {
            potentialPosition = new ChessPosition(myRow - 2, myCol - 1);
            if (legalMove(potentialPosition, board)) {
                moves.add(new ChessMove(myPosition, potentialPosition, null));
            }
        }
        if (inBounds(myRow - 1, myCol - 2)) {
            potentialPosition = new ChessPosition(myRow - 1, myCol - 2);
            if (legalMove(potentialPosition, board)) {
                moves.add(new ChessMove(myPosition, potentialPosition, null));
            }
        }
        if (inBounds(myRow + 1, myCol - 2)) {
            potentialPosition = new ChessPosition(myRow + 1, myCol - 2);
            if (legalMove(potentialPosition, board)) {
                moves.add(new ChessMove(myPosition, potentialPosition, null));
            }
        }
        if (inBounds(myRow + 2, myCol - 1)) {
            potentialPosition = new ChessPosition(myRow + 2, myCol - 1);
            if (legalMove(potentialPosition, board)) {
                moves.add(new ChessMove(myPosition, potentialPosition, null));
            }
        }
        return moves;
    }
}
