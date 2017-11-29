package com.tikalk.antsmasher.networking.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import com.tikalk.antsmasher.model.socket.HitSocketMessage;
import com.tikalk.antsmasher.model.socket.SocketMessage;

public class SocketMessageSerializer implements JsonSerializer<SocketMessage> {


    public static final String TAG = "TAG_SocketSerializer";

    @Override
    public JsonElement serialize(SocketMessage src, Type typeOfSrc, JsonSerializationContext serializer) {
        JsonObject message = new JsonObject();
        message.add("type", serializer.serialize(src.type));
        message.add("address", serializer.serialize(src.address));

        JsonObject body = null;

        if (src instanceof HitSocketMessage) {
            body = new JsonObject();
            body.addProperty("type", ((HitSocketMessage) src).antSmash.type);
            body.addProperty("antId", ((HitSocketMessage) src).antSmash.antId);
        }

        if (body != null) {
            message.add("body", body);
        }

        message.add("headers", new JsonObject());
        //  Log.i(TAG, "serialized message: " + message);

        return message;
    }

}