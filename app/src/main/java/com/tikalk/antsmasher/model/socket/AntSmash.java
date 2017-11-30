package com.tikalk.antsmasher.model.socket;

import com.google.gson.annotations.SerializedName;

/**
 * Ant smash to server.
 */

public class AntSmash {

    public static final String TYPE_HIT = "hit";
    public static final String TYPE_SELF_HIT = "selfHit";
    public static final String TYPE_MISS = "miss";

    @SerializedName("antId")
    public final String antId;
    @SerializedName("playerId")
    public final long playerId;
    @SerializedName("type")
    public final String type;

    public AntSmash(String type, String id, long playerId) {
        this.type = type;
        this.antId = id;
        this.playerId = playerId;
    }

    @Override
    public String toString() {
        return "AntSmash{" +
                "antId='" + antId + '\'' +
                ", playerId='" + playerId + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
