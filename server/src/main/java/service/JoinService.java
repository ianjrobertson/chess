package service;

import dataAccess.DataAccessException;
import dataAccess.DatabaseAuthDAO;
import model.AuthData;

public class JoinService {
    public String join(String authToken) throws DataAccessException {
        DatabaseAuthDAO authDao = new DatabaseAuthDAO();
        AuthData data = authDao.verifyAuth(authToken);
        return data.username();
    }
}
