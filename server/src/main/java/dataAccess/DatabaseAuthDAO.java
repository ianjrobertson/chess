package dataAccess;

import model.AuthData;

import java.sql.SQLException;
import java.util.UUID;

import static dataAccess.DatabaseManager.getConnection;

public class DatabaseAuthDAO implements AuthDAO{

    public DatabaseAuthDAO() throws DataAccessException {
        DatabaseManager.configureDatabase(createStatements);
    }

    @Override
    public void clear() {
        try (var preparedStatement = getConnection().prepareStatement("TRUNCATE TABLE auth")) {
            preparedStatement.executeUpdate();
        }
        catch (DataAccessException d) {
            //nah lol
        }
        catch (SQLException e) {
            //lol
        }
    }

    @Override
    public String createAuth(String username) throws DataAccessException {
        try (var preparedStatement = getConnection().prepareStatement("INSERT INTO auth (username, auth), VALUES(?, ?)")) {
            String authToken = generateAuth();
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, authToken);
            preparedStatement.executeUpdate();

            return authToken;
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private String generateAuth() {
        return UUID.randomUUID().toString();
    }


    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }

    @Override
    public AuthData verifyAuth(String authToken) {
        return null;
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  auth (
              `username` varchar(256) NOT NULL,
              `auth` varchar(256) NOT NULL,
              PRIMARY KEY (`username`)
            )
            """
    };

    public void configureDatabase(String createStatement) throws DataAccessException {
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

