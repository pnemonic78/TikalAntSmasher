package com.tikalk.antsmasher.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Game with teams.
 */

public class Game {

    private String id;
    private final List<Team> teams = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Ant addAnt(String antId, String speciesId) {
        AntSpecies species;
        for (Team team : teams) {
            species = team.getAntSpecies();
            if (speciesId.equals(species.getId())) {
                Ant ant = new Ant(antId);
                species.add(ant);
                return ant;
            }
        }
        return null;
    }

    public boolean removeAnt(Ant ant) {
        for (Team team : teams) {
            if (team.removeAnt(ant)) {
                return true;
            }
        }
        return false;
    }
}
