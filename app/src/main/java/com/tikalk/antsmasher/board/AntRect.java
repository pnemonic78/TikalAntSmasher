package com.tikalk.antsmasher.board;

import android.graphics.RectF;

/**
 * Ant rectangle.
 */

public class AntRect extends RectF {

    public int id;
    public float angle;
    public int speciesId;

    public boolean isHit(float x, float y) {
        return contains(x, y);
    }
}
