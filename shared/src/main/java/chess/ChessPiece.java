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
                BishopMovesCalculator bishopMovesCalculator = new BishopMovesCalculator(this.getTeamColor());
                return bishopMovesCalculator.pieceMoves(board, myPosition);
            }
            case KNIGHT: {
                //Move in L shape. 2 squares in one direction, 1 square in another direction.
                KnightMovesCalculator knightMovesCalculator = new KnightMovesCalculator(this.getTeamColor());
                return knightMovesCalculator.pieceMoves(board, myPosition);
            }
            case ROOK: {
                RookMovesCalculator rookMovesCalculator = new RookMovesCalculator(this.getTeamColor());
                return rookMovesCalculator.pieceMoves(board, myPosition);
            }
            case PAWN: {
                PawnMovesCalculator pawnMovesCalculator = new PawnMovesCalculator(this.getTeamColor());
                return pawnMovesCalculator.pieceMoves(board, myPosition);
            }
            //TODO add default case
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
