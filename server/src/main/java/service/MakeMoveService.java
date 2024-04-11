package service;

import chess.ChessGame;
import chess.ChessMove;
import dataAccess.DatabaseGameDAO;
import model.GameData;

public class MakeMoveService{
    public ChessGame makeMove(Integer gameID, String authToken, ChessMove newMove) throws Exception {
        DatabaseGameDAO gameDAO = new DatabaseGameDAO();
        // pull the current game from the database
        // make the move.
        // update the game in the database
        // return the updated game

        GameData game = gameDAO.getGame(gameID);
        game.game().makeMove(newMove);
        gameDAO.insertGame(game);
        return game.game();
    }
}
