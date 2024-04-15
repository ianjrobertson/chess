package ui;

import WebSocketFacade.WebSocketFacade;
import chess.*;
import ServerFacade.ServerFacade;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.userCommands.*;

import java.util.*;

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
        this.webSocketFacade = new WebSocketFacade(this);
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
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void run() {
        running = true;
        Scanner scanner = new Scanner(System.in);
        while (running) {
            String input = scanner.nextLine();
            String command = getFirstWord(input);
            command = command.toLowerCase(Locale.ROOT);
            switch(command) {
                case("help") -> this.help();
                case("redraw") -> this.redraw();
                case("leave") -> this.leaveGame();
                case("move") -> this.makeMove(input);
                case("resign") -> this.resign();
                case("highlight") -> this.highlight(input);
                case null, default -> this.unknownInput();
            }
        }
    }

    private boolean isPlayer() {
        return this.color != null;
    }

    public void updateGame(LoadGameMessage message) {
        if (color == ChessGame.TeamColor.WHITE || color == null)
            this.printWhite(message.getGame().getBoard(), null);
        else
            this.printBlack(message.getGame().getBoard(), null);
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
        System.out.println("move <ROW COLUMN> to <ROW COLUMN> <PromotionPiece>");
        System.out.println("resign - resign game, results in a loss");
        System.out.println("highlight <ROW COLUMN> - highlight legal moves");
    }

    private void redraw() {
        try {
            ChessBoard board = serverFacade.getGame(gameID, sessionAuthToken).getBoard();
            if (color == ChessGame.TeamColor.WHITE || color == null)
                this.printWhite(board, null);
            else
                this.printBlack(board, null);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void leaveGame() {
        try {
            running = false;
            LeaveMessage leaveMessage = new LeaveMessage(sessionAuthToken, this.gameID, UserGameCommand.CommandType.LEAVE);
            webSocketFacade.leaveGame(leaveMessage);
        }
        catch(Exception e) {
            printMessage(e.getMessage());
        }
    }

    private void makeMove(String input) {
        //If the move is a promotion move, we need to ask what piece the player wants
        try {
            if (!isPlayer())
                throw new Exception("Error: not a player");
            ChessMove move = parseMove(input);
            webSocketFacade.makeMove(new MakeMoveMessage(UserGameCommand.CommandType.MAKE_MOVE, sessionAuthToken, gameID, move));
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private ChessMove parseMove(String input) {
        //format: move 1 2 to 3 5 queen
        String[] words = input.trim().split("\\s+");
        int startRow = Integer.parseInt(words[1]);
        int startCol = Integer.parseInt(words[2]);
        int endRow = Integer.parseInt(words[4]);
        int endCol = Integer.parseInt(words[5]);
        ChessPiece.PieceType promotionPiece = getPromotionPiece(words);

        return new ChessMove(new ChessPosition(startRow, startCol), new ChessPosition(endRow, endCol), promotionPiece);
        //how to handle promotion pieces
        //I bet there is some logic I could borrow from my shared folder to handle this
    }

    private static ChessPiece.PieceType getPromotionPiece(String[] words) {
        ChessPiece.PieceType promotionPiece = null;

        if (words.length > 6) {
            String promotion = words[6];
            switch(promotion) {
                case("king") -> promotionPiece = ChessPiece.PieceType.KING;
                case("queen") -> promotionPiece = ChessPiece.PieceType.QUEEN;
                case("rook") -> promotionPiece = ChessPiece.PieceType.ROOK;
                case("bishop") -> promotionPiece = ChessPiece.PieceType.BISHOP;
                case("knight") -> promotionPiece = ChessPiece.PieceType.KNIGHT;
            }
        }
        return promotionPiece;
    }

    private void resign() {
        try {
            webSocketFacade.resignGame(new ResignMessage(UserGameCommand.CommandType.RESIGN, sessionAuthToken, gameID));
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void highlight(String input) {
        try {
            String[] words = input.trim().split("\\s+");
            int startRow = Integer.parseInt(words[1]);
            int startCol = Integer.parseInt(words[2]);
            ChessPosition piecePosition = new ChessPosition(startRow, startCol);
            ChessGame game = serverFacade.getGame(gameID, sessionAuthToken);
            Collection<ChessMove> pieceMoves = game.validMoves(piecePosition);
            Collection<ChessPosition> endPosition = new ArrayList<>();
            for (ChessMove move : pieceMoves) {
                endPosition.add(move.getEndPosition());
            }

            if (color == ChessGame.TeamColor.WHITE) {
                this.printWhite(game.getBoard(), endPosition);
            }
            else if (color == ChessGame.TeamColor.BLACK) {
                this.printBlack(game.getBoard(), endPosition);
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void printBlack(ChessBoard board, Collection<ChessPosition> highlightMoves) {
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + "   "); // for the row number
        for (int i = 8; i >= 1; i--) {
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SMALLEMPTY + i + " ");
        }
        System.out.print("   ");
        System.out.println(EscapeSequences.RESET_BG_COLOR);
        for (int row = 1; row <= 8; row ++) {
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + " " + row + " ");
            for (int col = 8; col > 0; col--) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);
                if (piece == null) {
                    if ((row + col) % 2 != 0) {
                        if (highlightMoves != null && highlightMoves.contains(position)) {
                            System.out.print(EscapeSequences.SET_BG_COLOR_GREEN + EscapeSequences.EMPTY);
                        }
                        else
                            System.out.print(EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.EMPTY);
                    }
                    else {
                        if (highlightMoves != null && highlightMoves.contains(position)) {
                            System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN + EscapeSequences.EMPTY);
                        }
                        else
                            System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY + EscapeSequences.EMPTY);
                    }
                }
                else {
                    if ((row + col) % 2 != 0) {
                        if (highlightMoves != null && highlightMoves.contains(position)) {
                            System.out.print(EscapeSequences.SET_BG_COLOR_GREEN + board.getPiece(new ChessPosition(row, col)));
                        }
                        else
                            System.out.print(EscapeSequences.SET_BG_COLOR_WHITE + board.getPiece(new ChessPosition(row, col)));
                    }
                    else {
                        if (highlightMoves != null && highlightMoves.contains(position)) {
                            System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN + board.getPiece(new ChessPosition(row, col)));

                        }
                        else
                            System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY + board.getPiece(new ChessPosition(row, col)));
                    }
                }
            }
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + " " + row + " ");
            System.out.println(EscapeSequences.RESET_BG_COLOR);
        }
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + "   "); // for the row number
        for (int i = 8; i >= 1; i--) {
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SMALLEMPTY + i + " ");
        }
        System.out.print("   ");
        System.out.println(EscapeSequences.RESET_BG_COLOR);
    }

    private void printWhite(ChessBoard board, Collection<ChessPosition> highlightMoves) {
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + "   "); // for the row number
        for (int i = 1; i <= 8 ; i++) {
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SMALLEMPTY + i + " ");
        }
        System.out.print("   ");
        System.out.println(EscapeSequences.RESET_BG_COLOR);
        //Print gameBoard
        for (int row = 8; row >= 1; row--) {
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + " " + row + " ");
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);
                if (piece == null) {
                    if ((row + col) % 2 != 0) {
                        if (highlightMoves != null && highlightMoves.contains(position)) {
                            System.out.print(EscapeSequences.SET_BG_COLOR_GREEN + EscapeSequences.EMPTY);
                        }
                        else
                            System.out.print(EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.EMPTY);
                    }
                    else {
                        if (highlightMoves != null && highlightMoves.contains(position)) {
                            System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN + EscapeSequences.EMPTY);
                        }
                        else
                            System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY + EscapeSequences.EMPTY);
                    }
                }
                else {
                    if ((row + col) % 2 != 0) {
                        if (highlightMoves != null && highlightMoves.contains(position)) {
                            System.out.print(EscapeSequences.SET_BG_COLOR_GREEN + board.getPiece(new ChessPosition(row, col)));
                        }
                        else
                            System.out.print(EscapeSequences.SET_BG_COLOR_WHITE + board.getPiece(new ChessPosition(row, col)));
                    }
                    else {
                        if (highlightMoves != null && highlightMoves.contains(position)) {
                            System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN + board.getPiece(new ChessPosition(row, col)));

                        }
                        else
                            System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY + board.getPiece(new ChessPosition(row, col)));
                    }
                }
            }
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + " " + row + " ");
            System.out.println(EscapeSequences.RESET_BG_COLOR);
        }
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + "   "); // for the row number
        for (int i = 1; i <= 8; i++) {
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SMALLEMPTY + i + " ");
        }
        System.out.print("   ");
        System.out.println(EscapeSequences.RESET_BG_COLOR);
    }
}
