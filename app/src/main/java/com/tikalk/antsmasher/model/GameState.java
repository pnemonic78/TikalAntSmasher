package com.tikalk.antsmasher.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by motibartov on 26/11/2017.
 */

public enum GameState {
    @SerializedName("NOT_STARTED")
    NOT_STARTED,
    @SerializedName("STARTED")
    STARTED,
    @SerializedName("STOPPED")
    STOPPED,
    @SerializedName("PAUSED")
    PAUSED,
    @SerializedName("RESUMED")
    RESUMED,
    @SerializedName("FINISH")
    FINISHED
}
