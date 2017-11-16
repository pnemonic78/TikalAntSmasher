package com.tikalk.antsmasher.board;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import java.util.Random;

import com.tikalk.antsmasher.model.Ant;
import com.tikalk.antsmasher.model.AntSpecies;
import com.tikalk.antsmasher.model.Game;
import com.tikalk.antsmasher.model.Team;

/**
 * Board presenter.
 */

public class BoardViewModel extends ViewModel {

    public interface View {
    }

    private View view;
    private MutableLiveData<Game> game;
    private Random random;

    public void setView(View view) {
        this.view = view;
    }

    public LiveData<Game> getGame() {
        if (game == null) {
            game = new MutableLiveData<>();
            loadGame();
        }
        return game;
    }

    private void loadGame() {
        // TODO Do an asynchronous operation to list for game on socket.
        Game data = new Game();
        populateGame(data);//TODO delete me!

        game.postValue(data);
    }

    private void populateGame(Game game) {
        game.setId(1);

        Team team = new Team(10, "Army");
        populateTeam(team);
        game.getTeams().add(team);

        team = new Team(20, "Fire");
        populateTeam(team);
        game.getTeams().add(team);

        team = new Team(30, "Black");
        populateTeam(team);
        game.getTeams().add(team);
    }

    private void populateTeam(Team team) {
        final long id = team.getId();
        AntSpecies species = team.getAntSpecies();
        if (id == 10) {
            species.setTint(Color.GREEN);
        } else if (id == 20) {
            species.setTint(Color.RED);
        } else if (id == 30) {
            species.setTint(Color.BLACK);
        }

        populateSpecies(species);
    }

    private void populateSpecies(AntSpecies species) {
        if (random == null) {
            random = new Random();
        }
        final int size = 10 + random.nextInt(10);
        final int speciesId = (int) (species.getId() * 100);
        Ant ant;
        for (int i = 0; i < size; i++) {
            ant = new Ant(speciesId + i);
            species.addAnt(ant);
        }
    }
}
