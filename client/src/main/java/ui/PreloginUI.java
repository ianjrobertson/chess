package ui;

import ServerFacade.ServerFacade;
import ServerFacade.ServiceRecords.*;
import java.util.Scanner;

public class PreloginUI {

    //Read, eval, Print, loop.
    private boolean running;
    private ServerFacade serverFacade;

    public PreloginUI(String url) {
        running = true;
        serverFacade = new ServerFacade(url);
    }

    public void run() {
        System.out.println("Type \"help\" to get started");
        Scanner scanner = new Scanner(System.in);
        while (running) {
            String input = scanner.nextLine();
            String command = getFirstWord(input);
            switch(command) {
                case("help") -> this.help();
                case("quit") -> this.quit();
                case("login") -> this.login(input);
                case("register") -> this.register(input);
                case null, default -> this.unknownInput();
            }
        }
        scanner.close();
    }

    public static String getFirstWord(String line) {
        String[] words = line.trim().split("\\s+");
        if (words.length > 0) {
            return words[0];
        } else {
            return ""; // or throw an exception, depending on your requirements
        }
    }

    private void unknownInput() {
        System.out.println("Unknown input, please try again");
    }

    /**
     * Displays text letting the user know what options they can take
     */
    private void help() {
        System.out.println("register <USERNAME> <PASSWORD> <EMAIL> - to create an account");
        System.out.println("login <USERNAME> <PASSWORD> - to play chess");
        System.out.println("quit - exit game");
        System.out.println("help - list options");
    }



    private void quit() {
        running = false;
        System.out.println("Have a nice day! :)");
    }

    private void login(String input) {
        String[] words = input.trim().split("\\s+");
        String username = words[1];
        String password = words[2];

        try {
            LoginResponse response = serverFacade.login(new LoginRequest(username, password));
            //System.out.println(response);
            this.postLogin(response.authToken());
        }
        catch (Exception e){
            System.out.println("Login failed"); //probably need to figure out how to display status code
        }
    }

    private void register(String input) {
        String[] words = input.trim().split("\\s+");
        String username = words[1];
        String password = words[2];
        String email = words[3];

        try {
            RegisterResponse response = serverFacade.register(new RegisterRequest(username, password, email));
            System.out.println(response);
            this.postLogin(response.authToken());
        }
        catch (Exception e) {
            System.out.println("Register failed");
        }
        // have a server facade class, call the register method on it with a register request.
    }

    private void postLogin(String authToken) {
        PostloginUI postloginUI = new PostloginUI(this.serverFacade, authToken);
        postloginUI.run();
        //Once we have exited the scope of postLogin, we can quit here too
    }
}
