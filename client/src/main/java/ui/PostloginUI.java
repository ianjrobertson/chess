package ui;

import chess.ChessGame;
import server.Server;
import server.ServerFacade;
import service.ServiceRecords.*;

import java.util.Scanner;

public class PostloginUI {
    private boolean running;
    private final ServerFacade serverFacade;
    private final String sessionAuthToken;
    public PostloginUI(ServerFacade facade, String authToken) {
        running = true;
        serverFacade = facade;
        sessionAuthToken = authToken;
    }

    public void run() {
        System.out.println("Login Successful - Type \"help\" to get started");
        Scanner scanner = new Scanner(System.in);
        while (running) {
            String input = scanner.nextLine();
            String command = getFirstWord(input);
            switch(command) {
                case("logout") -> this.logout();
                case("help") -> this.help();
                case("quit") -> this.quit();
                case("create") -> this.createGame(input);
                case("list") -> this.listGames();
                case("join") -> this.joinGame(input);
                case null, default -> this.unknownInput();
            }
        }
    }
    private void unknownInput() {
        System.out.println("Unknown input, please try again");
    }

    public static String getFirstWord(String line) {
        String[] words = line.trim().split("\\s+");
        if (words.length > 0) {
            return words[0];
        } else {
            return ""; // or throw an exception, depending on your requirements
        }
    }

    private void help() {
        System.out.println("help - list options");
        System.out.println("logout - logout the session");
        System.out.println("create <NAME> - create a new game");
        System.out.println("list - games");
        System.out.println("join <ID> [WHITE|BLACK|<empty>] - join existing game");
        System.out.println("observe <ID> - observe game");
        System.out.println("quit - end session");
    }

    private void quit() {
        running = false;
    }

    private void logout() {
        try {
            serverFacade.logout(new LogoutRequest(sessionAuthToken));
            System.out.println("Logout Successful - type \"help\" to get started");
            this.quit();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void createGame(String input) {
        String[] words = input.trim().split("\\s+");
        String name = words[1];
        try {
            CreateGameResponse res = serverFacade.createGame(new CreateGameRequest(name), sessionAuthToken);
            System.out.println("Game Created. Game ID: " + res.gameID());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void listGames() {
        try {
            ListGamesResponse res = serverFacade.listGames(new ListGamesRequest(sessionAuthToken));
            System.out.println(res);
            //To String method for listGames??
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void joinGame(String input) {
        String[] words = input.trim().split("\\s+");
        Integer gameID = Integer.parseInt(words[1]);
        String color = words[2];
        ChessGame.TeamColor teamColor;
        if (color.equalsIgnoreCase("white")) {
            teamColor = ChessGame.TeamColor.WHITE;
        }
        else if (color.equalsIgnoreCase("black")) {
            teamColor = ChessGame.TeamColor.BLACK;
        }
        else {
            this.unknownInput();
            return;
        }
        JoinGameRequest req = new JoinGameRequest(teamColor, gameID);
        try {
            serverFacade.joinGame(req, sessionAuthToken);
            System.out.println("Successfully Joined game: " + gameID);
            //Call GameUI
            GameplayUI gameplayUI = new GameplayUI(gameID, sessionAuthToken, serverFacade);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void observeGame(int gameID) {
        //Maybe we just make a gameUI that displays the game
    }

}
