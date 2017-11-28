package com.tikalk.antsmasher.model.socket;

import com.google.gson.annotations.Expose;

/**
 * Ant smash to server.
 */

public class AntSmash {

    public static final String HIT_TYPE = "hit";
    public static final String SELF_HIT_TYPE = "selfHit";
    public static final String MISS_TYPE = "miss";

    public final String antId;
    public final String type;

    @Expose(serialize = false, deserialize = true)
    public boolean smashedByUser = false;

    public AntSmash(String type, String id) {
        this.type = type;
        this.antId = id;
    }

    public AntSmash(String type, String id, boolean smashedByUser){
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
