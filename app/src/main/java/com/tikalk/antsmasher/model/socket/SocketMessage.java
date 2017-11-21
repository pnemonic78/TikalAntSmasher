package com.tikalk.antsmasher.model.socket;

import com.google.gson.annotations.SerializedName;


public class SocketMessage {

    public static final String TYPE_SEND = "send";
    public static final String TYPE_ERROR = "err";
    public static final String TYPE_REGISTER = "register";
    public static final String TYPE_UNREGISTER = "unregister";

    @SerializedName("type")
    public String type;
    @SerializedName("address")
    public String address;

    public SocketMessage(String type, String address) {
        this.type = type;
        this.address = address;
    }

    @Override
    public String toString() {
        return "SocketMessage{" +
                "type='" + type + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public String toSockJs() {
        return "[\"{\\\"type\\\":\\\"" + type + "\\\",\\\"address\\\":\\\"" + address + "\\\"";
    }
}
