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
        BishopMovesCalculator bishopMovesCalculator = new BishopMovesCalculator(this.getColor());
        moves.addAll(bishopMovesCalculator.pieceMoves(board, myPosition));

        //---------------------Rook type movements ----------------------

        RookMovesCalculator rookMovesCalculator = new RookMovesCalculator(this.getColor());
        moves.addAll(rookMovesCalculator.pieceMoves(board, myPosition));

        return moves;
    }
}
