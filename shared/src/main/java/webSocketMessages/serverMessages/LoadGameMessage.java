package webSocketMessages.serverMessages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage {
    private ChessGame game;
    public LoadGameMessage(ServerMessageType messageType, ChessGame game) {
        super(messageType);
        this.game = game;
    }

    public ChessGame getGame() {
        return this.game;
    }
}
