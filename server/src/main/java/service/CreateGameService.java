package service;

import dataAccess.*;
import model.AuthData;
import service.ServiceRecords.CreateGameRequest;
import service.ServiceRecords.CreateGameResponse;

public class CreateGameService {
    public CreateGameResponse createGame(String authToken, CreateGameRequest r) throws DataAccessException {

        //verify the authToken
        //I don't even know if we need to verify that there is not a game with the same name
        //
        AuthDAO authDAO = new DatabaseAuthDAO();
        GameDAO gameDAO = new DatabaseGameDAO();
        AuthData authData = authDAO.verifyAuth(authToken);
        if (authData == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        return new CreateGameResponse(gameDAO.createGame(r.gameName()));
    }
}
