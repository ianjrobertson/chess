package ui;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;
import server.Server;
import server.ServerFacade;

public class GameplayUI {

    private String sessionAuthToken;
    private final String headerString  = "ABCDEFGH";
    private ServerFacade serverFacade;

    //we need to draw the game from the perspective of both people.
    //The only information we get is the json file? We can construct a chess board.
    //We could construct a to_String method within chessBoard?
    // we need to print both perspectives.
    // So we could define our two_String method with a color flag and then that would return the correct perspective.
    //I am not sure if that would work actually, because I think that the unicode colors only work for console output. we can do simply text for now and then modify it more later

    public GameplayUI(int gameID, String sessionAuthToken, ServerFacade serverFacade) {
        this.sessionAuthToken = sessionAuthToken;
        this.serverFacade = serverFacade;
        // do an api call to retrieve the game JSON and the construct a chessgame and then a chessboard
        // Call the private print function for both perspectives
        //Later we can make a repl look with the websocket and wait for calls to keep printing the new boards we get.

        System.out.println(EscapeSequences.ERASE_SCREEN);
        try {
            this.printWhiteBoard(serverFacade.getGame(gameID, sessionAuthToken).getBoard());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void printWhiteBoard(ChessBoard board) {
        //So it look like I can just print it out normally, then If I want to add cool colors I can go ahead and do that
        //Print header
        System.out.print(" "); // for the row number
        for (int i = 0; i < headerString.length(); i++) {
            System.out.print(EscapeSequences.EMPTY + headerString.charAt(i));
        }
        System.out.println();
        //Print gameBoard
        for (int row = 1; row <= 8; row ++) {
            System.out.print(row);
            for (int col = 1; col <= 8; col++) {
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                if (piece == null) {
                    System.out.print(EscapeSequences.EMPTY);
                }
                else {
                    System.out.print(board.getPiece(new ChessPosition(row, col)));
                }
            }
            System.out.println();
        }
    }

    private void printBlackBoard(ChessBoard board) {

    }


}
