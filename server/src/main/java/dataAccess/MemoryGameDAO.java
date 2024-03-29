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
            throw new DataAccessException("Error: bad request");
        }
        return gameDataMap.get(gameID);
    }

    @Override
    public Collection<GameData> listGames() {
        return gameDataMap.values();
    }

    @Override
    public void joinGame(int gameID, String username, ChessGame.TeamColor playerColor) throws DataAccessException {
        // So we need to take the username and If they are a black player or white player
        //Then we are able to update the GameData object to reflect the players.

        if (playerColor == ChessGame.TeamColor.WHITE) { //username recieved is white
            if (this.getGame(gameID).whiteUsername() != null) {
                throw new DataAccessException("Error: already taken");
            }
            GameData updatedGameData = new GameData(gameID, username, this.getGame(gameID).blackUsername(),
                    this.getGame(gameID).gameName(), this.getGame(gameID).game());
            this.insertGame(updatedGameData);

        }
        else { //username recieve is black
            if (this.getGame(gameID).blackUsername() != null) {
                throw new DataAccessException("Error: already taken");
            }
            GameData updatedGameData = new GameData(gameID, this.getGame(gameID).whiteUsername(), username,
                    this.getGame(gameID).gameName(), this.getGame(gameID).game());
            this.insertGame(updatedGameData);
        }

    }
}
