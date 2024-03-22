import chess.*;
import server.Server;
import ui.PreloginUI;

public class Main {
    public static void main(String[] args) {
        var serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        Server server = new Server();
        server.run(8080);
        System.out.println("♕ 240 Chess Client:" + piece);
        PreloginUI preloginUI = new PreloginUI(serverUrl);
        preloginUI.run();

        server.stop();
    }
}