package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import service.*;
import service.ServiceRecords.*;
import spark.*;

public class Server {

    private final ClearService clearService;
    private final LoginService loginService;
    private final RegisterService registerService;
    private final LogoutService logoutService;
    private final ListGamesService listGamesService;
    private final CreateGameService createGameService;
    private final JoinGameService joinGameService;

    public Server() {
        clearService = new ClearService();
        loginService = new LoginService();
        registerService = new RegisterService();
        logoutService = new LogoutService();
        listGamesService = new ListGamesService();
        createGameService = new CreateGameService();
        joinGameService = new JoinGameService();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    //Make a handler for each of the endpoints

    private Object clear(Request req, Response res) {
        try {
            clearService.clear();
            res.status(200);
            return "{}";
        }
        catch (Exception e) {
            res.status(500);
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return new Gson().toJson(errorResponse);
        }
    }

    private Object register(Request req, Response res) {
        try {
            RegisterRequest registerRequest = new Gson().fromJson(req.body(), RegisterRequest.class);
            RegisterResponse registerResponse = registerService.register(registerRequest);
            //res.body();
            res.status(200);
            return new Gson().toJson(registerResponse);
        }
        catch(DataAccessException d) {
            if (d.getMessage().equals("Error: already taken")) {
                ErrorResponse errorResponse = new ErrorResponse(d.getMessage());
                //res.body(new Gson().toJson(errorResponse));
                res.status(403);
                return new Gson().toJson(errorResponse);
            }
            else if (d.getMessage().equals("Error: bad request")) {
                ErrorResponse errorResponse = new ErrorResponse(d.getMessage());
                res.status(400);
                return new Gson().toJson(errorResponse);
            }
            else {
                ErrorResponse errorResponse = new ErrorResponse(d.getMessage());
                //res.body(new Gson().toJson(errorResponse));
                res.status(500);
                return new Gson().toJson(errorResponse);
            }
            //Need to figure out what to do with Error" bad request
        }
    }

    private Object login(Request req, Response res) {
        //take the request object and break it down using gson,
        //perform the operation using the service class
        //then return the loginResponse in a Json using Gson
        try {
            LoginRequest loginRequest = new Gson().fromJson(req.body(), LoginRequest.class);
            LoginResponse loginResponse = loginService.login(loginRequest);
            res.status(200);
            return new Gson().toJson(loginResponse);
        }
        catch(DataAccessException d) {
            ErrorResponse errorResponse = new ErrorResponse(d.getMessage());
            if (d.getMessage().equals("Error: unauthorized")) {
                res.status(401);
            }
            else {
                res.status(500);
            }
            return new Gson().toJson(errorResponse);
        }
    }

    private Object logout(Request req, Response res) {
        //take the request object, should be an authToken string.
        //call the logout method on the logoutService class
        //Handle the exceptions!

        try {
            String authToken = req.headers("authorization");
            LogoutRequest logoutRequest = new LogoutRequest(authToken);
            //LogoutRequest logoutRequest = new Gson().fromJson(req.headers("authorization"), LogoutRequest.class);
            logoutService.logout(logoutRequest);
            res.status(200);
            return "{}";
        }
        catch (DataAccessException d) {
            if (d.getMessage().equals("Error: unauthorized")) {
                ErrorResponse errorResponse = new ErrorResponse(d.getMessage());
                res.status(401);
                return new Gson().toJson(errorResponse);
            }
            else {
                ErrorResponse errorResponse = new ErrorResponse(d.getMessage());
                //res.body(new Gson().toJson(errorResponse));
                res.status(500);
                return new Gson().toJson(errorResponse);
            }
        }
    }

    private Object listGames(Request req, Response res) {
        try {
            ListGamesRequest listGamesRequest = new ListGamesRequest(req.headers("authorization"));
            ListGamesResponse listGamesResponse = listGamesService.listGames(listGamesRequest);
            //res.body(new Gson().toJson(listGamesResponse));
            res.status(200);
            return new Gson().toJson(listGamesResponse);
        }
        catch(DataAccessException d) {
            ErrorResponse errorResponse = new ErrorResponse(d.getMessage());
            if (d.getMessage().equals("Error: unauthorized")) {
                res.status(401);
            }
            else {
                res.status(500);
            }
            return new Gson().toJson(errorResponse);
        }
    }

    private Object createGame(Request req, Response res) {
        try {
            String authToken = req.headers("authorization");
            CreateGameRequest createGameRequest = new Gson().fromJson(req.body(), CreateGameRequest.class);
            CreateGameResponse createGameResponse = createGameService.createGame(authToken, createGameRequest);
            //res.body(new Gson().toJson(createGameResponse));
            res.status(200);
            return new Gson().toJson(createGameResponse);

        }
        catch (DataAccessException d) {
            ErrorResponse errorResponse = new ErrorResponse(d.getMessage());
            res.body(new Gson().toJson(errorResponse));
            if (d.getMessage().equals("Error: bad request")) {
                res.status(400);
            }
            else if (d.getMessage().equals("Error: unauthorized")) {
                res.status(401);
            }
            else {
                res.status(500);
            }
            return new Gson().toJson(errorResponse);
        }
    }

    private Object joinGame(Request req, Response res) {
        try {
            String authToken = req.headers("authorization");
            JoinGameRequest joinGameRequest = new Gson().fromJson(req.body(), JoinGameRequest.class);
            joinGameService.joinGame(authToken, joinGameRequest);
            res.status(200);
            return "{}";
        }
        catch(DataAccessException d) {
            ErrorResponse errorResponse = new ErrorResponse(d.getMessage());
            if (d.getMessage().equals("Error: bad request")) {
                res.status(400);
            }
            else if (d.getMessage().equals("Error: unauthorized")) {
                res.status(401);
            }
            else if (d.getMessage().equals("Error: already taken")) {
                res.status(403);
            }
            else {
                res.status(500);
            }
            return new Gson().toJson(errorResponse);
        }
    }
}
