package dataAccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{
    private static final HashMap<String, AuthData> authDataMap = new HashMap<>();
    @Override
    public void clear() {
        authDataMap.clear();
    }
    @Override
    public String createAuth(String username) {
        String authToken = generateAuth();
        authDataMap.put(authToken, new AuthData(authToken, username));
        return authToken;
    }

    /**
    @Override
    public boolean getAuth(String authToken) { //return if given authToken exists in the list.
        return authDataMap.containsKey(authToken);
    }
     **/

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        if (!authDataMap.containsKey(authToken)) {
            throw new DataAccessException("Unknown authToken");
        }
        authDataMap.remove(authToken);
    }

    private String generateAuth() {
        return UUID.randomUUID().toString();
    }

    @Override
    public AuthData verifyAuth(String authToken) {
            return authDataMap.getOrDefault(authToken, null); //If the given authToken maps to a authData object, return the object else return null
    }

    /**
    @Override
    public void insertAuth(AuthData auth) throws DataAccessException {
        if (authDataMap.containsKey(auth.authToken())) {
            throw new DataAccessException("authToken already exists");
        }
        authDataMap.put(auth.authToken(), auth);
    }
    **/
}
