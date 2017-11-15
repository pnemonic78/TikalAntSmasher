package com.tikalk.antsmasher.model;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author moshe on 2017/11/15.
 */

public class Team {

    private long id;
    private String name;
    private Drawable icon;
    private final List<Player> players = new ArrayList<>();

    public Team(long id, String name) {
        this(id, name, (Drawable) null);
    }

    public Team(long id, String name, Drawable icon) {
        setId(id);
        setName(name);
        setIcon(icon);
    }

    public Team(long id, String name, Bitmap icon) {
        this(id, name, new BitmapDrawable(icon));
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

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players.clear();
        if (players != null) {
            this.players.addAll(players);
        }
    }
}
