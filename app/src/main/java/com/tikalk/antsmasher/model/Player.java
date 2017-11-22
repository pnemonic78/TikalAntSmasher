package com.tikalk.antsmasher.model;

import com.google.gson.annotations.SerializedName;

/**
 * Player.
 */

public class Player {

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("avatar")
    private String avatarUri;

    public Player(String name) {
        this(null, name);
    }

    public Player(String id, String name) {
        setId(id);
        setName(name);
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

    public String getAvatarUri() {
        return avatarUri;
    }

    public void setAvatarUri(String avatarUri) {
        this.avatarUri = avatarUri;
    }
}
