package service;

import dataAccess.*;
import service.ServiceRecords.RegisterRequest;
import service.ServiceRecords.RegisterResponse;

public class RegisterService {
    public RegisterResponse register(RegisterRequest r) throws DataAccessException {
        UserDAO userDAO = new DatabaseUserDAO();
        AuthDAO authDAO = new DatabaseAuthDAO();

        userDAO.createUser(r.username(), r.password(),r.email()); // try to create the user. If taken throws dataAccessException
        String auth = authDAO.createAuth(r.username());

        return new RegisterResponse(r.username(), auth);

    }
}
