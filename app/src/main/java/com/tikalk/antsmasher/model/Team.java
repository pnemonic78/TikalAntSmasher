package com.tikalk.antsmasher.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.annotations.NonNull;

/**
 * Team with players.
 */

public class Team {

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("players")
    private final List<Player> players = new ArrayList<>();
    @SerializedName("antSpecies")
    private AntSpecies antSpecies;

    public Team(String id, String name) {
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

    public Map<String, Ant> getAnts() {
        return getAntSpecies().getAnts();
    }

    public List<Ant> getAllAnts() {
        return getAntSpecies().getAllAnts();
    }

    public boolean removeAnt(Ant ant) {
        return getAntSpecies().remove(ant);
    }
}
