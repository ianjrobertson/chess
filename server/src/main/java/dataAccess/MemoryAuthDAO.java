package dataAccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{

    //Need to store a map of the auth tokens
    //Need to have function that creates an authToken. Maybe have a private helper function that adds it to the database.

    //A map of authToken strings to AuthData objects.
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

    @Override
    public boolean getAuth(String authToken) { //return if given authToken exists in the list.
        return authDataMap.containsKey(authToken);
    }
    @Override
    public void deleteAuth(String authToken) {
        authDataMap.remove(authToken);
    }

    private String generateAuth() {
        return UUID.randomUUID().toString();
    }

    @Override
    public AuthData verifyAuth(String authToken) {
            return authDataMap.getOrDefault(authToken, null); //If the given authToken maps to a authData object, return the object else return null
    }
}
