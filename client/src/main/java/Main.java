import chess.*;
import ui.PreloginUI;

public class Main {
    public static void main(String[] args) {
        var serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = "http://localhost: " + args[0];
        }
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client:" + piece);
        PreloginUI preloginUI = new PreloginUI(serverUrl);
        preloginUI.run();
    }
}