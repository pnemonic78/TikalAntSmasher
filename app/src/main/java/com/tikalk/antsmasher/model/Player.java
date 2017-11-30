package com.tikalk.antsmasher.model;

import com.google.gson.annotations.SerializedName;

/**
 * Player.
 */

public class Player extends User {

    @SerializedName("score")
    private int score;
    @SerializedName("teamName")
    private String teamName;

    public Player(String name) {
        super(name);
    }

    public Player(long id, String name) {
        super(id, name);
    }

    public Player(long id, String name, int score, String teamName) {
        super(id, name);
        setScore(score);
        setTeamName(teamName);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
