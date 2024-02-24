package service;

import dataAccess.*;

public class ClearService {

    public void clear() {
        //Now we use the data access objects to clear everything.
        //Instantiate a Data Access object for each of types and call the static

        UserDAO userDAO = new MemoryUserDAO();
        userDAO.clear();
        AuthDAO authDAO = new MemoryAuthDAO();
        authDAO.clear();
        GameDAO gameDAO = new MemoryGameDAO();
        gameDAO.clear();
    }

}
