package com.tikalk.antsmasher.model;

/**
 * Ant location.
 */

public class AntLocation {

    public final int id;
    public final String speciesId;
    public final float xPercent;
    public final float yPercent;

    public AntLocation(int id, String speciesId, float xPercent, float yPercent) {
        this.id = id;
        this.speciesId = speciesId;
        this.xPercent = xPercent;
        this.yPercent = yPercent;
    }
}
