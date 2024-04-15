package service;

import dataAccess.DatabaseAuthDAO;
import dataAccess.DatabaseGameDAO;
import dataAccess.DatabaseUserDAO;
import model.GameData;
import model.UserData;

public class LeaveGameService {
    public String leaveGame(Integer gameID, String authToken) throws Exception {
        DatabaseGameDAO gameDAO = new DatabaseGameDAO();
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
        //okay so we need to get the username using the AuthDao
        String username = authDAO.verifyAuth(authToken).username();

        //Then we need to construct a new GameData object that copies everything but sets the corresponding username to null
        GameData game = gameDAO.getGame(gameID);
        String whiteUsername = game.whiteUsername();
        String blackUsername = game.blackUsername();

        if (username.equals(whiteUsername)) {
            whiteUsername = null;
        }
        else if (username.equals(blackUsername)) {
            blackUsername = null;
        }
        else {
            //throw new Exception("Error: Invalid username");
        }
        GameData newGame = new GameData(game.gameID(), whiteUsername, blackUsername, game.gameName(), game.game());

        gameDAO.insertGame(newGame);
        //then we can call the insertGame method on the GameDao and update everything that we need to
        return username;
    }
}
