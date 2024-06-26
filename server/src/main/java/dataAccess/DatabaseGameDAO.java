package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import com.mysql.cj.jdbc.exceptions.MySQLQueryInterruptedException;
import com.mysql.cj.protocol.Resultset;
import model.GameData;

import javax.xml.crypto.Data;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

public class DatabaseGameDAO implements GameDAO{

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  game (
              gameID int NOT NULL AUTO_INCREMENT,
              whiteUsername varchar(256),
              blackUsername varchar(256),
              gameName varchar(256) NOT NULL,
              chessGame JSON NOT NULL,
              PRIMARY KEY (gameID)
            )
            """
    };

    public DatabaseGameDAO() throws DataAccessException {
        DatabaseManager.configureDatabase(createStatements);
    }

    public void clear() throws DataAccessException {
        try (var preparedStatement = DatabaseManager.getConnection().prepareStatement("DROP TABLE game")) {
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void insertGame(GameData g) throws DataAccessException {
        //do nothing for this implementation
        if (g == null || g.gameName() == null) {
            throw new DataAccessException("Error: bad request");
        }
        try (var preparedStatement = DatabaseManager.getConnection().prepareStatement("UPDATE game SET chessGame = ?, whiteUsername = ?, blackUsername = ? WHERE gameID = ?")) {
            preparedStatement.setString(1, new Gson().toJson(g.game()));
            preparedStatement.setString(2, g.whiteUsername());
            preparedStatement.setString(3, g.blackUsername());
            preparedStatement.setInt(4, g.gameID());
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        if (gameName == null) {
            throw new DataAccessException("Error: bad request");
        }
        try (var preparedStatement = DatabaseManager.getConnection().prepareStatement("INSERT INTO game (gameName, chessGame) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, gameName);
            preparedStatement.setString(2, new Gson().toJson(new ChessGame()));
            preparedStatement.executeUpdate();
            try (var resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1); //returns the next gameID that was auto Incrememnted!
                }
                else {
                    throw new DataAccessException("Something bad happened");
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (var preparedStatement = DatabaseManager.getConnection().prepareStatement("SELECT gameID, whiteUsername, blackUsername, gameName, chessGame FROM game WHERE gameID = ?")) {
            preparedStatement.setInt(1, gameID);
            preparedStatement.executeQuery();

            try (var resultSet = preparedStatement.getResultSet()) {
                if (resultSet.next()) {
                    var fetchedWhiteUsername = resultSet.getString("whiteUsername");
                    var fetchedBlackUsername = resultSet.getString("blackUsername");
                    var gameName = resultSet.getString("gameName");
                    var chessGame = new Gson().fromJson(resultSet.getString("chessGame"), ChessGame.class);

                    return new GameData(gameID, fetchedWhiteUsername, fetchedBlackUsername, gameName, chessGame);
                }
                else {
                    throw new DataAccessException("Error: bad request");
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException{
        var gameList = new ArrayList<GameData>();
        try (var preparedStatement = DatabaseManager.getConnection().prepareStatement("SELECT * FROM game")) {
            preparedStatement.executeQuery();
            try (var resultSet = preparedStatement.getResultSet()) {
                while(resultSet.next())
                {
                    gameList.add(readGame(resultSet));
                }
                return gameList;
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private GameData readGame(ResultSet rs) throws DataAccessException {
        try {
            int gameID = rs.getInt("gameID");
            String whiteUsername = rs.getString("whiteUsername");
            String blackUsername = rs.getString("blackUsername");
            String gameName = rs.getString("gameName");
            ChessGame chessGame = new Gson().fromJson(rs.getString("chessGame"), ChessGame.class);

            return new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame);
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void joinGame(int gameID, String username, ChessGame.TeamColor teamColor) throws DataAccessException {
        try {
            GameData gameData = this.getGame(gameID);

            if (teamColor == ChessGame.TeamColor.WHITE) {
                if (gameData.whiteUsername() != null) {
                    throw new DataAccessException("Error: already taken");
                }
                else {
                    try (var preparedStatement = DatabaseManager.getConnection().prepareStatement("UPDATE game SET whiteUsername = ? WHERE gameID = ?")) {
                        preparedStatement.setString(1,username);
                        preparedStatement.setInt(2,gameID);
                        preparedStatement.executeUpdate();
                    }
                }
            }
            else { //assuming its not null...
                if (gameData.blackUsername() != null) {
                    throw new DataAccessException("Error: already taken");
                }
                else {
                    try (var preparedStatement = DatabaseManager.getConnection().prepareStatement("UPDATE game SET blackUsername = ? WHERE gameID = ?")) {
                        preparedStatement.setString(1,username);
                        preparedStatement.setInt(2,gameID);
                        preparedStatement.executeUpdate();
                    }
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
