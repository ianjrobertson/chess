package dataAccess;

import model.AuthData;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.UUID;

import static dataAccess.DatabaseManager.getConnection;

public class DatabaseAuthDAO implements AuthDAO{

    public DatabaseAuthDAO() throws DataAccessException {
        DatabaseManager.configureDatabase(createStatements);
    }

    @Override
    public void clear() {
        try (var preparedStatement = getConnection().prepareStatement("DROP TABLE auth")) {
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
        try (var preparedStatement = getConnection().prepareStatement("INSERT INTO auth (username, auth) VALUES(?, ?)")) {
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
        try (var preparedStatement = getConnection().prepareStatement("DELETE FROM auth WHERE auth = ?")) {
            preparedStatement.setString(1, authToken);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new DataAccessException("Error: unauthorized");
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public AuthData verifyAuth(String authToken) throws DataAccessException {
        // Given an authData. If not .next() we can return null()
        try (var preparedStatement = getConnection().prepareStatement("SELECT username FROM auth WHERE auth = ?")) {
            preparedStatement.setString(1, authToken);
            preparedStatement.executeQuery();

            try (var resultset = preparedStatement.getResultSet()) {
                if (resultset.next()) {
                    String fetchedUsername = resultset.getString("username");
                    return new AuthData(authToken, fetchedUsername);
                }
                else {
                    return null;
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  auth (
              username varchar(256) NOT NULL,
              auth varchar(256) NOT NULL,
              PRIMARY KEY (auth)
            )
            """
    };
}

