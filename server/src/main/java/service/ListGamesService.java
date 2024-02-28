package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import service.ServiceRecords.ListGamesRequest;
import service.ServiceRecords.ListGamesResponse;

public class ListGamesService {
    public ListGamesResponse listGames(ListGamesRequest r) throws DataAccessException {
        MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
        MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
        if (memoryAuthDAO.verifyAuth(r.authToken()) == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        return new ListGamesResponse(memoryGameDAO.listGames());
    }
}
