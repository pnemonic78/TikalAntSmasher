package com.tikalk.antsmasher.model;

/**
 * Ant location.
 */

public class AntLocation {

    public final String id;
    public final float xPercent;
    public final float yPercent;

    public AntLocation(String id, float xPercent, float yPercent) {
        this.id = id;
        this.xPercent = xPercent;
        this.yPercent = yPercent;
    }
}
