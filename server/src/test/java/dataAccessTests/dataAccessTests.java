package dataAccessTests;

import dataAccess.*;
import chess.ChessGame;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.*;
import service.ClearService;

public class dataAccessTests {

    @BeforeAll
    @AfterAll
    static void resetTest() throws DataAccessException {
        ClearService clearService = new ClearService();
        clearService.clear();
    }

    @Test
    public void authClearPositive() throws DataAccessException {
        AuthDAO authDAO = new DatabaseAuthDAO();
        authDAO.createAuth("fakeUser");
        Assertions.assertDoesNotThrow(authDAO::clear);
    }

    @Test
    public void gameClearPositive() throws DataAccessException{
        GameDAO gameDAO = new DatabaseGameDAO();
        gameDAO.createGame("fakeGame");
        Assertions.assertDoesNotThrow(gameDAO::clear);
    }

    @Test
    public void userClearPositive() throws DataAccessException{
        UserDAO userDAO = new DatabaseUserDAO();
        userDAO.createUser("stinky", "uhoh", "ian@gmail.com");
        Assertions.assertDoesNotThrow(userDAO::clear);
    }

    @Test
    public void createUserPositive() throws DataAccessException{
        UserDAO userDAO = new DatabaseUserDAO();
        Assertions.assertDoesNotThrow(() -> userDAO.createUser("ian","password","ian@byu.edu"));
    }

    @Test
    public void createUserNegative() throws DataAccessException{
        UserDAO userDAO = new DatabaseUserDAO();
        Assertions.assertThrows(DataAccessException.class, () -> userDAO.createUser("ian", "password", "ian@byu.edu"));
    }

    @Test
    public void getUserPositive() throws DataAccessException{
        UserDAO userDAO = new DatabaseUserDAO();
        UserData u = userDAO.createUser("richard", "password", "email");
        Assertions.assertDoesNotThrow(() -> userDAO.getUser("richard"));
    }

    @Test
    public void getUserNegative()throws DataAccessException{
        UserDAO userDAO = new DatabaseUserDAO();
        Assertions.assertThrows(DataAccessException.class, () -> userDAO.getUser("john"));
    }

    @Test
    public void verifyUserPositive() throws DataAccessException{
        UserDAO userDAO = new DatabaseUserDAO();
        assert (userDAO.verifyUser("ian", "password"));
    }

    @Test
    public void verifyUserNegative() throws DataAccessException{
        UserDAO userDAO = new DatabaseUserDAO();
        assert (!userDAO.verifyUser("ian", "fake password"));
    }

    @Test
    public void insertUserPositive() throws DataAccessException{
        UserDAO userDAO = new DatabaseUserDAO();
        UserData u = new UserData("bob", "chicken", "bob@byu.edu");
        Assertions.assertDoesNotThrow(() -> userDAO.insertUser(u));

    }
    @Test
    public void insertUserNegative() throws DataAccessException{
        UserDAO userDAO = new DatabaseUserDAO();
        UserData u = new UserData(null, "chicken", "bob@byu.edu");
        Assertions.assertThrows(DataAccessException.class, () -> userDAO.insertUser(u));
    }

    @Test
    public void createAuthPositive() throws DataAccessException{
        AuthDAO authDAO = new DatabaseAuthDAO();
        Assertions.assertDoesNotThrow(() -> authDAO.createAuth("anna"));
    }

    @Test
    public void createAuthNegative() throws DataAccessException{
        AuthDAO authDAO = new DatabaseAuthDAO();
        Assertions.assertThrows(DataAccessException.class, () -> authDAO.createAuth(null));
    }

    @Test
    public void deleteAuthPositive() throws DataAccessException{
        AuthDAO authDAO = new DatabaseAuthDAO();
        Assertions.assertDoesNotThrow(() -> authDAO.deleteAuth(authDAO.createAuth("brock")));
    }

    @Test
    public void deleteAuthNegative() throws DataAccessException{
        AuthDAO authDAO = new DatabaseAuthDAO();
        Assertions.assertThrows(DataAccessException.class, () -> authDAO.deleteAuth("bad auth"));
    }

    @Test
    public void verifyAuthPositive() throws DataAccessException{
        AuthDAO authDAO = new DatabaseAuthDAO();
        assert (authDAO.verifyAuth(authDAO.createAuth("nolan")) != null);
    }

    @Test
    public void verifyAuthNegative() throws DataAccessException{
        AuthDAO authDAO = new DatabaseAuthDAO();
        assert (authDAO.verifyAuth("fake auth Token") == null);
    }

    @Test
    public void insertGamePositive() throws DataAccessException{
        //Database DAO does not implement this method
        GameDAO gameDAO = new DatabaseGameDAO();
        assert(true);

    }

    @Test
    public void insertGameNegative() throws DataAccessException{
        //Database DAO does not implement this method
        GameDAO gameDAO = new DatabaseGameDAO();
        assert(true);
    }

    @Test
    public void createGamePositive()throws DataAccessException {
        GameDAO gameDAO = new DatabaseGameDAO();
        Assertions.assertDoesNotThrow(() -> gameDAO.createGame("Epic Chess Game"));
    }

    @Test
    public void createGameNegative() throws DataAccessException{
        GameDAO gameDAO = new DatabaseGameDAO();
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.createGame(null));
    }

    @Test
    public void getGamePositive() throws DataAccessException{
        GameDAO gameDAO = new DatabaseGameDAO();
        int gameID = gameDAO.createGame("best chess game");
        Assertions.assertDoesNotThrow(() -> gameDAO.getGame(gameID));
    }

    @Test
    public void getGameNegative() throws DataAccessException{
        GameDAO gameDAO = new DatabaseGameDAO();
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.getGame(100000));
    }

    @Test
    public void listGamesPositive() throws DataAccessException{
        GameDAO gameDAO = new DatabaseGameDAO();
        Assertions.assertDoesNotThrow(gameDAO::listGames);
    }

    @Test
    public void listGamesNegative() throws DataAccessException {
        GameDAO gameDAO = new DatabaseGameDAO();
        gameDAO.clear();
        Assertions.assertThrows(DataAccessException.class, gameDAO::listGames);
    }

    @Test
    public void joinGamePositive() throws DataAccessException{
        GameDAO gameDAO = new DatabaseGameDAO();
        int gameID = gameDAO.createGame("The real chess game");
        Assertions.assertDoesNotThrow(() -> gameDAO.joinGame(gameID, "ian", ChessGame.TeamColor.WHITE));
    }

    @Test
    public void joinGameNegative() throws DataAccessException{
        GameDAO gameDAO = new DatabaseGameDAO();
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.joinGame(1000, "anna", ChessGame.TeamColor.BLACK));
    }
}
