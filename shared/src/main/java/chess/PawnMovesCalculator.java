package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator extends PieceMovesCalculator {
    public PawnMovesCalculator(ChessGame.TeamColor color) {
        super(color);
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();

        //Pass control to white piece movements.
        if (this.getColor() == ChessGame.TeamColor.WHITE) {
            //Check if at row == 2
                //Check legal move
                    //add move row += 1
            //Check if legalMove(row + 1)
                //add move row + 1
            //Check if legalMove(row + 1, col + 1)
                //add move row + 1, col + 1
        }
        //pass control to black movements.
        else if(this.getColor() == ChessGame.TeamColor.BLACK) {
            //Check if at row == 7
                //Check legal move
                    //add move. row -= 2
            //Check if legalMove(row + 1)
                //add move row - 1
            //Check if legalMove(row + 1, col + 1)
                //add move row + 1, col + 1
        }

        return moves;
    }
}
