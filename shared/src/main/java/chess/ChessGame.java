package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

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
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
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
        TeamColor teamColor = this.board.getPiece(startPosition).getTeamColor();
        if (this.board.getPiece(startPosition) == null)
            return null;
        Collection<ChessMove> moves = this.board.getPiece(startPosition).pieceMoves(board,startPosition);
        Iterator<ChessMove> iterator = moves.iterator();

        while (iterator.hasNext()) {
            boolean kingInDanger = false;
            ChessMove move = iterator.next(); //update the move
            ChessBoard copyBoard = new ChessBoard(this.board); //Deep Copy of ChessBoard
            this.board.movePiece(move); //make the move

            //See if the King is in Check
            ChessPosition kingPosition = this.board.getKingPosition(teamColor);
            Collection<ChessPosition> enemyTeamPositions = this.board.getTeamPositions(getOppositeColor(teamColor)); // list of enemy positions
            Collection<ChessMove> validMoves; //temp variable of validMoves given enemyPositions

            for (ChessPosition position : enemyTeamPositions) { //Iterate through enemy positions
                validMoves = this.board.getPiece(position).pieceMoves(this.board, position); // assign temp variable to validMoves of enemyPosition;
                for (ChessMove enemyMove: validMoves)  { // Iterate through validMoves
                    if (enemyMove.getEndPosition().equals(kingPosition)) //If the endPosition of an enemyMove is the kingposition return true;
                        kingInDanger = true;
                }
            }

            if (kingInDanger) {
                iterator.remove();
            }

            setBoard(copyBoard); //sets the chessBoard back to the original board.
        }

        return moves;
    }

    /**
     * private Collection<ChessMove> removeInvalidMoves(Collection<ChessMove> moves) {
     *         Iterator<ChessMove> iterator = moves.iterator();
     *         while(iterator.hasNext()) {
     *             ChessMove potentialMove = iterator.next();
     *             if (leavesKingInDanger(potentialMove, getTeamTurn())) {
     *                 iterator.remove();
     *             }
     *         }
     *         return moves;
     *     }
     */

    private boolean isValid(ChessMove move) {
        Collection<ChessMove> moves = validMoves(move.getStartPosition());
        return moves.contains(move) && !leavesKingInDanger(move, this.getTeamTurn());
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        // You can only make a move if it is your turn.
            if (isValid(move) && isTurn(move)) {
                this.board.movePiece(move);
                setTeamTurn(getOppositeColor(getTeamTurn()));
            }
            else
                throw new InvalidMoveException("Not a valid move");
    }

    private boolean leavesKingInDanger(ChessMove move, TeamColor color) {
        ChessBoard copyBoard = new ChessBoard(this.board); //Deep Copy of ChessBoard
        this.board.movePiece(move);
        boolean stillInCheck = isInCheck(color);
        setBoard(copyBoard); //sets the chessBoard back to the original board.

        return stillInCheck;
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
        ChessPosition kingPosition = this.board.getKingPosition(teamColor);
        Collection<ChessPosition> enemyTeamPositions = this.board.getTeamPositions(getOppositeColor(teamColor)); // list of enemy positions
        Collection<ChessMove> validMoves; //temp variable of validMoves given enemyPositions

        for (ChessPosition position : enemyTeamPositions) { //Iterate through enemy positions
            validMoves = validMoves(position); // assign temp variable to validMoves of enemyPosition;
            for (ChessMove enemyMove: validMoves)  { // Iterate through validMoves
                if (enemyMove.getEndPosition().equals(kingPosition)) //If the endPosition of an enemyMove is the kingposition return true;
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
        Collection<ChessPosition> teamPositions = this.board.getTeamPositions(teamColor); // All team Positions
        Collection<ChessMove> validMoves;

        for (ChessPosition teamPosition: teamPositions) { //For all team positions
            validMoves = this.validMoves(teamPosition); //Generate a list of their valid moves
            for (ChessMove validMove: validMoves) { //For each validMove
                if (!leavesKingInDanger(validMove, teamColor)) { // Simulate the move and see if it leaves the king in Check
                    return false; //If the move takes the king out of check, Not in checkMate
                }
            }
        }
        return true; //If each move left the king in Check, in CheckMate.
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        //so... if the team is not in check and it is their turn.
        // but for every validMove, they are in check.
        // We have a Stalemate.
        if (this.getTeamTurn() == teamColor && !isInCheck(teamColor)) { // If it is their turn and they are not in check
            Collection<ChessPosition> teamPositions = this.board.getTeamPositions(teamColor);
            Collection<ChessMove> validMoves;
            for (ChessPosition position : teamPositions) {
                validMoves = this.validMoves(position);
                for (ChessMove move: validMoves) {
                    if (!leavesKingInDanger(move, teamColor))
                        return false; // If the move doesn't result in a check. not in stalemate.
                }
            }
            return true; //If there are no valid moves, or every valid move results in a check.
        }
        return false; //If its not the teams turn or the team is in check.
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
