package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import model.UserData;

public class LoginService {
    LoginResponse login(LoginRequest r) {
        try {
            MemoryUserDAO userDAO= new MemoryUserDAO();
            UserData u = userDAO.getUser(r.username());
            if (userDAO.verifyUser(r.username(), r.password())) {
                MemoryAuthDAO authDAO = new MemoryAuthDAO();
                String auth = authDAO.createAuth(r.username());
                return new LoginResponse(auth, r.username());
            }
        }
        catch(DataAccessException d) {
            System.out.println("uh oh");
        }

    }
}
