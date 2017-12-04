package com.tikalk.antsmasher.model;

import com.google.gson.annotations.SerializedName;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Player.
 */

public class Player extends User implements Parcelable {

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

    protected Player(Parcel in) {
        super(in);
        score = in.readInt();
        teamName = in.readString();
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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(score);
        dest.writeString(teamName);
    }

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };
}
