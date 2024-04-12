package chess;

import chess.MovesCalculator.*;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private static final String UNICODE_ESCAPE = "\u001b";
    private static final String SET_TEXT_COLOR = UNICODE_ESCAPE + "[38;5;";
    public static final String RESET_TEXT_COLOR = SET_TEXT_COLOR + "0m";
    public static final String SET_TEXT_COLOR_WHITE = SET_TEXT_COLOR + "15m";
    public static final String SET_TEXT_COLOR_RED = SET_TEXT_COLOR + "160m";
    public static final String SET_TEXT_COLOR_GREEN = SET_TEXT_COLOR + "46m";
    public static final String SET_TEXT_COLOR_LIGHT_GREY = SET_TEXT_COLOR + "242m";
    public static final String SET_TEXT_COLOR_BLUE = SET_TEXT_COLOR + "12m";
    public static final String WHITE_KING = " ♔ ";
    public static final String WHITE_QUEEN = " ♕ ";
    public static final String WHITE_BISHOP = " ♗ ";
    public static final String WHITE_KNIGHT = " ♘ ";
    public static final String WHITE_ROOK = " ♖ ";
    public static final String WHITE_PAWN = " ♙ ";
    public static final String BLACK_KING = " ♚ ";
    public static final String BLACK_QUEEN = " ♛ ";
    public static final String BLACK_BISHOP = " ♝ ";
    public static final String BLACK_KNIGHT = " ♞ ";
    public static final String BLACK_ROOK = " ♜ ";
    public static final String BLACK_PAWN = " ♟ ";
    public static final String EMPTY = " \u2003 ";
    @Override
    public String toString() {
        //switch statement that will return the unicode character for each type
        switch(this.type) {
            case KING: {
                if (this.pieceColor == ChessGame.TeamColor.WHITE)
                    return SET_TEXT_COLOR_BLUE + BLACK_KING + SET_TEXT_COLOR_WHITE;
                else
                    return SET_TEXT_COLOR_RED + BLACK_KING + SET_TEXT_COLOR_WHITE;
            }
            case QUEEN: {
                if (this.pieceColor == ChessGame.TeamColor.WHITE)
                    return SET_TEXT_COLOR_BLUE + BLACK_QUEEN+ SET_TEXT_COLOR_WHITE;
                else
                    return SET_TEXT_COLOR_RED + BLACK_QUEEN+ SET_TEXT_COLOR_WHITE;
            }
            case KNIGHT: {
                if (this.pieceColor == ChessGame.TeamColor.WHITE)
                    return SET_TEXT_COLOR_BLUE + BLACK_KNIGHT+ SET_TEXT_COLOR_WHITE;
                else
                    return SET_TEXT_COLOR_RED + BLACK_KNIGHT+ SET_TEXT_COLOR_WHITE;
            }
            case BISHOP: {
                if (this.pieceColor == ChessGame.TeamColor.WHITE)
                    return SET_TEXT_COLOR_BLUE + BLACK_BISHOP+ SET_TEXT_COLOR_WHITE;
                else
                    return SET_TEXT_COLOR_RED + BLACK_BISHOP+ SET_TEXT_COLOR_WHITE;
            }
            case ROOK: {
                if (this.pieceColor == ChessGame.TeamColor.WHITE)
                    return SET_TEXT_COLOR_BLUE + BLACK_ROOK+ SET_TEXT_COLOR_WHITE;
                else
                    return SET_TEXT_COLOR_RED + BLACK_ROOK+ SET_TEXT_COLOR_WHITE;
            }
            case PAWN: {
                if (this.pieceColor == ChessGame.TeamColor.WHITE)
                    return SET_TEXT_COLOR_BLUE + BLACK_PAWN + SET_TEXT_COLOR_WHITE;
                else
                    return SET_TEXT_COLOR_RED + BLACK_PAWN+ SET_TEXT_COLOR_WHITE;
            }
            case null, default: {
                return EMPTY;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    //Private Instance Variables.
    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;
    //Constructor.
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
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

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

         // Generally -- we take a board and a position and then check the positions around to see whats going on.
         // Each piece has different dimensions we are going to check.
         // Maybe use a try to generalize the boundaries of the board.
         // Capture rules for each piece.
         //Can never move to a position that your own piece is in.

        return switch (this.type) {
            case KING -> {
                KingMovesCalculator kingMovesCalculator = new KingMovesCalculator(this.getTeamColor());
                yield kingMovesCalculator.pieceMoves(board, myPosition);
            }
            case QUEEN -> {
                QueenMovesCalculator queenMovesCalculator = new QueenMovesCalculator(this.getTeamColor());
                yield queenMovesCalculator.pieceMoves(board, myPosition);
            }
            case BISHOP -> {
                BishopMovesCalculator bishopMovesCalculator = new BishopMovesCalculator(this.getTeamColor());
                yield bishopMovesCalculator.pieceMoves(board, myPosition);
            }
            case KNIGHT -> {
                KnightMovesCalculator knightMovesCalculator = new KnightMovesCalculator(this.getTeamColor());
                yield knightMovesCalculator.pieceMoves(board, myPosition);
            }
            case ROOK -> {
                RookMovesCalculator rookMovesCalculator = new RookMovesCalculator(this.getTeamColor());
                yield rookMovesCalculator.pieceMoves(board, myPosition);
            }
            case PAWN -> {
                PawnMovesCalculator pawnMovesCalculator = new PawnMovesCalculator(this.getTeamColor());
                yield pawnMovesCalculator.pieceMoves(board, myPosition);
            }
        };
    }
}
