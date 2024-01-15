package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
//Private Instance Variables.
    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;
    private boolean firstMove;
    //Constructor.
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
        this.firstMove = true;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    public boolean isFirstMove() {
        return this.firstMove;
    }

    public void movePiece() {
        this.firstMove = false;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();

        /**
         * Generally -- we take a board and a position and then check the positions around to see whats going on.
         * Each piece has different dimensions we are going to check.
         * Maybe use a try to generalize the boundaries of the board.
         */
        switch (this.type) {
            case KING: {
                //some code here;
                break;
            }
            case QUEEN: {
                //Queen calculatio here
                break;
            }
            case BISHOP: {
                //Bishop calculation here
                break;
            }
            case KNIGHT: {
                //Knight calculatio here
                break;
            }
            case ROOK: {
                //Rook calculations here
                break;
            }
            case PAWN: {
                if ( this.isFirstMove() ) {
                    // If not obstructed
                        //Add the next two positions to the moves
                }
                // If there is a piece in <row + 1, col + 1> or <row + 1, col -1>
                    // Add <row + 1, col + 1> or <row + 1, col -1> to moves.
                    // Need checking if the pawn is on an edge so we don't get an out of bounds exception.
                // If position.col + 1 is not obstructed:
                    // Add position.col + 1 to moves

                //Pawn calculations here
                break;
            }
        }
        return moves;
    }
}
