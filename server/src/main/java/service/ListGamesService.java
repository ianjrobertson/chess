package service;

import dataAccess.*;
import service.ServiceRecords.ListGamesRequest;
import service.ServiceRecords.ListGamesResponse;

public class ListGamesService {
    public ListGamesResponse listGames(ListGamesRequest r) throws DataAccessException {
        GameDAO gameDAO = new DatabaseGameDAO();
        AuthDAO authDAO = new DatabaseAuthDAO();
        if (authDAO.verifyAuth(r.authToken()) == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        return new ListGamesResponse(gameDAO.listGames());
    }
}
