package server;

import service.ServiceRecords.LoginRequest;
import service.ServiceRecords.LoginResponse;
import service.ServiceRecords.RegisterRequest;
import service.ServiceRecords.RegisterResponse;

public class ServerFacade {

    public ServerFacade() {
        Server server = new Server();
        server.run(0); // need to figure out exactly what to do with this part.
    }
    //becausr how does it work with sending http requests to the server. what does that even mean.
    public LoginResponse login(LoginRequest r) {

        return null;
    }

    public RegisterResponse register(RegisterRequest r) {

        return null;
    }

}
