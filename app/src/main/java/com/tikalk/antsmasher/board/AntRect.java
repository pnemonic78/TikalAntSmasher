package com.tikalk.antsmasher.board;

import android.graphics.RectF;

/**
 * Ant rectangle.
 */

public class AntRect extends RectF {

    private static final float HEADING_DOWN = 0f;
    private static final double RADIANS_TO_DEGREES = 180.0 / Math.PI;

    public int id;
    public float angle = HEADING_DOWN;
    public int speciesId;
    public boolean alive = true;

    public boolean isHit(float x, float y) {
        return alive && contains(x, y);
    }

    public boolean isVisible(float screenWidth, float screenHeight) {
        return (left >= 0) && (left < screenWidth) && (top >= 0) && (top < screenHeight);
    }

    private void calculateHeading(final float x2, final float y2) {
        final float x1 = x();
        final float y1 = y();
        final float dx = x2 - x1;
        final float dy = y2 - y1;
        final double theta = Math.atan2(dy, dx) * RADIANS_TO_DEGREES;
        this.angle = (float) (270f + theta);
    }

    /**
     * Move the ant to another target location.
     *
     * @param x the horizontal coordinate, in the middle of the ant.
     * @param y the vertical coordinate, in the middle of the ant.
     */
    public void moveTo(float x, float y) {
        final float w2 = width() / 2;
        final float h2 = height() / 2;
        final float x1 = x - w2;
        final float y1 = y - h2;
        calculateHeading(x, y);
        offsetTo(x1, y1);
    }

    public float x() {
        return left + (width() / 2);
    }

    public float y() {
        return top + (height() / 2);
    }
}
