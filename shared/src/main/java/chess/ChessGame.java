package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private ChessGame.TeamColor teamTurn;
    public ChessGame() {
        this.board = new ChessBoard();
        this.teamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    private ChessGame.TeamColor getOppositeColor(ChessGame.TeamColor color) {
        if (color == TeamColor.WHITE)
            return TeamColor.BLACK;
        else
            return TeamColor.WHITE;
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        return this.board.getPiece(startPosition).pieceMoves(board,startPosition);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        // You can only make a move if it is your turn.
            if (isValid(move) && isTurn(move))
                this.board.movePiece(move);
            else
                throw new InvalidMoveException("Not a valid move");
    }

    private boolean isValid(ChessMove move) {
        Collection<ChessMove> moves = validMoves(move.getStartPosition());
        return moves.contains(move);
        // A king can't make a move that would allow it to be captured. hmmmmmmmmm So do we need to simulate the board with that move??2
    }

    private boolean isTurn(ChessMove move) {
        ChessGame.TeamColor color = this.getBoard().getPiece(move.getStartPosition()).getTeamColor();
        return color == this.getTeamTurn();
    }
    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // So given a teamColor, we need to return the position of that teams king.
        // It would make the most sense to have that be a function of a chessBoard. A chessBoard knows where its Kings are.
        // In check means that the current posistion of the king is a valid move for an enemy piece.
        // So we load the position of the king.color == teamColor
        // Then we iterate through the valid moves for all enemy pieces on the board.
        // We just need to check if the king position is contained in the validMoves of any piece on the board.

        ChessPosition kingPosition = this.board.getKingPosition(teamColor);
        Collection<ChessPosition> enemyTeamPositions = this.board.getTeamPositions(getOppositeColor(teamColor)); // list of enemy positions
        Collection<ChessMove> validMoves; //temp variable of validMoves given enemyPositions

        for (ChessPosition position : enemyTeamPositions) { //Iterate through enemy positions
            validMoves = validMoves(position); // assign temp variable to validMoves of enemyPosition;
            for (ChessMove enemyMove: validMoves)  { // Iterate through validMoves
                if (enemyMove.getEndPosition() == kingPosition) //If the endPosition of an enemyMove is the kingposition return true;
                    return true;
            }
        }

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        // So, I could simulate a chessBoard. If for every valid move a king makes, He is still in check.
        // A problem is My check function tests the current board.
        // I could make a new ChessBoard for every potential Position of the King.
        // Move the King to that potentialPosition.
        // If the King is not in check in that position, return false.
        // If I iterate through all the ChessBoards, and the King is in check for all of them.
        // The king is in checkmate.

        ChessPosition kingPosition = this.board.getKingPosition(teamColor);
        Collection<ChessMove> kingMoves = validMoves(kingPosition);
        Collection<ChessPosition> enemyTeamPositions = this.board.getTeamPositions(getOppositeColor(teamColor)); // list of enemy positions
        Collection<ChessMove> validMoves; //temp variable of validMoves given enemyPositions
        boolean killMove;

        for (ChessMove kingMove: kingMoves) { // Iterate through valid Moves a king can make.
            killMove = false;
            for (ChessPosition position: enemyTeamPositions) { //For all enemy pieces
                validMoves = this.validMoves(position); //for all enemy valid moves
                for (ChessMove enemyMove : validMoves) {
                    if (kingMove.getEndPosition() == enemyMove.getEndPosition()) { //If the kingMoves end position is the same as an enemyMove end position
                        killMove = true;
                        break;
                    }
                }
            }
            if (!killMove) //If we didn't find an enemy move that would place the king in check for the given kingMove.
                return false; // The king is not in CheckMate
        }
        return true; //If we iterated through all the possible kingMoves and found a killMove in each one, the king is in CheckMate
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        //Given a teamColor, we look at the positions of all their pieces
        // If The validMoves collection length is greater than 0, we return false.
        //If we make it through all the pieces, return true.
        Collection<ChessPosition> teamPositions = this.board.getTeamPositions(teamColor);
        for (ChessPosition position : teamPositions) {
            if (!this.validMoves(position).isEmpty()) //If the validMoves for that piece is not empty
                return false;
        }
        return true; //Iterated through all pieces, and none of the pieces had a valid move
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
}
