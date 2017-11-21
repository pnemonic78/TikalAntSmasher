package com.tikalk.antsmasher.model;

import android.graphics.Color;
import android.support.annotation.ColorInt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Ant species.
 */

public class AntSpecies {

    private String id;
    private String name;
    @ColorInt
    private int tint = Color.BLACK;
    private final Map<String, Ant> antsById = new HashMap<>();
    private final List<Ant> ants = new ArrayList<>();

    public AntSpecies() {
        this(null);
    }

    public AntSpecies(String id) {
        setId(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
        ants.add(ant);
        antsById.put(ant.getId(), ant);
    }

    public void remove(Ant ant) {
        final String id = ant.getId();
        if (antsById.containsKey(id)) {
            ants.remove(ant);
            antsById.remove(id);
        }
    }
}
