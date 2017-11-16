package com.tikalk.antsmasher.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Game with teams.
 */

public class Game {

    private long id;
    private final List<Team> teams = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public List<Ant> getAnts() {
        final List<Ant> ants = new ArrayList<>();
        for (Team team : teams) {
            ants.addAll(team.getAnts());
        }
        return ants;
    }
}
