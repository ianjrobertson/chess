package service;

import dataAccess.MemoryGameDAO;

public class ListGamesService {
    public ListGamesResponse listGames(ListGamesRequest r) {
        MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
        //verify the authtoken
        //Return the list of all the games.
        //I think this one will be pretty easy
    }
}
