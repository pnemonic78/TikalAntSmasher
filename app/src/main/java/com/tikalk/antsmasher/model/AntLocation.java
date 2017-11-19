package com.tikalk.antsmasher.model;

/**
 * Ant location.
 */

public class AntLocation {

    public final int id;
    public final float xPercent;
    public final float yPercent;

    public AntLocation(int id, float xPercent, float yPercent) {
        this.id = id;
        this.xPercent = xPercent;
        this.yPercent = yPercent;
    }
}
