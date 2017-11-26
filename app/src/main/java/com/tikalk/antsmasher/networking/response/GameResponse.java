package com.tikalk.antsmasher.networking.response;

import com.google.gson.annotations.SerializedName;

import com.tikalk.antsmasher.model.GameState;

/**
 * Created by motibartov on 26/11/2017.
 */

public class GameResponse {

    @SerializedName("id")
    public int id;
    @SerializedName("createdAt")
    public long createdAt;
    @SerializedName("updatedAt")
    public long updatedAt;
    @SerializedName("state")
    public GameState state;
    @SerializedName("startTime")
    public Long startTime;
    @SerializedName("stopTime")
    public Long stopTime;
    @SerializedName("pauseTime")
    public Long pauseTime;
    @SerializedName("resumeTime")
    public Long resumeTime;
    @SerializedName("finishTime")
    public Long finishTime;
    @SerializedName("gameTime")
    public int gameTimeSeconds;

}
