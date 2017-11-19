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

    public boolean isHit(float x, float y) {
        return contains(x, y);
    }

    public boolean isVisible(float screenWidth, float screenHeight) {
        return (left >= 0) && (left < screenWidth) && (top >= 0) && (top < screenHeight);
    }

    private void calculateHeading(final float x2, final float y2) {
        final float x1 = left;
        final float y1 = top;
        final float dx = x2 - x1;
        final float dy = y2 - y1;
        final double theta = Math.atan2(dy, dx) * RADIANS_TO_DEGREES;
//        this.angle = (float) (90.0 - theta);
    }

    /**
     * Move the ant to another target location.
     *
     * @param left the horizontal coordinate.
     * @param top  the vertical coordinate.
     */
    public void set(float left, float top) {
        final float w = width();
        final float h = height();
        set(left, top, left + w, top + h);
    }

    @Override
    public void offset(float dx, float dy) {
        final float x2 = left + dx;
        final float y2 = top + dy;
        calculateHeading(x2, y2);
        super.offset(dx, dy);
    }

    @Override
    public void set(float left, float top, float right, float bottom) {
        final float x2 = left;
        final float y2 = top;
        calculateHeading(x2, y2);
        super.set(left, top, right, bottom);
    }
}
