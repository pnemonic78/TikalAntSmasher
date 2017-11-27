package com.tikalk.antsmasher.model.socket;

import com.google.gson.annotations.SerializedName;

import android.support.annotation.NonNull;


public class SocketMessage {

    public static final String TYPE_SEND = "send";
    public static final String TYPE_ERROR = "err";
    public static final String TYPE_REGISTER = "register";
    public static final String TYPE_RECORD = "rec";

    @SerializedName("type")
    public String type;
    @SerializedName("address")
    public String address;
    @SerializedName("body")
    public String body;

    public SocketMessage(String type, String address) {
        this(type, address, null);
    }

    public SocketMessage(String type, String address, String body) {
        this.type = type;
        this.address = address;
        this.body = body;
    }

    @NonNull
    @Override
    public String toString() {
        return "SocketMessage{" +
                "type='" + type + '\'' +
                ", address='" + address + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
