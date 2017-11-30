package com.tikalk.antsmasher.model.socket;

import com.google.gson.annotations.SerializedName;

/**
 * @author moshe on 2017/11/30.
 */

public class TeamScore {

    @SerializedName("teamId")
    public final long teamId;
    @SerializedName("score")
    public final int score;

    public TeamScore(long teamId, int score) {
        this.teamId = teamId;
        this.score = score;
    }
}
