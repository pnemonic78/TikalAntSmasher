package com.tikalk.antsmasher.model;

import android.graphics.PointF;
import android.graphics.RectF;

/**
 * Ant.
 */

public class Ant {

    private static final float HEADING_DOWN = 0f;
    private static final double RADIANS_TO_DEGREES = 180.0 / Math.PI;

    private int id;
    private final RectF location = new RectF();
    private final PointF endLocation = new PointF();
    private float headingDegrees = HEADING_DOWN;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RectF getLocation() {
        return location;
    }

    public PointF getEndLocation() {
        return endLocation;
    }

    public void setLocation(float x, float y) {
        final float width2 = location.width() / 2;
        final float height2 = location.height() / 2;
        location.set(x - width2, y - width2, x + width2, y + height2);
        calculateHeading();
    }

    public void setSize(float width, float height) {
        final float x = location.left + (location.width() / 2);
        final float y = location.top + (location.height() / 2);
        final float width2 = width / 2;
        final float height2 = height / 2;
        location.set(x - width2, y - width2, x + width2, y + height2);
        calculateHeading();
    }

    /**
     * Get the heading direction.
     * The default heading is down toward the bottom of the board.
     *
     * @return the angle, in degrees.
     */
    public float getHeading() {
        return headingDegrees;
    }

    private void calculateHeading() {
        final float x1 = location.left + (location.width() / 2);
        final float y1 = location.top + (location.height() / 2);
        final float x2 = endLocation.x;
        final float y2 = endLocation.y;
        final float dx = x2 - x1;
        final float dy = y2 - y1;
        final double theta = Math.atan2(dy, dx) * RADIANS_TO_DEGREES;
        this.headingDegrees = (float) (90.0 - theta);
    }

    /**
     * Move the and to another target location.
     *
     * @param x the horizontal coordinate.
     * @param y the vertical coordinate.
     */
    public void moveTo(float x, float y) {
        endLocation.set(x, y);
        calculateHeading();
    }

    public boolean isHit(float x, float y) {
        return location.contains(x, y);
    }
}
