package com.tikalk.antsmasher.model;

import com.google.gson.annotations.SerializedName;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Ant species with ants.
 */

public class AntSpecies implements Parcelable {

    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String name;
    @ColorInt
    @SerializedName("color")
    private int tint = Color.TRANSPARENT;
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
        if (tint == Color.TRANSPARENT) {
            switch ((int) id) {
                case 1:
                    tint = 0xFFcc0000;
                    break;
                case 2:
                    tint = 0xFF00cc00;
                    break;
                case 3:
                    tint = Color.BLACK;
                    break;
            }
        }
        return tint;
    }

    public void setTint(@ColorInt int tint) {
        this.tint = tint;
    }

    @NonNull
    public Map<String, Ant> getAnts() {
        return antsById;
    }

    @NonNull
    public List<Ant> getAllAnts() {
        return ants;
    }

    public void setAnts(@Nullable Collection<Ant> ants) {
        this.ants.clear();

        if (ants != null) {
            for (Ant ant : ants) {
                add(ant);
            }
        }
    }

    public void add(@NonNull Ant ant) {
        ant.setSpecies(this);
        ants.add(ant);
        antsById.put(ant.getId(), ant);
    }

    public boolean remove(@NonNull Ant ant) {
        ant.setSpecies(null);
        final String id = ant.getId();
        if (antsById.containsKey(id)) {
            ants.remove(ant);
            antsById.remove(id);
            return true;
        }
        return false;
    }

    public boolean contains(@NonNull Ant ant) {
        return antsById.containsKey(ant.getId());
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.tint);
        dest.writeFloat(this.size);
    }

    protected AntSpecies(Parcel in) {
        this.name = in.readString();
        this.tint = in.readInt();
        this.size = in.readFloat();
    }

    public static final Parcelable.Creator<AntSpecies> CREATOR = new Parcelable.Creator<AntSpecies>() {
        @Override
        public AntSpecies createFromParcel(Parcel source) {
            return new AntSpecies(source);
        }

        @Override
        public AntSpecies[] newArray(int size) {
            return new AntSpecies[size];
        }
    };
}
