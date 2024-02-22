package dataAccess;

import model.AuthData;

public interface AuthDAO {
    void clear();
    String createAuth(String username);
    boolean getAuth(String authToken);
    void deleteAuth(String authToken);
    AuthData verifyAuth(String authToken);
}
