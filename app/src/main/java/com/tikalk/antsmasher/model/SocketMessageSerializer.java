package com.tikalk.antsmasher.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;


public class SocketMessageSerializer implements JsonSerializer<SocketMessage> {

    @Override
    public JsonElement serialize(SocketMessage src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject user = new JsonObject();
        user.add("type", context.serialize(src.type));
        user.add("address", context.serialize(src.address));

        JsonObject body = null;

        if (body != null) {
            user.add("body", body);
        }
        user.add("headers", new JsonObject());
        return user;
    }

}