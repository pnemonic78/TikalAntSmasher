package com.tikalk.antsmasher.model.socket;

import com.google.gson.annotations.SerializedName;

/**
 * Ant smash to server.
 */

public class AntSmash {

    @SerializedName("antId")
    public final String antId;
    @SerializedName("playerId")
    public final long playerId;
    @SerializedName("timestamp")
    public final long timestamp;

    public AntSmash(String antId, long playerId) {
        this(antId, playerId, System.currentTimeMillis());
    }

    public AntSmash(String antId, long playerId, long timestamp) {
        this.antId = antId;
        this.timestamp = timestamp;
        this.playerId = playerId;
    }
}
