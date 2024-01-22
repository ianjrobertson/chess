package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator extends PieceMovesCalculator {
    public PawnMovesCalculator(ChessGame.TeamColor color) {
        super(color);
    }

    /**
     * legal move implementation for pawn piece.
     *
     * @param potentialPosition
     * @param board
     * @return boolean of legality of move.
     */
    @Override
    protected boolean legalMove(ChessPosition potentialPosition, ChessBoard board, boolean attackMove) {
        if (!board.containsPiece(potentialPosition) && !attackMove)
            return true;
        else if (board.getPiece(potentialPosition).getTeamColor() != this.getColor())
            return true;
        else
            return false;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPosition potentialPosition;
        //Pass control to white piece movements.
        if (this.getColor() == ChessGame.TeamColor.WHITE) {
            if (myPosition.getRow() == 2) { //Check if at row == 2
               potentialPosition = new ChessPosition(4, myPosition.getColumn());
               if (legalMove(potentialPosition, board, false)) { //Check legal move
                   moves.add(new ChessMove(myPosition, potentialPosition, null)); //add move row += 1
               }
            }
            if (myPosition.getRow() == 7) { // Check promotion piece
                potentialPosition = new ChessPosition(8, myPosition.getColumn());
                if (legalMove(potentialPosition, board, false)) {
                    moves.add(new ChessMove(myPosition, potentialPosition, null)); //TODO add implementation for promotion piece
                }
            }
            potentialPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
            if (legalMove(potentialPosition, board)) { //Check if legalMove(row + 1)
                moves.add(new ChessMove(myPosition, potentialPosition, null)); //add move row + 1
            }

            //attack move left
            potentialPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
            if (legalMove(potentialPosition, board, true)) {
                moves.add(new ChessMove(myPosition, potentialPosition, null));
            }

            //attack move right
            potentialPosition  = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
            if (legalMove(potentialPosition, board, true)) {
                moves.add(new ChessMove(myPosition, potentialPosition, null));
            }
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
