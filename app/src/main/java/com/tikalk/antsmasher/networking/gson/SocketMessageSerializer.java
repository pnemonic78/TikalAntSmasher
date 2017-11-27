package com.tikalk.antsmasher.networking.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import com.tikalk.antsmasher.model.socket.SocketMessage;

public class SocketMessageSerializer implements JsonSerializer<SocketMessage> {

    @Override
    public JsonElement serialize(SocketMessage src, Type typeOfSrc, JsonSerializationContext serializer) {
        JsonObject message = new JsonObject();
        message.add("type", serializer.serialize(src.type));
        message.add("address", serializer.serialize(src.address));
        if (src.body != null) {
            message.add("body", serializer.serialize(src.body));
        }
        message.add("headers", new JsonObject());
        return message;
    }

}