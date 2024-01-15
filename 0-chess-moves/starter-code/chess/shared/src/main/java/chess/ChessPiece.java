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
         *
         * Capture rules for each piece.
         * Can never move to a position that your own piece is in.
         */
        switch (this.type) {
            case KING: {
                //some code here;
                // Move 1 square in any direction
                // Cannot make a move that would allow the opponent to capture their king
                break;
            }
            case QUEEN: {
                //All moves a Rook and Bishop can make.
                break;
            }
            case BISHOP: {
                //Move in diagonal lines as far as there is open space
                //capture piece at the end of a line.

                //The way we check the Bishop would be similar, but we probably need a double nested loop.
                break;
            }
            case KNIGHT: {
                //Move in L shape. 2 squares in one direction, 1 square in another direction.
                // Can jump over pieces.
                break;
            }
            case ROOK: {
                //Move in straight lines as far as there is open space.
                //Can capture an enemy at the end of the line.

                //searching right
                for (int col = myPosition.getColumn() + 1; col <= 8; col++) {
                    ChessPosition potentialPosition = new ChessPosition(myPosition.getRow(), col);
                    if (legalMove(potentialPosition, board)) {
                        ChessMove move = new ChessMove(myPosition, potentialPosition, null);
                        moves.add(move);
                    }
                }
                //searching left
                for (int col = myPosition.getColumn() - 1; col >= 1; col--) {
                    ChessPosition potentialPosition = new ChessPosition(myPosition.getRow(), col);
                    if (legalMove(potentialPosition, board)) {
                        ChessMove move = new ChessMove(myPosition,potentialPosition, null);
                        moves.add(move);
                    }
                }
                // Search up
                for (int row = myPosition.getRow() + 1; row <= 8; row++) {
                    ChessPosition potentialPosition = new ChessPosition(row, myPosition.getColumn());
                    if (legalMove(potentialPosition, board)) {
                        ChessMove move = new ChessMove(myPosition, potentialPosition, null);
                        moves.add(move);
                    }
                }
                // Search down
                for (int row = myPosition.getRow() - 1; row >= 1; row--) {
                    ChessPosition potentialPosition = new ChessPosition(row, myPosition.getColumn());
                    if (legalMove(potentialPosition, board)) {
                        ChessMove move = new ChessMove(myPosition, potentialPosition, null);
                        moves.add(move);
                    }
                }
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

    private boolean legalMove(ChessPosition potentialPosition, ChessBoard board){
        if (!board.containsPiece(potentialPosition))
            return true;
        else if (board.getPiece(potentialPosition).pieceColor != this.getTeamColor())
            return true;
        else
            return false;
    }
}
