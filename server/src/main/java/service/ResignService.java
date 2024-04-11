package service;

import dataAccess.DatabaseAuthDAO;
import dataAccess.DatabaseGameDAO;
import model.GameData;

public class ResignService {
    public String resign(Integer gameID, String authToken) throws Exception{
        //So pull the current game from the database
        //set the isOver boolean to true
        // update the game
        // insert it back into the database
        DatabaseGameDAO gameDAO = new DatabaseGameDAO();
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();

        GameData game = gameDAO.getGame(gameID);
        game.game().setGameOver();
        gameDAO.insertGame(game);

        return authDAO.verifyAuth(authToken).username();
    }
}
