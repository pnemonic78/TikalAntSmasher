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

    @SerializedName("antId")
    private long id;
    @SerializedName("teams")
    private List<Team> teams;
    @SerializedName("state")
    private GameState state = GameState.NOT_STARTED;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public List<Team> getTeams() {
        if (teams == null) {
            teams = new ArrayList<>();
        }
        return teams;
    }

    @NonNull
    public List<Ant> getAllAnts() {
        final List<Ant> ants = new ArrayList<>();
        for (Team team : getTeams()) {
            ants.addAll(team.getAllAnts());
        }
        return ants;
    }

    @NonNull
    public Map<String, Ant> getAnts() {
        final Map<String, Ant> ants = new HashMap<>();
        for (Team team : getTeams()) {
            ants.putAll(team.getAnts());
        }
        return ants;
    }

    public Ant getAnt(String id) {
        Ant ant;
        for (Team team : getTeams()) {
            ant = team.getAnts().get(id);
            if (ant != null) {
                return ant;
            }
        }
        return null;
    }

    @Nullable
    public Ant addAnt(String antId, long speciesId) {
        AntSpecies species;
        for (Team team : getTeams()) {
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
        for (Team team : getTeams()) {
            if (team.remove(ant)) {
                return true;
            }
        }
        return false;
    }

    public boolean isSameTeam(long teamId, Ant ant) {
        return isSameTeam(teamId, ant.getId());
    }

    public boolean isSameTeam(long teamId, String antId) {
        for (Team team : getTeams()) {
            if (team.getId() == teamId) {
                if (team.getAntSpecies().getAnts().containsKey(antId)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public GameState getState() {
        return state;
    }

    public void setTeams(List<Team> teams) {
        getTeams().clear();
        if (teams != null) {
            getTeams().addAll(teams);
        }
    }

    public Player getPlayer(long playerId) {
        Player player;
        for (Team team : getTeams()) {
            player = team.getPlayer(playerId);
            if (player != null) {
                return player;
            }
        }
        return null;
    }

    public Team getTeam(long teamId) {
        for (Team team : getTeams()) {
            if (team.getId() == teamId) {
                return team;
            }
        }
        return null;
    }
}
