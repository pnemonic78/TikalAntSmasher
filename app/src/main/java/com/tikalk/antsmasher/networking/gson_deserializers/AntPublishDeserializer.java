package com.tikalk.antsmasher.networking.gson_deserializers;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import com.tikalk.antsmasher.model.AntSpecies;
import com.tikalk.antsmasher.model.GameState;
import com.tikalk.antsmasher.model.socket.AntLocation;
import com.tikalk.antsmasher.model.socket.AntLocationMessage;
import com.tikalk.antsmasher.model.socket.GameStateMessage;

import java.lang.reflect.Type;

/**
 * Created by motibartov on 26/11/2017.
 */

public class AntPublishDeserializer implements JsonDeserializer<AntLocationMessage> {


    @Override
    public AntLocationMessage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject jObject = json.getAsJsonObject();
        JsonObject body = jObject.getAsJsonObject("body");

        return new AntLocationMessage(jObject.get("type").getAsString(),
                jObject.get("address").getAsString(),
                new AntLocation(body.get("id").getAsString()
                        , body.get("species").getAsLong()
                        , body.get("xRate").getAsFloat()
                        , body.get("yRate").getAsFloat()));
    }
}
