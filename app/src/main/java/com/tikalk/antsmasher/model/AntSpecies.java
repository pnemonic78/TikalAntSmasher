package com.tikalk.antsmasher.model;

import com.google.gson.annotations.SerializedName;

import android.graphics.Color;
import android.support.annotation.ColorInt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Ant species with ants.
 */

public class AntSpecies {

    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String name;
    @ColorInt
    @SerializedName("color")
    private int tint = Color.BLACK;
    @SerializedName("size")
    private float size = 1;
    @SerializedName("speed")
    private float speed = 1;
    private final Map<String, Ant> antsById = new HashMap<>();
    private final List<Ant> ants = new ArrayList<>();

    public AntSpecies() {
        this(0);
    }

    public AntSpecies(long id) {
        setId(id);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ColorInt
    public int getTint() {
        return tint;
    }

    public void setTint(@ColorInt int tint) {
        this.tint = tint;
    }

    public Map<String, Ant> getAnts() {
        return antsById;
    }

    public List<Ant> getAllAnts() {
        return ants;
    }

    public void setAnts(Collection<Ant> ants) {
        this.ants.clear();

        if (ants != null) {
            for (Ant ant : ants) {
                add(ant);
            }
        }
    }

    public void add(Ant ant) {
        ant.setSpecies(this);
        ants.add(ant);
        antsById.put(ant.getId(), ant);
    }

    public boolean remove(Ant ant) {
        ant.setSpecies(null);
        final String id = ant.getId();
        if (antsById.containsKey(id)) {
            ants.remove(ant);
            antsById.remove(id);
            return true;
        }
        return false;
    }

    public boolean contains(Ant ant) {
        return antsById.containsKey(ant.getId());
    }
}
