package com.tikalk.antsmasher.networking.gson_deserializers;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import com.tikalk.antsmasher.model.GameState;
import com.tikalk.antsmasher.model.socket.GameStateMessage;

import java.lang.reflect.Type;

/**
 * Created by motibartov on 26/11/2017.
 */

public class GameStateDeserializer implements JsonDeserializer<GameStateMessage> {

    private static final String TAG = "TAG_StateDeserializer";


    @Override
    public GameStateMessage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject jObject = json.getAsJsonObject();
        JsonObject body = jObject.getAsJsonObject("body");

        Log.i(TAG, "deserialize: " + body.toString());
        return new GameStateMessage(jObject.get("type").getAsString(),
                jObject.get("address").getAsString(),
                GameState.valueOf(body.get("state").getAsString()));
    }
}
