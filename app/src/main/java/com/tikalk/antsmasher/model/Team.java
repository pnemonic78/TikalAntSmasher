package com.tikalk.antsmasher.model;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * Team with players.
 */

public class Team {

    private int id;
    private String name;
    private final List<Player> players = new ArrayList<>();
    private AntSpecies antSpecies;

    public Team(int id, String name) {
        setId(id);
        setName(name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public SparseArray<Ant> getAnts() {
        return getAntSpecies().getAnts();
    }

    public List<Ant> getAllAnts() {
        return getAntSpecies().getAllAnts();
    }
}
