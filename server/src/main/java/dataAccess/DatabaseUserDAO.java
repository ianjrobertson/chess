package dataAccess;

import jdk.jshell.spi.ExecutionControl;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.sql.Statement;

import static dataAccess.DatabaseManager.getConnection;


public class DatabaseUserDAO implements UserDAO{

    public DatabaseUserDAO() throws DataAccessException {
        DatabaseManager.configureDatabase(createStatements);
    }
    @Override
    public void clear() {
        try (var preparedStatement = getConnection().prepareStatement("TRUNCATE TABLE user")) {
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
        try (var preparedStatement = getConnection().prepareStatement("INSERT INTO user (username, password, email), Values(?, ?, ?)")) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String hashedPassword = encoder.encode(password);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.setString(3, email);

            preparedStatement.executeUpdate();
            return new UserData(username, hashedPassword, email);
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

            try (var resultSet = preparedStatement.getResultSet()) {
                if (resultSet.next()) {
                    String fetchedUsername = resultSet.getString("username");
                    String fetchedPassword = resultSet.getString("password");
                    String fetchedEmail = resultSet.getString("email");

                    return new UserData(fetchedUsername, fetchedPassword, fetchedEmail);
                }
                else {
                    throw new DataAccessException("Error: couldn't find it lol"); //TODO add a verbose error message
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public boolean verifyUser(String username, String password) throws DataAccessException {
        try (var preparedStatement = getConnection().prepareStatement("SELECT password FROM user WHERE username = ?")) {
            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate(); //Retrieve the hashed password from the database.
            try (var resultSet = preparedStatement.getResultSet()) {
                if (resultSet.next()) {
                    String fetchedPassword = resultSet.getString("password");
                    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                    return encoder.matches(password, fetchedPassword); // Compare the hashed password with the given password.
                }
                else {
                    return false;
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void insertUser(UserData u) throws DataAccessException {
        this.createUser(u.username(), u.password(), u.email()); //lol
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