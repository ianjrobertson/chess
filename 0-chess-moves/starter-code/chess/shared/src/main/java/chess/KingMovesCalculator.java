package chess;

import java.util.Collection;

public class KingMovesCalculator extends PieceMovesCalculator {

    public KingMovesCalculator(ChessGame.TeamColor color) {
        super(color);
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return null;
    }
}
