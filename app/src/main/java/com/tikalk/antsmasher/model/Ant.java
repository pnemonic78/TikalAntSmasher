package com.tikalk.antsmasher.model;

import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Ant.
 */

public class Ant implements Comparable<Ant> {

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

    @NonNull
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
        return (location.x >= 0f) && (location.x <= 1f) && (location.y >= 0f) && (location.y <= 1f);
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public int compareTo(@NonNull Ant that) {
        return this.id.compareTo(that.id);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Ant) {
            return compareTo((Ant) obj) == 0;
        }
        return super.equals(obj);
    }
}
