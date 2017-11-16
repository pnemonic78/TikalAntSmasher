package com.tikalk.antsmasher.model;

import android.util.SparseArray;

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

    public List<Ant> getAllAnts() {
        final List<Ant> ants = new ArrayList<>();
        for (Team team : teams) {
            ants.addAll(team.getAllAnts());
        }
        return ants;
    }

    public SparseArray<Ant> getAnts() {
        final SparseArray<Ant> ants = new SparseArray<>();
        SparseArray<Ant> teamAnts;
        int size;
        for (Team team : teams) {
            teamAnts = team.getAnts();
            size = teamAnts.size();
            for (int i = 0; i < size; i++) {
                ants.append(teamAnts.keyAt(i), teamAnts.valueAt(i));
            }
        }
        return ants;
    }
}
