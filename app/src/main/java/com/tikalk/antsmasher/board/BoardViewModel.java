package com.tikalk.antsmasher.board;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.Color;

import java.util.List;
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
        void moveAntBy(Ant ant, float dxPercent, float dyPercent);

        void paint();

        void stopGame();
    }

    private View view;
    private MutableLiveData<Game> game;
    private Random random;
    private Thread thread;

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
        start(data);
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
            species.setId(1);
            species.setTint(Color.GREEN);
        } else if (id == 20) {
            species.setId(2);
            species.setTint(Color.RED);
        } else if (id == 30) {
            species.setId(3);
            species.setTint(Color.BLACK);
        }

        populateSpecies(species);
    }

    private void populateSpecies(AntSpecies species) {
        if (random == null) {
            random = new Random();
        }
        final int size = 10;//TODO + random.nextInt(10);
        final int antIdBase = species.getId() * 1000;
        Ant ant;
        for (int i = 0; i < size; i++) {
            ant = new Ant(antIdBase + i);
            ant.setSpecies(species);
            ant.setLocation(i / 10f, species.getId() / 10f);
            species.addAnt(ant);
        }
    }

    public void start(final Game game) {
        thread = new Thread() {

            @Override
            public void run() {
                if (random == null) {
                    random = new Random();
                }
                final List<Ant> ants = game.getAllAnts();
                final int size = ants.size();
                boolean visible;
                Ant ant;
                do {
                    visible = false;
                    for (int i = 0; i < size; i++) {
                        float dx = (random.nextBoolean() ? +1f : -1f) * random.nextFloat() / 100f;
                        float dy = random.nextFloat() / 100f;
                        ant = ants.get(i);
                        ant.moveBy(dx, dy);
                        view.moveAntBy(ant, dx, dy);
                        visible |= ant.isVisible();
                    }
                    view.paint();
                    try {
                        sleep(50L);
                    } catch (InterruptedException e) {
                    }
                } while (visible && isAlive() && !isInterrupted());
                view.stopGame();
            }
        };
        thread.start();
    }

    public void stop() {
        if (thread != null) {
            thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException e) {
            }
        }
    }
}
