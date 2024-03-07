package service;

import dataAccess.*;
import model.UserData;
import service.ServiceRecords.LoginRequest;
import service.ServiceRecords.LoginResponse;

public class LoginService {
    public LoginResponse login(LoginRequest r) throws DataAccessException {
        UserDAO userDAO= new DatabaseUserDAO();
        UserData u = userDAO.getUser(r.username()); // try to get the username from memory
        if (!userDAO.verifyUser(r.username(), r.password())) { //verify the username and password
            throw new DataAccessException("Error: unauthorized");
        }
        AuthDAO authDAO = new DatabaseAuthDAO();
        String auth = authDAO.createAuth(r.username()); // generate a new auth and insert into mem
        return new LoginResponse(auth, r.username()); // return the login response object
    }
}
