package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import model.AuthData;

public class CreateGameService {
    public CreateGameResponse createGame(String authToken, CreateGameRequest r) throws DataAccessException {

        //verify the authToken
        //I don't even know if we need to verify that there is not a game with the same name
        //
        MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
        MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
        AuthData authData = memoryAuthDAO.verifyAuth(authToken);
        if (authData == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        return new CreateGameResponse(memoryGameDAO.createGame(r.gameName()));
    }
}
