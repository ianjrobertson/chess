package server.Websocket;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WebSocketSessions {
    //we use this to store all the websocket sessions happening on our server.

    //so we need to modify our server code to open up a websocket connection when a game starts.
    //make this static? or make the map static?
    Map<Integer, Map<String, Session>> sessionMap;
    public WebSocketSessions() {
        sessionMap = new HashMap<>();
    }

    public void addSession(Integer gameID, String authToken, Session session) {
        if (sessionMap.containsKey(gameID)) { //If game exists,
            sessionMap.get(gameID).put(authToken, session); //update existing mapping
        }
        else { //If game does not exist
            Map<String, Session> newSession = new HashMap<>(); //create new mapping
            newSession.put(authToken, session);
            this.sessionMap.put(gameID, newSession);
        }
    }

    public void removeSessionFromGame(Integer gameID, String authToken, Session session) {
        if(sessionMap.containsKey(gameID)) {
            if (sessionMap.get(gameID).containsKey(authToken)) {
                sessionMap.get(gameID).remove(authToken, session);
            }
        }
        //maybe a try catch for if the gameID doesn't exist?
    }

    public void removeSession(Session session) {
        //Do we need to iterate through each game and then each group in the game to find the matching session and delete it

    }

    public Map<String, Session> getSessionsForGame(Integer gameID) {
        //default return?
        return this.sessionMap.get(gameID);
    }

}
