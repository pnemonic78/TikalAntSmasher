package com.tikalk.antsmasher.model;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Ant species.
 */

public class AntSpecies {

    private int id;
    private String name;
    @ColorInt
    private int tint = Color.BLACK;
    private final SparseArray<Ant> ants = new SparseArray<>();

    public AntSpecies() {
    }

    public AntSpecies(int id) {
        setId(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public SparseArray<Ant> getAnts() {
        return ants;
    }

    public List<Ant> getAllAnts() {
        final int size = ants.size();
        List<Ant> all = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            all.add(ants.valueAt(i));
        }
        return all;
    }

    public void setAnts(Collection<Ant> ants) {
        this.ants.clear();

        if (ants != null) {
            for (Ant ant : ants) {
                addAnt(ant);
            }
        }
    }

    public void addAnt(Ant ant) {
        this.ants.append(ant.getId(), ant);
    }
}
