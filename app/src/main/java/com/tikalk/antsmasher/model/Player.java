package com.tikalk.antsmasher.model;

import android.graphics.drawable.Drawable;

/**
 * Player.
 */

public class Player {

    private long id;
    private String name;
    private String avatarUri;

    public Player(String name) {
        setName(name);
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
}
