package ui;

import chess.ChessGame;
import model.GameData;
import ServerFacade.ServerFacade;
import service.ServiceRecords.*;

import java.util.*;

public class PostloginUI {
    private boolean running;
    private final ServerFacade serverFacade;
    private final String sessionAuthToken;
    private HashMap<Integer, Integer> gameIDMap;
    public PostloginUI(ServerFacade facade, String authToken) {
        running = true;
        serverFacade = facade;
        sessionAuthToken = authToken;
        gameIDMap = new HashMap<>();
    }

    public void run() {
        System.out.println("Login Successful - Type \"help\" to get started");
        Scanner scanner = new Scanner(System.in);
        while (running) {
            String input = scanner.nextLine();
            String command = getFirstWord(input);
            command = command.toLowerCase(Locale.ROOT);
            switch(command) {
                case("logout") -> this.logout();
                case("help") -> this.help();
                case("create") -> this.createGame(input);
                case("list") -> this.listGames();
                case("join") -> this.joinGame(input);
                case("observe") -> this.observeGame(input);
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
        System.out.println("join <ID> [WHITE|BLACK] - join existing game");
        System.out.println("observe <ID> - observe game");
    }

    private void quit() {
        running = false;
    }

    private void logout() {
        try {
            serverFacade.logout(new LogoutRequest(sessionAuthToken));
            System.out.println("Logout Successful");
            System.out.println("Type \"help\" to get started");
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
            gameIDMap.put(gameIDMap.size() + 1, res.gameID());
            System.out.println("Game Created. \"list\" games to join");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void listGames() {
        try {
            ListGamesResponse res = serverFacade.listGames(new ListGamesRequest(sessionAuthToken));
            Collection<GameData> gamesList = res.games();
            int gameIndex = 1;
            for (GameData game: gamesList) {
                gameIDMap.put(gameIndex, game.gameID());
                System.out.print(gameIndex++ + ". ");
                System.out.println(game.listGame());
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void joinGame(String input) {
        try {
            String[] words = input.trim().split("\\s+");
            int gameID = gameIDMap.get(Integer.parseInt(words[1]));
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
            serverFacade.joinGame(req, sessionAuthToken);
            System.out.println("Successfully Joined game: " + gameID);
            //Call GameUI
            GameplayUI gameplayUI = new GameplayUI(gameID, sessionAuthToken, serverFacade);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void observeGame(String input) {
        try {
            String[] words = input.trim().split("\\s+");
            int gameID = gameIDMap.get(Integer.parseInt(words[1]));
            JoinGameRequest req = new JoinGameRequest(null, gameID);
            serverFacade.joinGame(req, sessionAuthToken);
            GameplayUI gameplayUI = new GameplayUI(gameID, sessionAuthToken, serverFacade);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
