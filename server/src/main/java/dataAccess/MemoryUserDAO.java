package dataAccess;

import model.UserData;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    private static final HashMap<String, UserData> userDataMap = new HashMap<>();
    @Override
    public void clear() {
        userDataMap.clear();
    }
    @Override
    public UserData createUser(String username, String password, String email) throws DataAccessException {
        if (username == null || password == null || email == null) {
            throw new DataAccessException("Error: bad request");
        }
        UserData newUser = new UserData(username, password, email);
        this.insertUser(newUser);
        return newUser;
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        if (!userDataMap.containsKey(username)) {
            throw new DataAccessException("Error: unauthorized");
        }
        return userDataMap.get(username);
    }

    @Override
    public boolean verifyUser(String username, String password) {
        return userDataMap.get(username).password().equals(password); // Return if the userData object associated with
    }

    @Override
    public void insertUser(UserData u) throws DataAccessException {
        if (userDataMap.containsKey(u.username()))
            throw new DataAccessException("Error: already taken");
        userDataMap.put(u.username(), u);
    }

    @Override
    public void deleteUser(String username) throws DataAccessException{
        if (!userDataMap.containsKey(username)) {
            throw new DataAccessException("Unknown user: " + username);
        }
        userDataMap.remove(username);
    }
}
