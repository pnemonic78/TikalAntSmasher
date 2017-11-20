package com.tikalk.antsmasher.model;

import android.graphics.PointF;

/**
 * Ant.
 */

public class Ant {

    private String id;
    private final PointF location = new PointF();
    private AntSpecies species;
    private boolean alive = true;

    public Ant() {
    }

    public Ant(String id) {
        setId(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PointF getLocation() {
        return location;
    }

    public void setLocation(float x, float y) {
        location.set(x, y);
    }

    public AntSpecies getSpecies() {
        return species;
    }

    public void setSpecies(AntSpecies species) {
        this.species = species;
    }

    public boolean isVisible() {
        return (location.x >= 0f) && (location.x < 1f) && (location.y >= 0f) && (location.y < 1f);
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
