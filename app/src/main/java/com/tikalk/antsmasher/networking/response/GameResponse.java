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
    public double createdAt;
    @SerializedName("updatedAt")
    public double updatedAt;
    @SerializedName("state")
    public GameState state;
    @SerializedName("startTime")
    public Double startTime;
    @SerializedName("stopTime")
    public Double stopTime;
    @SerializedName("pauseTime")
    public Double pauseTime;
    @SerializedName("resumeTime")
    public Double resumeTime;
    @SerializedName("finishTime")
    public Double finishTime;
    @SerializedName("gameTime")
    public int gameTimeSeconds;

}
