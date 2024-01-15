package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    //Need to figure out how we choose a color for teams!
    private ChessPiece [][] board;
    private final ChessGame.TeamColor teamColor;
    private final ChessGame.TeamColor otherTeamColor;
    public ChessBoard() {
        this.board = new ChessPiece[8][8];
        this.teamColor = teamColor;
        if (this.teamColor == ChessGame.TeamColor.WHITE)
            this.otherTeamColor = ChessGame.TeamColor.BLACK;
        else
            this.otherTeamColor = ChessGame.TeamColor.WHITE;

        resetBoard();
        //Maybe call resetBoard() here to make it the default board.
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
        this.board[position.getRow()][position.getColumn()] = piece;
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
        return this.board[position.getRow()][position.getColumn()];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        //Might need to double check the indexing of the pieces... Looking up or looking down.
        //Start by looping through row 1 and row 6 to set pawns.
        for (int col = 0; col < 8; col++)
        {
            //Top Row
            ChessPosition top = new ChessPosition(1, col);
            ChessPiece topPawn = new ChessPiece(teamColor, ChessPiece.PieceType.PAWN); // Need to decide how we choose colors
            this.addPiece(top, topPawn);

            //Bottom Row
            ChessPosition bottom = new ChessPosition(6, col);
            ChessPiece bottomPawn = new ChessPiece(otherTeamColor, ChessPiece.PieceType.PAWN); //Whatever the other color is
            this.addPiece(bottom, bottomPawn);
        }
        //Set Rooks
        ChessPosition topLeft = new ChessPosition(0,0);
        ChessPosition topRight = new ChessPosition(0,7);
        ChessPiece topLeftRook = new ChessPiece(teamColor, ChessPiece.PieceType.ROOK);
        ChessPiece topRightRook = new ChessPiece(teamColor, ChessPiece.PieceType.ROOK);
        this.addPiece(topLeft, topLeftRook);
        this.addPiece(topRight, topRightRook);

        ChessPosition bottomLeft = new ChessPosition(7,0);
        ChessPosition bottomRight = new ChessPosition(7,7);
        ChessPiece bottomLeftRook = new ChessPiece(otherTeamColor, ChessPiece.PieceType.ROOK);
        ChessPiece bottomRightRook = new ChessPiece(otherTeamColor, ChessPiece.PieceType.ROOK);
        this.addPiece(bottomLeft, bottomLeftRook);
        this.addPiece(bottomRight, bottomRightRook);

        //Set Knights
        ChessPosition topLeftK = new ChessPosition(0,1);
        ChessPosition topRightK = new ChessPosition(0,6);
        ChessPiece topLeftKnight = new ChessPiece(teamColor, ChessPiece.PieceType.KNIGHT);
        ChessPiece topRightKnight = new ChessPiece(teamColor, ChessPiece.PieceType.KNIGHT);
        this.addPiece(topLeftK, topLeftKnight);
        this.addPiece(topRightK, topRightKnight);

        ChessPosition bottomLeftK = new ChessPosition(7,1);
        ChessPosition bottomRightK = new ChessPosition(7,6);
        ChessPiece bottomLeftKnight = new ChessPiece(otherTeamColor, ChessPiece.PieceType.KNIGHT);
        ChessPiece bottomRightKnight = new ChessPiece(otherTeamColor, ChessPiece.PieceType.KNIGHT);
        this.addPiece(bottomLeftK, bottomLeftKnight);
        this.addPiece(bottomRightK, bottomRightKnight);

        //Set Bishops
        ChessPosition topLeftB = new ChessPosition(0,2);
        ChessPosition topRightB = new ChessPosition(0,5);
        ChessPiece topLeftBishop = new ChessPiece(teamColor, ChessPiece.PieceType.BISHOP);
        ChessPiece topRightBishop = new ChessPiece(teamColor, ChessPiece.PieceType.BISHOP);
        this.addPiece(topLeftB, topLeftBishop);
        this.addPiece(topRightB, topRightBishop);

        ChessPosition bottomLeftB = new ChessPosition(7,2);
        ChessPosition bottomRightB = new ChessPosition(7,5);
        ChessPiece bottomLeftBishop = new ChessPiece(otherTeamColor, ChessPiece.PieceType.BISHOP);
        ChessPiece bottomRightBishop = new ChessPiece(otherTeamColor, ChessPiece.PieceType.BISHOP);
        this.addPiece(bottomLeftB, bottomLeftBishop);
        this.addPiece(bottomRightB, bottomRightBishop);

        //Set Queens
        ChessPosition topQueenPos = new ChessPosition(0,3);
        ChessPosition bottomQueenPos = new ChessPosition(7,3);
        ChessPiece topQueen = new ChessPiece(teamColor, ChessPiece.PieceType.QUEEN);
        ChessPiece bottomQueen = new ChessPiece(otherTeamColor, ChessPiece.PieceType.QUEEN);
        this.addPiece(topQueenPos, topQueen);
        this.addPiece(bottomQueenPos, bottomQueen);

        //Set Kings
        ChessPosition topKingPos = new ChessPosition(0,4);
        ChessPosition bottomKingPos = new ChessPosition(7,4);
        ChessPiece topKing = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
        ChessPiece bottomKing = new ChessPiece(otherTeamColor, ChessPiece.PieceType.KING);
        this.addPiece(topKingPos, topKing);
        this.addPiece(bottomKingPos, bottomKing);
    }
}
