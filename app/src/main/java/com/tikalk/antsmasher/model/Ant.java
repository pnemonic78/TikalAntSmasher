package com.tikalk.antsmasher.model;

import android.graphics.RectF;

/**
 * Ant.
 */

public class Ant {

    private long id;
    private final RectF location = new RectF();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public RectF getLocation() {
        return location;
    }

    public void setLocation(float x, float y) {
        final float width = location.width();
        final float height = location.height();
        location.set(x, y, x + width, y + height);
    }
}
