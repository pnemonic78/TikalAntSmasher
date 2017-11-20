package com.tikalk.antsmasher.model;

/**
 * Player.
 */

public class Player {

    private String id;
    private String name;
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
