package com.tikalk.antsmasher.model.socket;

import com.google.gson.annotations.Expose;
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

    @Expose(serialize = false, deserialize = true)
    public boolean smashedByUser;

    public AntSmash(String type, String id, long playerId) {
        this(type, id, playerId, false);
    }

    public AntSmash(String type, String id, long playerId, boolean smashedByUser) {
        this.type = type;
        this.antId = id;
        this.playerId = playerId;
        this.smashedByUser = smashedByUser;
    }

    @Override
    public String toString() {
        return "AntSmash{" +
                "antId='" + antId + '\'' +
                ", playerId='" + playerId + '\'' +
                ", type='" + type + '\'' +
                ", smashedByUser=" + smashedByUser +
                '}';
    }
}
