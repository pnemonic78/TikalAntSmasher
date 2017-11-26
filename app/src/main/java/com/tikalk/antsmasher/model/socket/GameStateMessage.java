package com.tikalk.antsmasher.model.socket;

import com.tikalk.antsmasher.model.GameState;

public class GameStateMessage extends SocketMessage<GameState> {

    public GameStateMessage(String type, String address, GameState state) {
        super(type, address, state);
    }
}
