package dataAccess;

import chess.ChessGame;
import model.GameData;

import javax.xml.crypto.Data;
import java.util.Collection;

public interface GameDAO {
    void clear() throws DataAccessException;
    void insertGame(GameData g) throws DataAccessException;
    int createGame(String gameName) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    Collection<GameData> listGames() throws DataAccessException;
    void joinGame(int gameID, String username, ChessGame.TeamColor teamColor) throws DataAccessException;
}
