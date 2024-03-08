package dataAccessTests;

import dataAccess.DataAccessException;
import chess.ChessGame;
import org.junit.jupiter.api.*;
import service.ClearService;

public class dataAccessTests {

    @BeforeAll
    public void resetTest() throws DataAccessException {
        ClearService clearService = new ClearService();
        clearService.clear();
    }

    @Test
    public void authClearPositive() {

    }

    @Test
    public void gameClearPositive() {

    }

    @Test
    public void userClearPositive() {

    }

    @Test
    public void createUserPositive() {

    }

    @Test
    public void createUserNegative() {

    }

    @Test
    public void getUserPositive() {

    }

    @Test
    public void getUserNegative() {

    }

    @Test
    public void verifyUserPositive() {

    }

    @Test
    public void verifyUserNegative() {

    }

    @Test
    public void insertUserPositive() {

    }
    @Test
    public void insertUserNegative() {

    }

    @Test
    public void createAuthPositive() {

    }

    @Test
    public void createAuthNegative() {

    }

    @Test
    public void deleteAuthPositive() {

    }

    @Test
    public void deleteAuthNegative() {

    }

    @Test
    public void verifyAuthPositive() {

    }

    @Test
    public void verifyAuthNegative() {

    }

    @Test
    public void insertGamePositive() {

    }

    @Test
    public void insertGameNegative() {

    }

    @Test
    public void createGamePositive() {

    }

    @Test
    public void createGameNegative() {

    }

    @Test
    public void getGamePositive() {

    }

    @Test
    public void getGameNegative() {

    }

    @Test
    public void listGamesPositive() {

    }

    @Test
    public void listGamesNegative() {

    }

    @Test
    public void joinGamePositive() {

    }

    @Test
    public void joinGameNegative() {

    }
}
