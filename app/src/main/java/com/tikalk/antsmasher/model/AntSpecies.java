package com.tikalk.antsmasher.model;

import android.graphics.drawable.Drawable;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Ant species.
 */

public class AntSpecies {

    private long id;
    private String name;
    private String imageUri;
    private Drawable image;
    private final SparseArray<Ant> ants = new SparseArray<>();

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

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
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
