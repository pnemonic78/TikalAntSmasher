package com.tikalk.antsmasher.model.socket;

import com.tikalk.antsmasher.model.Game;

public class GameStateMessage extends SocketMessage<String> {


    public GameStateMessage(String type, String address, String state) {
        super(type, address, state);
    }
}
