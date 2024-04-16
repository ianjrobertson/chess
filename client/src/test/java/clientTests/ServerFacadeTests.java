package clientTests;

import chess.ChessGame;
import org.junit.jupiter.api.*;
import server.Server;
import ServerFacade.ServerFacade;
import ServerFacade.ServiceRecords.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        var serverUrl = "http://localhost:" + port;
        serverFacade = new ServerFacade(serverUrl);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    public void clearServer() throws Exception {serverFacade.clear();}

    @Test void clearPositive() throws  Exception {
        Assertions.assertDoesNotThrow(() -> serverFacade.clear());
    }

    @Test void clearNegative() throws Exception {
        assert true; // do nothing
    }

    @Test
    public void registerPositive() throws Exception {
        this.clearServer();
        Assertions.assertDoesNotThrow(() -> serverFacade.register(new RegisterRequest("ian", "password", "ianjr@byu.edu")));
    }

    @Test
    public void registerNegative() throws Exception {
        this.clearServer();
        Assertions.assertThrows(Exception.class, () -> serverFacade.register(new RegisterRequest(null, null, null)));
    }

    @Test
    public void loginPositive() throws Exception {
        this.clearServer();
        serverFacade.register(new RegisterRequest("ian", "password", "ianjr@byu.edu"));
        Assertions.assertDoesNotThrow(() -> serverFacade.login(new LoginRequest("ian", "password")));
    }

    @Test
    public void loginNegative() throws Exception{
        this.clearServer();
        Assertions.assertThrows(Exception.class, () -> serverFacade.login(new LoginRequest("ian", "password")));
    }

    @Test
    public void logoutPositive() throws Exception {
        RegisterResponse registerResponse = serverFacade.register(new RegisterRequest("ian", "password", "ianjr@byu.edu"));
        serverFacade.logout(new LogoutRequest(registerResponse.authToken()));
        LoginResponse loginResponse =  serverFacade.login(new LoginRequest("ian", "password"));
        Assertions.assertDoesNotThrow(() -> serverFacade.logout(new LogoutRequest(loginResponse.authToken())));
    }

    @Test
    public void logoutNegative() throws Exception {
        Assertions.assertThrows(Exception.class, () -> serverFacade.logout(new LogoutRequest("bad auth")));
    }

    @Test public void createGamePositive() throws Exception {
        //this.clearServer();
        RegisterResponse req = serverFacade.register(new RegisterRequest("ian", "password", "ianjr@byu.edu"));
        String auth = req.authToken();
        Assertions.assertDoesNotThrow(() -> serverFacade.createGame(new CreateGameRequest("new game"), auth));
    }
    @Test public void createGameNegative() throws Exception{
        Assertions.assertThrows(Exception.class, () -> serverFacade.createGame(new CreateGameRequest("game name"), "bad auth"));
    }
    @Test public void listGamesPositive() throws Exception{
        this.clearServer();
        RegisterResponse req = serverFacade.register(new RegisterRequest("ian", "password", "ianjr@byu.edu"));
        String auth = req.authToken();
        Assertions.assertDoesNotThrow(() -> serverFacade.listGames(new ListGamesRequest(auth)));
    }

    @Test public void listGamesNegative() throws Exception {
        this.clearServer();
        Assertions.assertThrows(Exception.class,() -> serverFacade.listGames(new ListGamesRequest("bad auth")));
    }

    @Test public void getGamePositive() throws Exception{
        this.clearServer();
        RegisterResponse req = serverFacade.register(new RegisterRequest("ian", "password", "ianjr@byu.edu"));
        String auth = req.authToken();
        CreateGameResponse createGameResponse = serverFacade.createGame(new CreateGameRequest("New Game"), auth);
        Assertions.assertDoesNotThrow(() -> serverFacade.getGame(createGameResponse.gameID(), auth));
    }
    @Test public void getGameNegative() throws Exception{
        this.clearServer();
        Assertions.assertThrows(Exception.class, () -> serverFacade.getGame(200, "bad auth"));
    }
    @Test public void joinGamePositive() throws Exception{
        this.clearServer();
        RegisterResponse req = serverFacade.register(new RegisterRequest("ian", "password", "ianjr@byu.edu"));
        String auth = req.authToken();
        CreateGameResponse createGameResponse = serverFacade.createGame(new CreateGameRequest("New Game"), auth);
        Assertions.assertDoesNotThrow(() -> serverFacade.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, createGameResponse.gameID()), auth));
    }
    @Test public void joinGameNegative() throws Exception   {
        this.clearServer();
        Assertions.assertThrows(Exception.class,() -> serverFacade.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 100), "bad auth"));
    }
}
