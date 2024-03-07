package dataAccess;

import model.AuthData;

public class DatabaseAuthDAO implements AuthDAO{

    @Override
    public void clear() {

    }

    @Override
    public String createAuth(String username) {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }

    @Override
    public AuthData verifyAuth(String authToken) {
        return null;
    }
}
