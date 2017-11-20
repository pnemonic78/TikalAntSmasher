package com.tikalk.antsmasher.model;

import com.google.gson.annotations.SerializedName;


public class SmashSocketMessage extends SocketMessage {

    @SerializedName("body")
    private AntSmash antSmash;

    public SmashSocketMessage(String type, String address, AntSmash smash) {
        super(type, address);
        this.antSmash = smash;
    }

    public AntSmash getAntSmash() {
        return antSmash;
    }

    @Override
    public String toString() {
        return "SmashSocketMessage{" +
                "antSmash=" + antSmash +
                '}';
    }
}
