package com.tikalk.antsmasher.board;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.Color;
import android.graphics.PointF;
import android.support.annotation.Nullable;

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
        /**
         * Move the ant to another location.
         *
         * @param ant the ant to move.
         */
        void moveAnt(Ant ant);

        /**
         * Repaint the board.
         */
        void paint();

        /**
         * Notification that the game has finished.
         */
        void onGameFinished();
    }

    private View view;
    private MutableLiveData<Game> game;
    private static final Random random = new Random();
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

    public static void populateGame(Game game) {
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

    public static void populateTeam(Team team) {
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

    private static void populateSpecies(AntSpecies species) {
        final int size = 5 + random.nextInt(5);
        final int antIdBase = species.getId() * 1000;
        Ant ant;
        for (int i = 0; i < size; i++) {
            ant = new Ant(antIdBase + i);
            ant.setSpecies(species);
            ant.setLocation(random.nextFloat(), random.nextFloat());
            ant.setLocation(i / (float) size, species.getId() / 10f);//±!@
            species.addAnt(ant);
        }
    }

    public void start(final Game game) {
        view.paint();

        thread = new Thread() {

            @Override
            public void run() {
                try {
                    sleep(1000L);
                } catch (InterruptedException e) {
                }

                final List<Ant> ants = game.getAllAnts();
                final int size = ants.size();
                boolean visible;
                Ant ant;
                float dx;
                float dy;
                float x;
                float y;
                PointF location;

                do {
                    try {
                        sleep(1L);
                    } catch (InterruptedException e) {
                    }
                    visible = false;
                    for (int i = 0; i < size; i++) {
                        ant = ants.get(i);
                        dx = (random.nextBoolean() ? +1f : -1f) * random.nextFloat() * 0.001f;
                        dy = random.nextFloat() * 0.001f;
                        location = ant.getLocation();
                        x = location.x + dx;
                        y = location.y + dy;
                        ant.setLocation(x, y);
                        view.moveAnt(ant);
                        visible |= ant.isVisible();
                    }
                    view.paint();
                } while (visible && isAlive() && !isInterrupted());
                view.onGameFinished();
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

    public void onAntTouch(@Nullable Integer antId) {
        //TODO send hit/miss to server via socket.
        System.out.println("±!@ onAntTouch " + antId);
    }
}
