package com.tikalk.antsmasher.model.socket;

/**
 * Ant location from server.
 */

public class AntLocation {

    public final String antId;
    public final String speciesId;
    public final float xPercent;
    public final float yPercent;

    public AntLocation(String antId, String speciesId, float xPercent, float yPercent) {
        this.antId = antId;
        this.speciesId = speciesId;
        this.xPercent = xPercent;
        this.yPercent = yPercent;
    }

    @Override
    public String toString() {
        return "{id:" + antId + ", species:" + speciesId + ", x:" + xPercent + ", y:" + yPercent + "}";
    }
}
