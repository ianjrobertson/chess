package dataAccess;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{

    @Override
    public void clear() {

    }

    @Override
    public GameData createGame(String gameName, String authToken) {

        return null;
    }

    @Override
    public GameData getGame(String gameName) {
        return null;
    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public Collection<GameData> listGames() {
        return null;
    }

    @Override
    public void updateGame() {

    }

    @Override
    public void joinGame() {

    }
}
