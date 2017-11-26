package com.tikalk.antsmasher.model.socket;

import com.google.gson.annotations.SerializedName;

import android.support.annotation.NonNull;

/**
 * Ant location from server.
 */

public class AntLocation {

    @SerializedName("id")
    public final String antId;
    @SerializedName("species")
    public final long speciesId;
    @SerializedName("xPromil")
    public final int xPromil;
    @SerializedName("yPromil")
    public final int yPromil;

    public AntLocation(String antId, long speciesId, float xPercent, float yPercent) {
        this.antId = antId;
        this.speciesId = speciesId;
        this.xPromil = (int) (xPercent * 1000);
        this.yPromil = (int) (yPercent * 1000);
    }

    @NonNull
    @Override
    public String toString() {
        return "{id:" + antId + ", species:" + speciesId + ", x:" + xPromil + ", y:" + yPromil + "}";
    }
}
