package dataAccess;

import chess.ChessGame;
import model.GameData;

import javax.xml.crypto.Data;
import java.util.Collection;

public interface GameDAO {
    void clear();
    int createGame(GameData g);
    GameData getGame(int gameID) throws DataAccessException;
    Collection<GameData> listGames();
    void updateGame();
    void joinGame();
}
