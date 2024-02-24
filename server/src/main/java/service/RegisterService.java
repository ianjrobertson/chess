package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import model.UserData;

public class RegisterService {
    public RegisterResponse register(RegisterRequest r) throws DataAccessException {
        MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
        MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();

        memoryUserDAO.createUser(r.username(), r.password(),r.email()); // try to create the user. If taken throws dataAccessException
        String auth = memoryAuthDAO.createAuth(r.username());

        return new RegisterResponse(r.username(), auth);

    }
}
