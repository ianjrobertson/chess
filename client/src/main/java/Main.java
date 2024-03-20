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
        System.out.println("♕ 240 Chess Client: " + piece);
        Server server = new Server();
        server.run(8080);
        PreloginUI preloginUI = new PreloginUI(serverUrl);
        preloginUI.run();
        //So in our main method. Our preLoginUI should take the web url as a parameter. Then our server facade should use that to make
        //The http requests.

        //How when you hit quit, send a message to main to kill the server.
    }
}