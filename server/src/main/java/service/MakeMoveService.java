package service;

import chess.ChessGame;
import chess.ChessMove;
import dataAccess.DatabaseAuthDAO;
import dataAccess.DatabaseGameDAO;
import model.GameData;

public class MakeMoveService{
    public GameData makeMove(Integer gameID, String authToken, ChessMove newMove) throws Exception {
        DatabaseGameDAO gameDAO = new DatabaseGameDAO();
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
        String username = authDAO.verifyAuth(authToken).username();
        // pull the current game from the database
        // make the move.
        // update the game in the database
        // return the updated game

        GameData game = gameDAO.getGame(gameID);
        if (username.equals(game.whiteUsername()) && game.game().getTeamTurn() == ChessGame.TeamColor.WHITE) {
            game.game().makeMove(newMove);
            gameDAO.insertGame(game);
        }
        else if (username.equals(game.blackUsername()) && game.game().getTeamTurn() == ChessGame.TeamColor.BLACK) {
            game.game().makeMove(newMove);
            gameDAO.insertGame(game);
        }
        else {
            throw new Exception(String.format("Error: not %s turn", username));
        }

        return game;
    }

}
