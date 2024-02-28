package serviceTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;

import service.*;
import service.ServiceRecords.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class serviceTests {

    @BeforeEach
    public void resetTest() {
        ClearService clearService = new ClearService();
        clearService.clear();
    }
    @Test
    public void clearTest() throws DataAccessException {
        MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
        MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
        MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
        ClearService clearService = new ClearService();
        clearService.clear();
        assert (memoryGameDAO.listGames().isEmpty());
    }
    @Test
    public void registerPass() throws DataAccessException {
        MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
        MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
        MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
        RegisterService registerService = new RegisterService();
        registerService.register(new RegisterRequest("user", "password", "email"));
        assert (memoryUserDAO.getUser("user") != null);
    }
    @Test
    public void registerFail() throws DataAccessException {

        MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
        MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
        MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
        RegisterService registerService = new RegisterService();
        assertThrows (DataAccessException.class, () -> registerService.register(new RegisterRequest("user", null, "email")));

    }
    @Test
    public void loginPass() throws DataAccessException {
        MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
        MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
        MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
        LoginService loginService = new LoginService();
        RegisterService registerService = new RegisterService();
        registerService.register(new RegisterRequest("user", "password", "email"));
        loginService.login(new LoginRequest("user", "password"));
        assert (memoryUserDAO.verifyUser("user", "password"));
    }
    @Test
    public void loginFail() throws DataAccessException {
        MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
        MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
        MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
        LoginService loginService = new LoginService();
        assertThrows (DataAccessException.class, () -> loginService.login(new LoginRequest("user", "password")));

    }
    @Test
    public void logoutPass() throws DataAccessException {
        MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
        MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
        MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
        LogoutService logoutService = new LogoutService();
        RegisterService registerService = new RegisterService();
        registerService.register(new RegisterRequest("user", "password", "email"));
        String auth = memoryAuthDAO.createAuth("user");
        logoutService.logout(new LogoutRequest(auth));
        assert (memoryAuthDAO.verifyAuth(auth) == null);

    }
    @Test
    public void logoutFail() throws DataAccessException {
        MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
        MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
        MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
        LogoutService logoutService = new LogoutService();
        RegisterService registerService = new RegisterService();
        registerService.register(new RegisterRequest("user", "password", "email"));
        String auth = memoryAuthDAO.createAuth("user");
        assertThrows (DataAccessException.class, () -> logoutService.logout(new LogoutRequest("bad auth token")));

    }
    @Test
    public void createGamePass() throws DataAccessException {
        MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
        MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
        MemoryGameDAO memoryGameDAO = new MemoryGameDAO();

        String auth = new MemoryAuthDAO().createAuth("user");
        CreateGameService createGameService = new CreateGameService();
        CreateGameResponse createGameResponse = createGameService.createGame(auth, new CreateGameRequest("New Game"));
        assert (memoryGameDAO.getGame(createGameResponse.gameID()) != null);

    }
    @Test
    public void createGameFail() throws DataAccessException {
        MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
        MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
        MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
        CreateGameService createGameService = new CreateGameService();
        assertThrows (DataAccessException.class, () -> createGameService.createGame("bad Auth", new CreateGameRequest("New Game")));

    }
    @Test
    public void listGamesPass() throws DataAccessException {
        MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
        MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
        MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
        ListGamesService listGamesService = new ListGamesService();
        String auth = memoryAuthDAO.createAuth("brock");
        memoryGameDAO.insertGame(new GameData(100, "brock", "Ian", "bad game", new ChessGame()));
        ListGamesResponse listGamesResponse = listGamesService.listGames(new ListGamesRequest(auth));
        assert (!listGamesResponse.games().isEmpty());
    }
    @Test
    public void listGamesFail() throws DataAccessException {
        MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
        MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
        MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
        ListGamesService listGamesService = new ListGamesService();
        memoryGameDAO.insertGame(new GameData(100, "brock", "Ian", "bad game", new ChessGame()));
        assertThrows (DataAccessException.class, () -> listGamesService.listGames(new ListGamesRequest("bad auth token")));

    }
    @Test
    public void joinGamePass() throws DataAccessException {
        MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
        MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
        MemoryGameDAO memoryGameDAO = new MemoryGameDAO();

        JoinGameService joinGameService = new JoinGameService();
        memoryGameDAO.insertGame(new GameData(100, "brock", null, "bad game", new ChessGame()));
        String auth = memoryAuthDAO.createAuth("ian");
        joinGameService.joinGame(auth, new JoinGameRequest(ChessGame.TeamColor.BLACK, 100));
        assert (memoryGameDAO.getGame(100).blackUsername().equals("ian"));

    }
    @Test
    public void joinGameFail() throws DataAccessException {
        MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
        MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
        MemoryGameDAO memoryGameDAO = new MemoryGameDAO();

        JoinGameService joinGameService = new JoinGameService();
        memoryGameDAO.insertGame(new GameData(100, "brock", null, "bad game", new ChessGame()));
        assertThrows (DataAccessException.class, () -> joinGameService.joinGame("bad auth token", new JoinGameRequest(ChessGame.TeamColor.BLACK, 100)));
    }

}
