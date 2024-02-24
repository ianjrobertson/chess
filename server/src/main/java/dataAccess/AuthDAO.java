package dataAccess;

import model.AuthData;

import javax.xml.crypto.Data;

public interface AuthDAO {
    void clear();
    String createAuth(String username);
    boolean getAuth(String authToken);
    void deleteAuth(String authToken) throws DataAccessException;
    AuthData verifyAuth(String authToken);
    void insertAuth(AuthData auth) throws DataAccessException;
}
