package com.tikalk.antsmasher.model.socket;

import com.google.gson.annotations.SerializedName;

import android.support.annotation.NonNull;

/**
 * Ant location from server.
 */

public class AntLocation {

    @SerializedName("antId")
    public final String antId;
    @SerializedName("species")
    public final long speciesId;
    @SerializedName("xRate")
    public final float xRate;
    @SerializedName("yRate")
    public final float yRate;

    public AntLocation(String antId, long speciesId, float xPercent, float yPercent) {
        this.antId = antId;
        this.speciesId = speciesId;
        this.xRate = xPercent;
        this.yRate = yPercent;
    }

    @NonNull
    @Override
    public String toString() {
        return "{id:" + antId + ", species:" + speciesId + ", x:" + xRate + ", y:" + yRate + "}";
    }
}
