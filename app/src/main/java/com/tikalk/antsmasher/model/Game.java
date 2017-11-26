package com.tikalk.antsmasher.model;

import com.google.gson.annotations.SerializedName;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Game with teams.
 */

public class Game {

    @SerializedName("id")
    private long id;
    @SerializedName("teams")
    private final List<Team> teams = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    GameState state = GameState.NOT_STARTED;

    @NonNull
    public List<Team> getTeams() {
        return teams;
    }

    @NonNull
    public List<Ant> getAllAnts() {
        final List<Ant> ants = new ArrayList<>();
        for (Team team : teams) {
            ants.addAll(team.getAllAnts());
        }
        return ants;
    }

    @NonNull
    public Map<String, Ant> getAnts() {
        final Map<String, Ant> ants = new HashMap<>();
        for (Team team : teams) {
            ants.putAll(team.getAnts());
        }
        return ants;
    }

    public Ant getAnt(String id) {
        return getAnts().get(id);
    }

    @Nullable
    public Ant addAnt(String antId, long speciesId) {
        AntSpecies species;
        for (Team team : teams) {
            species = team.getAntSpecies();
            if (speciesId == species.getId()) {
                Ant ant = new Ant(antId);
                species.add(ant);
                return ant;
            }
        }
        return null;
    }

    public boolean removeAnt(Ant ant) {
        for (Team team : teams) {
            if (team.remove(ant)) {
                return true;
            }
        }
        return false;
    }

    public boolean isSameTeam(String teamId, Ant ant) {
        for (Team team : getTeams()) {
            if (team.contains(ant)) {
                return true;
            }
        }
        return false;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public void setState(String state){
        this.state = GameState.valueOf(state);
    }
}
