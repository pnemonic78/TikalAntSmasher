package com.tikalk.antsmasher.model;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Team with players.
 */

public class Team implements Parcelable {

    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String name;
    @SerializedName("players")
    private List<Player> players;
    @SerializedName("antSpecies")
    private AntSpecies antSpecies;
    @SerializedName("score")
    private int score;

    public Team(long id, String name) {
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

    @NonNull
    public List<Player> getPlayers() {
        if (players == null) {
            players = new ArrayList<>();
        }
        return players;
    }

    public void setPlayers(@Nullable List<Player> players) {
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

    public Map<String, Ant> getAnts() {
        return getAntSpecies().getAnts();
    }

    public List<Ant> getAllAnts() {
        return getAntSpecies().getAllAnts();
    }

    public boolean remove(Ant ant) {
        return getAntSpecies().remove(ant);
    }

    public boolean contains(Ant ant) {
        return getAntSpecies().contains(ant);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", players=" + players +
                ", antSpecies=" + antSpecies +
                ", score=" + score +
                '}';
    }

    public Player getPlayer(long playerId) {
        for (Player player : getPlayers()) {
            if (player.getId() == playerId) {
                return player;
            }
        }
        return null;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeList(this.players);
        dest.writeParcelable(this.antSpecies, flags);
        dest.writeInt(this.score);
    }

    protected Team(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.players = new ArrayList<Player>();
        in.readList(this.players, Player.class.getClassLoader());
        this.antSpecies = in.readParcelable(AntSpecies.class.getClassLoader());
        this.score = in.readInt();
    }

    public static final Parcelable.Creator<Team> CREATOR = new Parcelable.Creator<Team>() {
        @Override
        public Team createFromParcel(Parcel source) {
            return new Team(source);
        }

        @Override
        public Team[] newArray(int size) {
            return new Team[size];
        }
    };

    public boolean contains(Player player) {
        return getPlayer(player.getId()) != null;
    }
}
