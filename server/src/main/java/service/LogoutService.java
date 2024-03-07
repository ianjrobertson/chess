package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.DatabaseAuthDAO;
import dataAccess.MemoryAuthDAO;
import service.ServiceRecords.LogoutRequest;

public class LogoutService {
    public void logout(LogoutRequest r) throws DataAccessException {
        //verify the authToken
        //Delete the authToken from the database
        AuthDAO authDAO = new DatabaseAuthDAO();
        if (authDAO.verifyAuth(r.authToken()) == null) { //If the AuthData object returns null. Throw unauthorized error
            throw new DataAccessException("Error: unauthorized");
        }
        authDAO.deleteAuth(r.authToken());
    }
}
