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

    @SerializedName("score")
    private long score;

    @SerializedName("teamName")
    private String teamName;

    public Player(String name) {
        this(0, name);
    }

    public Player(long id, String name) {
        setId(id);
        setName(name);
    }

    public Player(long id, String name, long score, String teamName) {
        setId(id);
        setName(name);
        setAvatarUri(null);
        setScore(score);
        setTeamName(teamName);
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


    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
