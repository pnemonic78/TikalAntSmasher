package com.tikalk.antsmasher.model.socket;

import com.google.gson.annotations.SerializedName;

import android.support.annotation.NonNull;


public class SocketMessage<B> {

    public static final String TYPE_SEND = "send";
    public static final String TYPE_ERROR = "err";
    public static final String TYPE_REGISTER = "register";
    public static final String TYPE_HIT = "hit";

    public static final String HIT_TRAIL_ADDRESS = "hit-trial-message";
    public static final String SMASH_MESSAGE_ADDRESS = "smash-message";
    public static final String PLAY_SCORE_ADDRESS = "playerScore-message";
    public static final String TEAM_SCORE_ADDRESS = "teamScore-message";

    @SerializedName("type")
    public String type;
    @SerializedName("address")
    public String address;

    public SocketMessage(String type, String address) {
        this(type, address, null);
    }

    public SocketMessage(String type, String address, String body) {
        this.type = type;
        this.address = address;
    }

    @NonNull
    @Override
    public String toString() {
        return "SocketMessage{" +
                "type='" + type + '\'' +
                ", address='" + address + '\'';
    }
}
