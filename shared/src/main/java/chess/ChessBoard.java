package chess;

import javax.lang.model.type.NullType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Collection;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private final ChessPiece[][] board;
    private final ChessGame.TeamColor teamColor;
    private final ChessGame.TeamColor otherTeamColor;

    private ChessPosition whiteKingPosition;
    private ChessPosition blackKingPosition;

    public ChessBoard() {
        this.board = new ChessPiece[8][8];
        this.teamColor = ChessGame.TeamColor.WHITE;
        this.otherTeamColor = ChessGame.TeamColor.BLACK;
        this.whiteKingPosition = null;
        this.blackKingPosition = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(board, that.board) && teamColor == that.teamColor && otherTeamColor == that.otherTeamColor;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(teamColor, otherTeamColor);
        result = 31 * result + Arrays.deepHashCode(board);
        return result;
    }

    public ChessGame.TeamColor getTeamColor() {
        return this.teamColor;
    }

    public ChessGame.TeamColor getOtherTeamColor() {
        return this.otherTeamColor;
    }

    public ChessPosition getKingPosition(ChessGame.TeamColor teamColor) {

        if (teamColor == ChessGame.TeamColor.WHITE)
            return this.whiteKingPosition;
        else
            return this.blackKingPosition;
    }

    public Collection<ChessPosition> getTeamPositions(ChessGame.TeamColor teamColor) {
        //Traverse the double array and return an arrayList of positions
        Collection<ChessPosition> teamPositions = new ArrayList<>();
        ChessPosition potentialPosition;
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                potentialPosition = new ChessPosition(row, col);
                if (this.containsPiece(potentialPosition) && this.getPiece(potentialPosition).getTeamColor() == teamColor) { // If it contains a team piece
                    teamPositions.add(potentialPosition);
                }
            }
        }
        return teamPositions;
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        //Include error handling
        // -- out of bounds handling could also be handled when constructing position.

        // Need handling if there is already a piece there or if the position is out of bounds.
        if (piece.getPieceType() == ChessPiece.PieceType.KING)
            setKingPosition(piece.getTeamColor(), position);
        this.board[position.getRowIndex()][position.getColumnIndex()] = piece;
    }

    private void setKingPosition(ChessGame.TeamColor teamColor, ChessPosition newPosition) {
        if (teamColor == ChessGame.TeamColor.WHITE)
            this.whiteKingPosition = newPosition;
        else
            this.blackKingPosition = newPosition;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        //What error handling is needed?
        //Make sure the position is on the board?
        //Does color or type matter?

        //Need NULL exception
        return this.board[position.getRowIndex()][position.getColumnIndex()];
    }

    public void movePiece(ChessMove move) {
        if (this.getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.KING) //If the piece being moved is a king
            setKingPosition(this.getPiece(move.getStartPosition()).getTeamColor(), move.getEndPosition()); //update the position variable

        if (move.getPromotionPiece() == null) { //If normal move
            ChessPiece tempPiece = getPiece(move.getStartPosition()); //do we need deep copy???
            this.addPiece(move.getEndPosition(), tempPiece);
            this.addPiece(move.getStartPosition(), null);
        } else { //If there is a promotion piece.
            ChessGame.TeamColor teamColor = getPiece(move.getStartPosition()).getTeamColor();
            this.addPiece(move.getEndPosition(), new ChessPiece(teamColor, move.getPromotionPiece()));
            this.addPiece(move.getStartPosition(), null);

        }
    }

    public boolean containsPiece(ChessPosition position) {
        return getPiece(position) != null;
    }

    public boolean containsEnemyPiece(ChessPosition position, ChessGame.TeamColor color) {
        if (containsPiece(position)) {
            return (this.getPiece(position).getTeamColor() != color);
        }
        return false;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        //Start by looping through row 2 and row 7 to set pawns.
        for (int col = 1; col <= 8; col++) {
            //Top Row
            ChessPosition bottom = new ChessPosition(7, col);
            ChessPiece bottomPawn = new ChessPiece(otherTeamColor, ChessPiece.PieceType.PAWN); //Whatever the other color is
            this.addPiece(bottom, bottomPawn);

            //Bottom row
            ChessPosition top = new ChessPosition(2, col);
            ChessPiece topPawn = new ChessPiece(teamColor, ChessPiece.PieceType.PAWN); // Need to decide how we choose colors
            this.addPiece(top, topPawn);
        }
        //Set Rooks
        ChessPosition topLeft = new ChessPosition(8, 1);
        ChessPosition topRight = new ChessPosition(8, 8);
        ChessPiece topLeftRook = new ChessPiece(otherTeamColor, ChessPiece.PieceType.ROOK);
        ChessPiece topRightRook = new ChessPiece(otherTeamColor, ChessPiece.PieceType.ROOK);
        this.addPiece(topLeft, topLeftRook);
        this.addPiece(topRight, topRightRook);

        ChessPosition bottomLeft = new ChessPosition(1, 1);
        ChessPosition bottomRight = new ChessPosition(1, 8);
        ChessPiece bottomLeftRook = new ChessPiece(teamColor, ChessPiece.PieceType.ROOK);
        ChessPiece bottomRightRook = new ChessPiece(teamColor, ChessPiece.PieceType.ROOK);
        this.addPiece(bottomLeft, bottomLeftRook);
        this.addPiece(bottomRight, bottomRightRook);

        //Set Knights
        ChessPosition topLeftK = new ChessPosition(8, 2);
        ChessPosition topRightK = new ChessPosition(8, 7);
        ChessPiece topLeftKnight = new ChessPiece(otherTeamColor, ChessPiece.PieceType.KNIGHT);
        ChessPiece topRightKnight = new ChessPiece(otherTeamColor, ChessPiece.PieceType.KNIGHT);
        this.addPiece(topLeftK, topLeftKnight);
        this.addPiece(topRightK, topRightKnight);

        ChessPosition bottomLeftK = new ChessPosition(1, 2);
        ChessPosition bottomRightK = new ChessPosition(1, 7);
        ChessPiece bottomLeftKnight = new ChessPiece(teamColor, ChessPiece.PieceType.KNIGHT);
        ChessPiece bottomRightKnight = new ChessPiece(teamColor, ChessPiece.PieceType.KNIGHT);
        this.addPiece(bottomLeftK, bottomLeftKnight);
        this.addPiece(bottomRightK, bottomRightKnight);

        //Set Bishops
        ChessPosition topLeftB = new ChessPosition(8, 3);
        ChessPosition topRightB = new ChessPosition(8, 6);
        ChessPiece topLeftBishop = new ChessPiece(otherTeamColor, ChessPiece.PieceType.BISHOP);
        ChessPiece topRightBishop = new ChessPiece(otherTeamColor, ChessPiece.PieceType.BISHOP);
        this.addPiece(topLeftB, topLeftBishop);
        this.addPiece(topRightB, topRightBishop);

        ChessPosition bottomLeftB = new ChessPosition(1, 3);
        ChessPosition bottomRightB = new ChessPosition(1, 6);
        ChessPiece bottomLeftBishop = new ChessPiece(teamColor, ChessPiece.PieceType.BISHOP);
        ChessPiece bottomRightBishop = new ChessPiece(teamColor, ChessPiece.PieceType.BISHOP);
        this.addPiece(bottomLeftB, bottomLeftBishop);
        this.addPiece(bottomRightB, bottomRightBishop);

        //Set Queens
        ChessPosition topQueenPos = new ChessPosition(8, 4);
        ChessPosition bottomQueenPos = new ChessPosition(1, 4);
        ChessPiece topQueen = new ChessPiece(otherTeamColor, ChessPiece.PieceType.QUEEN);
        ChessPiece bottomQueen = new ChessPiece(teamColor, ChessPiece.PieceType.QUEEN);
        this.addPiece(topQueenPos, topQueen);
        this.addPiece(bottomQueenPos, bottomQueen);

        //Set Kings
        ChessPosition topKingPos = new ChessPosition(8, 5);
        ChessPosition bottomKingPos = new ChessPosition(1, 5);
        ChessPiece topKing = new ChessPiece(otherTeamColor, ChessPiece.PieceType.KING);
        ChessPiece bottomKing = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
        this.addPiece(topKingPos, topKing);
        this.addPiece(bottomKingPos, bottomKing);
    }
}
