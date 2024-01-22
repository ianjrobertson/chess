package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator extends PieceMovesCalculator {
    public PawnMovesCalculator(ChessGame.TeamColor color) {
        super(color);
    }

    /**
     * legal move implementation for pawn piece.
     * So... if diagonal: must have piece there.
     *
     * @param potentialPosition: potential position to be checked for legality
     * @param board: the current Chess Board.
     * @return boolean of legality of move.
     */
    protected boolean legalMove(ChessPosition potentialPosition, ChessBoard board, boolean attackMove) {
     if (attackMove)
             return board.containsEnemyPiece(potentialPosition);
     return !board.containsPiece(potentialPosition);
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
                    moves.add(new ChessMove(myPosition, potentialPosition, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, potentialPosition, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, potentialPosition, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, potentialPosition, ChessPiece.PieceType.QUEEN));
                }
            }
            potentialPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
            if (legalMove(potentialPosition, board, false)) { //Check if legalMove(row + 1)
                moves.add(new ChessMove(myPosition, potentialPosition, null)); //add move row + 1
            }

            //attack move left
            potentialPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
            if (legalMove(potentialPosition, board, true)) {
                if (myPosition.getRow() == 7) {
                    moves.add(new ChessMove(myPosition, potentialPosition, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, potentialPosition, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, potentialPosition, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, potentialPosition, ChessPiece.PieceType.QUEEN));
                }
                else
                    moves.add(new ChessMove(myPosition, potentialPosition, null));
            }

            //attack move right
            potentialPosition  = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
            if (legalMove(potentialPosition, board,true )) {
                if (myPosition.getRow() == 7) {
                    moves.add(new ChessMove(myPosition, potentialPosition, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, potentialPosition, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, potentialPosition, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, potentialPosition, ChessPiece.PieceType.QUEEN));
                }
                else
                    moves.add(new ChessMove(myPosition, potentialPosition, null));
            }
        }

        //pass control to black movements.
        else if(this.getColor() == ChessGame.TeamColor.BLACK) {
            if (myPosition.getRow() == 7) { //Check if at row == 7. starting move.
                potentialPosition = new ChessPosition(5, myPosition.getColumn());
                if (legalMove(potentialPosition, board, false)) { //Check legal move
                    moves.add(new ChessMove(myPosition, potentialPosition, null)); //add move row -= 2
                }
            }
            if (myPosition.getRow() == 2) { // Check promotion piece
                potentialPosition = new ChessPosition(1, myPosition.getColumn());
                if (legalMove(potentialPosition, board, false)) {
                    moves.add(new ChessMove(myPosition, potentialPosition, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, potentialPosition, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, potentialPosition, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, potentialPosition, ChessPiece.PieceType.QUEEN));                }
            }
            //standard forward move
            potentialPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
            if (legalMove(potentialPosition, board, false)) { //Check if legalMove(row + 1)
                moves.add(new ChessMove(myPosition, potentialPosition, null)); //add move row + 1
            }

            //attack move left
            potentialPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
            if (legalMove(potentialPosition, board, true)) {
                if (myPosition.getRow() == 7) {
                    moves.add(new ChessMove(myPosition, potentialPosition, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, potentialPosition, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, potentialPosition, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, potentialPosition, ChessPiece.PieceType.QUEEN));
                }
                else
                    moves.add(new ChessMove(myPosition, potentialPosition, null));
            }

            //attack move right
            potentialPosition  = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
            if (legalMove(potentialPosition, board,true )) {
                if (myPosition.getRow() == 7) {
                    moves.add(new ChessMove(myPosition, potentialPosition, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, potentialPosition, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, potentialPosition, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, potentialPosition, ChessPiece.PieceType.QUEEN));
                }
                else
                    moves.add(new ChessMove(myPosition, potentialPosition, null));
            }
        }

        return moves;
    }
}
