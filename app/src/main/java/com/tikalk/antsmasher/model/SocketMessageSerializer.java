package com.tikalk.antsmasher.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;


public class SocketMessageSerializer implements JsonSerializer<SocketMessage> {

    @Override
    public JsonElement serialize(SocketMessage src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject message = new JsonObject();
        message.add("type", context.serialize(src.type));
        message.add("address", context.serialize(src.address));

        JsonObject body;

        if (src instanceof SmashSocketMessage) {
            body = new JsonObject();
            body.addProperty("antId" , ((SmashSocketMessage)src).getAntSmash().id);
            body.addProperty("timestamp" , ((SmashSocketMessage)src).getAntSmash().timestamp);
            message.add("body", body);
        }

        message.add("headers", new JsonObject());
        return message;
    }

}