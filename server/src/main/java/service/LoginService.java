package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import model.UserData;

public class LoginService {
    public LoginResponse login(LoginRequest r) throws DataAccessException {
        MemoryUserDAO userDAO= new MemoryUserDAO();
        UserData u = userDAO.getUser(r.username()); // try to get the username from memory
        if (!userDAO.verifyUser(r.username(), r.password())) { //verify the username and password
            throw new DataAccessException("Error: unauthorized");
        }
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        String auth = authDAO.createAuth(r.username()); // generate a new auth and insert into mem
        return new LoginResponse(auth, r.username()); // return the login response object
    }
}
