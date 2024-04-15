package server.Websocket;

//import javax.websocket.Session;
import org.eclipse.jetty.websocket.api.*;
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

    public void addSession(Integer gameID, String authToken, Session session) throws Exception {
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
        //so each game has a map of the Users in the game and the session.
        //So we need to find the game that has the related session. And then remove The game <user, session> pairing from the session map
        Iterator<Map.Entry<Integer, Map<String, Session>>> iterator = this.sessionMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Map<String, Session>> entry = iterator.next();
            Map<String, Session> innerMap = entry.getValue();
            if (innerMap.containsValue(session)) {
                iterator.remove(); // Remove the entry from the outer map
            }
        }
    }

    public Map<String, Session> getSessionsForGame(Integer gameID) throws Exception {
        if (this.sessionMap.get(gameID) == null)
            throw new Exception("Error: Bad Request");
        return this.sessionMap.get(gameID);
    }

    public Integer getGameID(String authToken) {
        //given an auth token
        //return the mapping
        for (var game: sessionMap.keySet()) {
            if (sessionMap.get(game).containsKey(authToken)) {
                return game;
            }
        }
        return null;
    }
}
