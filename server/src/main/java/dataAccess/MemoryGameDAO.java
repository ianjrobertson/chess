package dataAccess;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    private static HashMap<Integer, GameData> gameDataMap = new HashMap<Integer, GameData>();

    @Override
    public void clear() {
        gameDataMap.clear();
    }

    @Override
    public int createGame(GameData g) {
        gameDataMap.put(g.gameID(), g);
        return g.gameID();
    }


    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        if (!gameDataMap.containsKey(gameID)) {
            throw new DataAccessException("Unknown gameID:" + gameID);
        }
        return gameDataMap.get(gameID);
    }

    @Override
    public Collection<GameData> listGames() {
        return gameDataMap.values();
    }

    @Override
    public void updateGame() {

    }

    @Override
    public void joinGame() {

    }
}
