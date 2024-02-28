package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import service.ServiceRecords.LogoutRequest;

public class LogoutService {
    public void logout(LogoutRequest r) throws DataAccessException {
        //verify the authToken
        //Delete the authToken from the database
        MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
        if (memoryAuthDAO.verifyAuth(r.authToken()) == null) { //If the AuthData object returns null. Throw unauthorized error
            throw new DataAccessException("Error: unauthorized");
        }
        memoryAuthDAO.deleteAuth(r.authToken());
    }
}
