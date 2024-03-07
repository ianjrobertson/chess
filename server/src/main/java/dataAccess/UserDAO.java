package dataAccess;

import model.UserData;

import javax.xml.crypto.Data;

public interface UserDAO {
    void clear();
    UserData createUser(String username, String password, String email) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    boolean verifyUser(String username, String password) throws DataAccessException;
    void insertUser(UserData u) throws DataAccessException;
    //void deleteUser(String username) throws DataAccessException;

}
