package serviceTests;

import chess.ChessGame;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import org.junit.jupiter.api.*;
import passoffTests.obfuscatedTestClasses.TestServerFacade;
import passoffTests.testClasses.TestException;
import passoffTests.testClasses.TestModels;
import server.Server;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;

import service.ClearService;
import service.ListGamesService;

public class serviceTests {

    @Test
    public void clearTest() {
        MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
        MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
        MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
        ClearService clearService = new ClearService();
        clearService.clear();
        assert (memoryGameDAO.listGames().isEmpty());
    }

    public void registerPass() {

    }

    public void registerFail() {

    }

    public void loginPass() {

    }

    public void loginFail() {

    }

    public void logoutPass() {

    }

    public void logoutFail() {

    }

    public void creatGamePass() {

    }

    public void createGameFail() {

    }

    public void listGamesPass() {

    }

    public void listGamesFail() {

    }

    public void joinGamePass() {

    }

    public void joinGameFail() {

    }

}
