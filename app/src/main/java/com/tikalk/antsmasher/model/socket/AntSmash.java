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
    @SerializedName("type")
    public final String type;

    @Expose(serialize = false, deserialize = true)
    public boolean smashedByUser;

    public AntSmash(String type, String id) {
        this(type, id, false);
    }

    public AntSmash(String type, String id, boolean smashedByUser) {
        this.type = type;
        this.antId = id;
        this.smashedByUser = smashedByUser;
    }

    @Override
    public String toString() {
        return "AntSmash{" +
                "antId='" + antId + '\'' +
                ", type='" + type + '\'' +
                ", smashedByUser=" + smashedByUser +
                '}';
    }
}
