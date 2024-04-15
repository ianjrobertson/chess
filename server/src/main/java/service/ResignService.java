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
        if (game.game().isGameOver()) { //if game is already over, we don't need to resign
            throw new Exception("Error: Game over");
        }
        // We don't want an observer to be able to resign a game.
        String username = authDAO.verifyAuth(authToken).username();
        if (game.blackUsername() == null && game.whiteUsername() == null || !username.equals(game.blackUsername()) && !username.equals(game.whiteUsername())) {
            throw new Exception("Error: Observer not allowed to resign");
        }
        game.game().setGameOver();
        gameDAO.insertGame(game);

        //if game is over, no more moves allowed we can handle in

        return authDAO.verifyAuth(authToken).username();
    }
}
