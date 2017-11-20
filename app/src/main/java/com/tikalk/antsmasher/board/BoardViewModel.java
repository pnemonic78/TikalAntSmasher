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
import com.tikalk.antsmasher.model.AntLocation;
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

        void smashAnt(Ant ant);
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
        final int antIdBase = species.getId() * 1000000;
        Ant ant;
        for (int i = 0; i < size; i++) {
            ant = new Ant(antIdBase + i);
            ant.setSpecies(species);
            ant.setLocation(random.nextFloat(), 0f);
            species.addAnt(ant);
        }
    }

    public void start() {
        view.paint();

        thread = new Thread() {

            @Override
            public void run() {
                final Game game = BoardViewModel.this.game.getValue();
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
                        if (!ant.isAlive()) {
                            continue;
                        }
                        dx = (random.nextBoolean() ? +1f : -1f) * random.nextFloat() * 0.001f;
                        dy = random.nextFloat() * 0.001f;
                        location = ant.getLocation();
                        x = location.x + dx;
                        y = location.y + dy;
                        onAntMoved(new AntLocation(ant.getId(), x, y));
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
        if (antId != null) {
            Ant ant = game.getValue().getAnts().get(antId);
            ant.setAlive(false);
            view.smashAnt(ant);
        }
    }

    public void onBoardReady() {
        if (allowStart()) {
            start();
        }
    }

    public boolean allowStart() {
        return (game.getValue() != null) && ((thread == null) || thread.isInterrupted() || !thread.isAlive());
    }

    public void onAntMoved(AntLocation location) {
        Game game = getGame().getValue();
        if (game != null) {
            Ant ant = game.getAnt(location.id);
            ant.setLocation(location.xPercent, location.yPercent);
            view.moveAnt(ant);
        }
    }
}
