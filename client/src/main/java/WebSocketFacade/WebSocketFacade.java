package WebSocketFacade;

import com.google.gson.Gson;
import server.Server;
import webSocketMessages.serverMessages.ServerMessage;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

public class WebSocketFacade extends Endpoint implements MessageHandler.Whole<String> {
    private Session session;
    //private GameHandler gameHandler;

    public WebSocketFacade() {

    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}

    public void connect() {

    }

    public void disconnect() {

    }


    @Override
    public void onMessage(String s) {
        //Deserialize message
        ServerMessage message = new Gson().fromJson(s, ServerMessage.class);

        //Switch statements that handle the type of the message.

        //Send the given type to the gameHandler that will process it.


        //Call gameHandler to process message
    }
}
