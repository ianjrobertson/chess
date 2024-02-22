package dataAccess;

import model.UserData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {
    private static final HashMap<String, UserData> userDataMap = new HashMap<>();
    @Override
    public void clear() {
        userDataMap.clear();
    }
    @Override
    public UserData createUser(String username, String password, String email) {
        UserData newUser = new UserData(username, password, email);
        userDataMap.put(username, newUser);
        return newUser;
    }

    @Override
    public UserData getUser(String username) {
        return userDataMap.get(username);
    }

    public boolean verifyUser(String username, String password) {
        return userDataMap.get(username).password().equals(password); // Return if the userData object associated with
    }
}
