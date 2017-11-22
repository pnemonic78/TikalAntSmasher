package com.tikalk.antsmasher.model.socket;

import com.google.gson.annotations.SerializedName;


public class SocketMessage<B> {

    public static final String TYPE_SEND = "send";
    public static final String TYPE_ERROR = "err";
    public static final String TYPE_REGISTER = "register";
    public static final String TYPE_UNREGISTER = "unregister";

    @SerializedName("type")
    public String type;
    @SerializedName("address")
    public String address;
    @SerializedName("body")
    public B body;

    public SocketMessage(String type, String address) {
        this(type, address, null);
    }

    public SocketMessage(String type, String address, B body) {
        this.type = type;
        this.address = address;
        this.body = body;
    }

    @Override
    public String toString() {
        return "SocketMessage{" +
                "type='" + type + '\'' +
                ", address='" + address + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
