package com.tikalk.antsmasher.model;

/**
 * Ant location.
 */

public class AntLocation {

    public final String id;
    public final float x;
    public final float y;

    public AntLocation(String id, float x, float y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }
}
