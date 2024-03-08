package dataAccess;

import model.AuthData;

public interface AuthDAO {
    void clear() throws DataAccessException;
    String createAuth(String username) throws DataAccessException;
    //boolean getAuth(String authToken);
    void deleteAuth(String authToken) throws DataAccessException;
    AuthData verifyAuth(String authToken) throws DataAccessException;
    //void insertAuth(AuthData auth) throws DataAccessException;
}
