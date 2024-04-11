package ui;

import webSocketMessages.serverMessages.LoadGameMessage;

public interface GameHandler {
    void updateGame(LoadGameMessage message);
    void printMessage(String message);
}
