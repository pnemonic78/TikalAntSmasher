package com.tikalk.antsmasher.model.socket;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
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
    public JsonElement body;

    public SocketMessage(String type, String address) {
        this(type, address, JsonNull.INSTANCE);
    }

    public SocketMessage(String type, String address, String body) {
        this(type, address, body == null ? JsonNull.INSTANCE : new JsonPrimitive(body));
    }

    public SocketMessage(String type, String address, JsonElement body) {
        this.type = type;
        this.address = address;
        this.body = body;
    }

    @NonNull
    @Override
    public String toString() {
        return "SocketMessage{" +
                "type='" + type + '\'' +
                ", address='" + address + '\'';
    }

    public String toSockJs() {
        return "[\"{\\\"type\\\":\\\"" + type + "\\\",\\\"address\\\":\\\"" + address + "\\\"";
    }
}
