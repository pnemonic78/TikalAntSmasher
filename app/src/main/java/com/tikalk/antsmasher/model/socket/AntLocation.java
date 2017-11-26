package com.tikalk.antsmasher.model.socket;

import android.support.annotation.NonNull;

/**
 * Ant location from server.
 */

public class AntLocation {

    public final String antId;
    public final long speciesId;
    public final float xPercent;
    public final float yPercent;

    public AntLocation(String antId, long speciesId, float xPercent, float yPercent) {
        this.antId = antId;
        this.speciesId = speciesId;
        this.xPercent = xPercent;
        this.yPercent = yPercent;
    }

    @NonNull
    @Override
    public String toString() {
        return "{id:" + antId + ", species:" + speciesId + ", x:" + xPercent + ", y:" + yPercent + "}";
    }
}
