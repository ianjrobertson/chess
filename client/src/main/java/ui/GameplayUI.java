package ui;

import WebSocketFacade.WebSocketFacade;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import ServerFacade.ServerFacade;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.userCommands.JoinObserverMessage;
import webSocketMessages.userCommands.JoinPlayerMessage;
import webSocketMessages.userCommands.LeaveMessage;
import webSocketMessages.userCommands.UserGameCommand;

import java.util.Locale;
import java.util.Scanner;

import static ui.PostloginUI.getFirstWord;

public class GameplayUI implements GameHandler {
    private String sessionAuthToken;
    private final String headerString  = "ABCDEFGH";
    private final String backwardsHeaderString = "HGFEDCBA";
    private ServerFacade serverFacade;
    private WebSocketFacade webSocketFacade;
    private boolean running;
    private ChessGame.TeamColor color;
    private Integer gameID;

    public GameplayUI(int gameID, String sessionAuthToken, ServerFacade serverFacade, ChessGame.TeamColor color) {
        this.gameID = gameID;
        this.sessionAuthToken = sessionAuthToken;
        this.serverFacade = serverFacade;
        this.webSocketFacade = new WebSocketFacade();
        this.color = color;

        System.out.println(EscapeSequences.ERASE_SCREEN);
        try {
            webSocketFacade.connect();
            if (color != null) {
                webSocketFacade.joinPlayer(new JoinPlayerMessage(UserGameCommand.CommandType.JOIN_PLAYER, sessionAuthToken, gameID, color));
            }
            else {
                webSocketFacade.joinObserver(new JoinObserverMessage(sessionAuthToken, UserGameCommand.CommandType.JOIN_OBSERVER, gameID));
            }
            ChessBoard board = serverFacade.getGame(gameID, sessionAuthToken).getBoard();
            this.printBlackBoard(board);
            System.out.println(EscapeSequences.RESET_BG_COLOR);
            this.printWhiteBoard(board);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (running) {
            String input = scanner.nextLine();
            String command = getFirstWord(input);
            command = command.toLowerCase(Locale.ROOT);
            switch(command) {
                case("help") -> this.help();
                case("redraw") -> this.redraw();
                case("leave") -> this.leaveGame();
                case("move") -> this.makeMove();
                case("resign") -> this.resign();
                case("highlight") -> this.highlight();
                case null, default -> this.unknownInput();
            }
        }
    }

    public void updateGame(LoadGameMessage message) {
        this.redraw(message.getGame().getBoard(), this.color);
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    private void unknownInput() {
        System.out.println("Unknown input, please try again");
    }

    private void help() {
        System.out.println("help - list options");
        System.out.println("redraw - redraw chess board");
        System.out.println("leave - leave the current game");
        System.out.println("move <ROW, COLUMN> -> <ROW, COLUMN>");
        System.out.println("resign - resign game, results in a loss");
        System.out.println("highlight <ROW, COLUMN> - highlight legal moves");
    }

    private void redraw(ChessBoard board, ChessGame.TeamColor color) {
        if (color == ChessGame.TeamColor.WHITE || color == null)
            this.printWhiteBoard(board);
        else
            this.printBlackBoard(board);
    }

    private void leaveGame() {
        try {
            LeaveMessage leaveMessage = new LeaveMessage(sessionAuthToken, this.gameID);
            webSocketFacade.leaveGame(leaveMessage);
        }
        catch(Exception e) {
            printMessage(e.getMessage());
        }
    }

    private void makeMove() {


    }

    private void resign() {

    }

    private void highlight() {

    }

    private void printWhiteBoard(ChessBoard board) {
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + "   "); // for the row number
        for (int i = 0; i < headerString.length(); i++) {
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SMALLEMPTY + headerString.charAt(i) + " ");
        }
        System.out.print("   ");
        System.out.println(EscapeSequences.RESET_BG_COLOR);
        //Print gameBoard
        for (int row = 1; row <= 8; row ++) {
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + " " + row + " ");
            for (int col = 1; col <= 8; col++) {
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                if (piece == null) {
                    if ((row + col) % 2 == 0)
                        System.out.print(EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.EMPTY);
                    else
                        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY + EscapeSequences.EMPTY);
                }
                else {
                    if ((row + col) % 2 == 0)
                        System.out.print(EscapeSequences.SET_BG_COLOR_WHITE + board.getPiece(new ChessPosition(row, col)));
                    else
                        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY + board.getPiece(new ChessPosition(row, col)));
                }
            }
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + " " + row + " ");
            System.out.println(EscapeSequences.RESET_BG_COLOR);
        }
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + "   "); // for the row number
        for (int i = 0; i < headerString.length(); i++) {
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SMALLEMPTY + headerString.charAt(i) + " ");
        }
        System.out.print("   ");
        System.out.println(EscapeSequences.RESET_BG_COLOR);
    }

    private void printBlackBoard(ChessBoard board) {
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + "   "); // for the row number
        for (int i = 0; i < backwardsHeaderString.length(); i++) {
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SMALLEMPTY + backwardsHeaderString.charAt(i) + " ");
        }
        System.out.print("   ");
        System.out.println(EscapeSequences.RESET_BG_COLOR);
        //Print gameBoard
        for (int row = 8; row >= 1; row--) {
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + " " + row + " ");
            for (int col = 8; col >= 1; col--) {
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                if (piece == null) {
                    if ((row + col) % 2 == 0)
                        System.out.print(EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.EMPTY);
                    else
                        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY + EscapeSequences.EMPTY);
                }
                else {
                    if ((row + col) % 2 == 0)
                        System.out.print(EscapeSequences.SET_BG_COLOR_WHITE + board.getPiece(new ChessPosition(row, col)));
                    else
                        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY + board.getPiece(new ChessPosition(row, col)));
                }
            }
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + " " + row + " ");
            System.out.println(EscapeSequences.RESET_BG_COLOR);
        }
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + "   "); // for the row number
        for (int i = 0; i < backwardsHeaderString.length(); i++) {
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SMALLEMPTY + backwardsHeaderString.charAt(i) + " ");
        }
        System.out.print("   ");
        System.out.println(EscapeSequences.RESET_BG_COLOR);
    }
}
