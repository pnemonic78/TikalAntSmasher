package com.tikalk.antsmasher.model;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * Team with players.
 */

public class Team {

    private long id;
    private String name;
    private String avatarUri;
    private Drawable avatar;
    private final List<Player> players = new ArrayList<>();
    private AntSpecies antSpecies;

    public Team(long id, String name) {
        this(id, name, (Drawable) null);
    }

    public Team(long id, String name, Drawable avatar) {
        setId(id);
        setName(name);
        setAvatar(avatar);
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

    public String getAvatarUri() {
        return avatarUri;
    }

    public void setAvatarUri(String avatarUri) {
        this.avatarUri = avatarUri;
    }

    public Drawable getAvatar() {
        return avatar;
    }

    public void setAvatar(Drawable icon) {
        this.avatar = icon;
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

    @NonNull
    public AntSpecies getAntSpecies() {
        if (antSpecies == null) {
            antSpecies = new AntSpecies();
        }
        return antSpecies;
    }

    public void setAntSpecies(AntSpecies antSpecies) {
        this.antSpecies = antSpecies;
    }

    public List<Ant> getAnts() {
        return getAntSpecies().getAnts();
    }
}
