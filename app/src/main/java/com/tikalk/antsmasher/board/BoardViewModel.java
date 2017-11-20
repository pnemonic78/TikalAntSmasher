package com.tikalk.antsmasher.board;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.Color;

import java.util.List;
import java.util.Random;

import com.tikalk.antsmasher.model.Ant;
import com.tikalk.antsmasher.model.AntLocation;
import com.tikalk.antsmasher.model.AntSmash;
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

        void smashAnt(Ant ant, boolean user);

        void sendSmash(AntSmash event);
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

        // fake movements from the server.
        thread = new Thread() {

            @Override
            public void run() {
                final View view = BoardViewModel.this.view;
                final Game game = BoardViewModel.this.game.getValue();
                final List<Ant> ants = game.getAllAnts();
                final int size = ants.size();
                Ant ant;
                float dy;
                float x;
                float y;
                double t = 0;
                final double T = Math.PI * 2 * 3;
                final double dt = T / 300;
                float[] antX = new float[size];

                for (int i = 0; i < size; i++) {
                    ant = ants.get(i);
                    antX[i] = ant.getLocation().x;
                }

                do {
                    for (int i = 0; i < size; i++) {
                        ant = ants.get(i);
                        if (!ant.isAlive()) {
                            continue;
                        }
                        dy = random.nextFloat() * 0.02f;
                        x = antX[i] + (float) (Math.sin(t) / 10);
                        y = ant.getLocation().y + dy;
                        onAntMoved(new AntLocation(ant.getId(), ant.getSpecies().getId(), x, y));
                        if (!ant.isVisible()) {
                            //TODO game.removeAnt(ant);
                            //TODO view.removeAnt(ant);
                        }
                        try {
                            sleep(1);
                        } catch (InterruptedException e) {
                        }
                    }
                    view.paint();
                    t += dt;
                } while ((t <= T) && isAlive() && !isInterrupted());
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

    public void onAntTouch(int antId) {
        //TODO send hit/miss to server via socket.
        AntSmash event = new AntSmash(antId, true);

        if (view != null) {
            view.sendSmash(event);
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

    /**
     * Notification from the server that the ant has been moved.
     *
     * @param event the location event.
     */
    public void onAntMoved(AntLocation event) {
        Game game = getGame().getValue();
        if (game != null) {
            Ant ant = game.getAnt(event.id);
            ant.setLocation(event.xPercent, event.yPercent);
            if (view != null) {
                view.moveAnt(ant);
            }
        }
    }

    /**
     * Notification from the server that the ant has been smashed.
     *
     * @param event the smash event.
     */
    public void onAntSmashed(AntSmash event) {
        Game game = getGame().getValue();
        if (game != null) {
            Ant ant = game.getAnt(event.id);
            if (ant != null) {
                ant.setAlive(false);
                if (view != null) {
                    view.smashAnt(ant, event.user);
                }
            }
        }
    }
}
