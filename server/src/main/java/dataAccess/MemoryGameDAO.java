package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    private static HashMap<Integer, GameData> gameDataMap = new HashMap<Integer, GameData>();
    private static Integer nextGameID = 1;

    @Override
    public void clear() {
        gameDataMap.clear();
    }

    @Override
    public void insertGame(GameData g) throws DataAccessException {
        if (g == null) {
            throw new DataAccessException("Error: bad request");
        }
        gameDataMap.put(g.gameID(), g);
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        GameData gameData = new GameData(nextGameID, null, null, gameName, new ChessGame());
        insertGame(gameData);
        return nextGameID++;
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
    public void joinGame(int gameID, String username, boolean isWhite) {
        // So we need to take the username and If they are a black player or white player
        //Then we are able to update the GameData object to reflect the players.

        if (isWhite) { //username recieved is white
            GameData updatedGameData = new GameData(gameID, username, gameDataMap.get(gameID).blackUsername(),
                    gameDataMap.get(gameID).gameName(), gameDataMap.get(gameID).game());
        }
        else { //username recieve is black
            GameData updatedGameData = new GameData(gameID, gameDataMap.get(gameID).whiteUsername(), username,
                    gameDataMap.get(gameID).gameName(), gameDataMap.get(gameID).game());
        }

        //TODO throw error if gameID is not found? if we just use the getGame() method, it will handle that
        //TODO thats the only major error case to probably worry about.

    }
}
