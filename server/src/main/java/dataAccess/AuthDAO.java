package dataAccess;

import model.AuthData;

public interface AuthDAO {
    void clear();
    String createAuth(String username) throws DataAccessException;
    //boolean getAuth(String authToken);
    void deleteAuth(String authToken) throws DataAccessException;
    AuthData verifyAuth(String authToken);
    //void insertAuth(AuthData auth) throws DataAccessException;
}
