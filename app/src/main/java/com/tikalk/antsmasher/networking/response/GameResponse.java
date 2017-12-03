package com.tikalk.antsmasher.networking.response;

import com.google.gson.annotations.SerializedName;

import com.tikalk.antsmasher.model.GameState;



public class GameResponse {

    @SerializedName("antId")
    public int id;
    @SerializedName("createdAt")
    public double createdAt;
    @SerializedName("updatedAt")
    public double updatedAt;
    @SerializedName("state")
    public GameState state;
    @SerializedName("startTime")
    public double startTime;
    @SerializedName("stopTime")
    public double stopTime;
    @SerializedName("pauseTime")
    public double pauseTime;
    @SerializedName("resumeTime")
    public double resumeTime;
    @SerializedName("finishTime")
    public double finishTime;
    @SerializedName("gameTime")
    public int gameTimeSeconds;

}
