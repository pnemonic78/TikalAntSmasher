package com.tikalk.antsmasher.model.socket;

import com.google.gson.annotations.SerializedName;

/**
 * @author moshe on 2017/11/30.
 */

public class PlayerScore {

    @SerializedName("playerId")
    public final long playerId;
    @SerializedName("score")
    public final int score;

    public PlayerScore(long playerId, int score) {
        this.playerId = playerId;
        this.score = score;
    }
}
