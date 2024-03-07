package dataAccess;

import jdk.jshell.spi.ExecutionControl;
import model.UserData;

import java.sql.SQLException;
import java.sql.Statement;

import static dataAccess.DatabaseManager.getConnection;


public class DatabaseUserDAO implements UserDAO{

    public DatabaseUserDAO() throws DataAccessException {
        this.configureDatabase();
    }
    @Override
    public void clear() {
        try (var preparedStatement = getConnection().prepareStatement("TRUNCATE user")) {
            preparedStatement.executeUpdate();
        }
        catch (DataAccessException d) {
            //haha do nothing lol
        }
        catch (SQLException e) {
            //do nothing lol
        }
    }

    @Override
    public UserData createUser(String username, String password, String email) throws DataAccessException {
        try (var preparedStatement = getConnection().prepareStatement("INSERT INTO user (username, password, email), Values(?, ?, ?)") {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);

            preparedStatement.executeUpdate();
            return new UserData(username, password, email);
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var preparedStatement = getConnection().prepareStatement("SELECT username, password, email FROM user WHERE username = ?")) {
            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();

            var resultSet = preparedStatement.getResultSet();
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public boolean verifyUser(String username, String password) {
        return false;
    }

    @Override
    public void insertUser(UserData u) throws DataAccessException {

    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  user (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email`, varchar(256) NOT NULL
              PRIMARY KEY (`username`)
            )
            """
};

    public void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = getConnection()) {
            for (var statement: createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }



}
