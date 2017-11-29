package com.tikalk.antsmasher.model.socket;

import com.google.gson.annotations.SerializedName;

import com.tikalk.antsmasher.model.GameState;

public class GameStateBody extends SocketMessage {

    @SerializedName("state")
    private GameState state;

    public GameStateBody(String type, String address, GameState state) {
        super(type, address);
        this.state = state;
    }

    public GameState getState() {
        return state;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", state=" + state;
    }
}
