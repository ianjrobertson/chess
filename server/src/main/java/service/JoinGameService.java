package service;

import chess.ChessGame;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import service.ServiceRecords.JoinGameRequest;

public class JoinGameService {
    public void joinGame(String authToken, JoinGameRequest r) throws DataAccessException {
        //verify the authToken
        AuthDAO authDAO = new DatabaseAuthDAO();
        AuthData authData = authDAO.verifyAuth(authToken);
        if (authData == null) {
            throw new DataAccessException("Error: unauthorized"); //Could refactor to throw this in MemoryAuthDAO
        }
        //verify that the game Exists
        DatabaseGameDAO gameDAO = new DatabaseGameDAO();
        GameData gameData = gameDAO.getGame(r.gameID()); //Throws a DataAccesException if game doesn't exist

        //if a color is specified, add that to color to gameID gameData object
        if (r.playerColor() == ChessGame.TeamColor.WHITE) {
            gameDAO.joinGame(r.gameID(), authData.username(), ChessGame.TeamColor.WHITE);
        }
        else if (r.playerColor() == ChessGame.TeamColor.BLACK) {
            gameDAO.joinGame(r.gameID(), authData.username(), ChessGame.TeamColor.BLACK);
        }
        else {
            // Else, I don't know what it means to add the player as a spectator???
        }
    }
}
