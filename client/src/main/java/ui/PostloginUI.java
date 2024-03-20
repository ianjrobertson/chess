package ui;

import server.Server;
import server.ServerFacade;
import service.ServiceRecords.LogoutRequest;

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
        System.out.println("Have a nice day! :)");
    }

    private void logout() {
        try {
            serverFacade.logout(new LogoutRequest(sessionAuthToken));
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void createGame(String name) {

    }

    private void listGames() {

    }

    private void joinGame(int gameID) {

    }

    private void observeGame(int gameID) {

    }

}
