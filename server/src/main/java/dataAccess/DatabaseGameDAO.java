package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public class DatabaseGameDAO implements GameDAO{

    public void clear() {

    }

    @Override
    public void insertGame(GameData g) throws DataAccessException {

    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        return 0;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public Collection<GameData> listGames() {
        return null;
    }

    @Override
    public void joinGame(int gameID, String username, ChessGame.TeamColor teamColor) throws DataAccessException {

    }
}
