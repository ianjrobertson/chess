package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    void clear();
    GameData createGame(String gameName, String authToken);
    GameData getGame(String gameName);
    GameData getGame(int gameID);
    Collection<GameData> listGames();
    void updateGame();
    void joinGame();
}
