package com.tikalk.antsmasher.model;

import com.google.gson.annotations.SerializedName;

/**
 * Player.
 */

public class Player {

    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String name;
    @SerializedName("avatar")
    private String avatarUri;

    public Player(String name) {
        this(0, name);
    }

    public Player(long id, String name) {
        setId(id);
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
