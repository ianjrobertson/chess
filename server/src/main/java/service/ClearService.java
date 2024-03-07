package service;

import dataAccess.*;

public class ClearService {

    public void clear() throws DataAccessException {
        //Now we use the data access objects to clear everything.
        //Instantiate a Data Access object for each of types and call the static
            UserDAO userDAO = new DatabaseUserDAO();
            userDAO.clear();
            AuthDAO authDAO = new DatabaseAuthDAO();
            authDAO.clear();
            GameDAO gameDAO = new DatabaseGameDAO();
            gameDAO.clear();

    }

}
