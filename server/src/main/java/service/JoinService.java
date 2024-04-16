package service;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.DatabaseAuthDAO;
import dataAccess.DatabaseGameDAO;
import model.AuthData;
import model.GameData;
import webSocketMessages.userCommands.JoinPlayerMessage;

public class JoinService {
    public AuthData join(String authString, Integer gameID) throws Exception {
        DatabaseAuthDAO authDao = new DatabaseAuthDAO();
        // If we get a join request, and the game does not exist in the database.
        DatabaseGameDAO gameDAO = new DatabaseGameDAO();
        String username = authDao.verifyAuth(authString).username();

        //how do we send an error message to a user if we don't have a good game ID?

        return authDao.verifyAuth(authString);
    }
}
