package server.Websocket;

//import javax.websocket.Session;
import org.eclipse.jetty.websocket.api.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketSessions {
    //we use this to store all the websocket sessions happening on our server.

    //so we need to modify our server code to open up a websocket connection when a game starts.
    //make this static? or make the map static?
    ConcurrentHashMap<Integer, Map<String, Session>> sessionMap;
    public WebSocketSessions() {
        sessionMap = new ConcurrentHashMap<>();
    }

    public void clearOldConnections() {
        for (var game : sessionMap.keySet()) {
            for (var connection: sessionMap.get(game).values()) {
                if (!connection.isOpen()) {
                    sessionMap.remove(game);
                }
            }
        }
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
        for(var game: sessionMap.keySet()) {
            var connections = sessionMap.get(game);
            if (connections.containsValue(session)) {
                sessionMap.remove(game, connections);
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
