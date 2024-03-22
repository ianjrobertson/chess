package model;
import chess.ChessGame;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {

    public String listGame() {
        return "Name: " + gameName + ", White: " + whiteUsername + ", Black: " + blackUsername;
    }
}


