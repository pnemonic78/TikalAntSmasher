package com.tikalk.antsmasher.board;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.Color;
import android.os.SystemClock;
import android.util.Log;

import java.util.Random;

import com.tikalk.antsmasher.model.Ant;
import com.tikalk.antsmasher.model.AntSpecies;
import com.tikalk.antsmasher.model.Game;
import com.tikalk.antsmasher.model.Team;
import com.tikalk.antsmasher.model.socket.AntLocation;
import com.tikalk.antsmasher.model.socket.AntSmash;

/**
 * Board presenter.
 */

public class BoardViewModel extends ViewModel {

    private static final String TAG = "BoardViewModel";

    public interface View {
        /**
         * Add an ant to the board.
         *
         * @param ant the new ant.
         */
        void addAnt(Ant ant);

        /**
         * Remove an ant.
         *
         * @param ant the ant to remove.
         */
        void removeAnt(Ant ant);

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
        game.setId("1");

        Team team = new Team("10", "Army");
        populateTeam(team);
        game.getTeams().add(team);

        team = new Team("20", "Fire");
        populateTeam(team);
        game.getTeams().add(team);

        team = new Team("30", "Black");
        populateTeam(team);
        game.getTeams().add(team);
    }

    public static void populateTeam(Team team) {
        AntSpecies species = team.getAntSpecies();
        switch (team.getId()) {
            case "10":
                species.setId("1");
                species.setTint(0xFF00cc00);
                break;
            case "20":
                species.setId("2");
                species.setTint(0xFFcc0000);
                break;
            case "30":
                species.setId("3");
                species.setTint(Color.BLACK);
                break;
        }
    }

    public void start() {
        view.paint();

        // fake ants from the server.
        thread = new Thread() {

            @Override
            public void run() {
                try {
                    // wait for View to start drawing.
                    sleep(500L);
                } catch (InterruptedException e) {
                }
                final View view = BoardViewModel.this.view;
                final Game game = BoardViewModel.this.game.getValue();
                final int size = 20;
                final Ant[] ants = new Ant[size];
                Ant ant;
                AntSpecies species;
                float dy;
                float x;
                float y;
                double t = 0;
                final double T = Math.PI * 2 * 3;
                final double dt = T / 300;
                float[] antX = new float[size];
                float[] antY = new float[size];
                float[] antVelocityY = new float[size];
                int antId = 1;
                int teamIndex;
                long start = SystemClock.uptimeMillis();

                for (int i = 0; i < size; i++) {
                    antX[i] = random.nextFloat();
                    antY[i] = 0f;
                    antVelocityY[i] = 1f + random.nextFloat();

                    teamIndex = random.nextInt(3);
                    species = game.getTeams().get(teamIndex).getAntSpecies();

                    ant = new Ant(String.valueOf(antId++));
                    ant.setLocation(antX[i], antY[i]);
                    species.add(ant);
                    ants[i] = ant;
                }

                do {
                    for (int i = 0; i < size; i++) {
                        ant = ants[i];
                        if (!ant.isAlive()) {
                            continue;
                        }
                        dy = antVelocityY[i] * random.nextFloat() * 0.02f;
                        x = antX[i] + (float) (Math.sin(t) / 10);
                        y = ant.getLocation().y + dy;
                        species = ant.getSpecies();
                        onAntMoved(new AntLocation(ant.getId(), species.getId(), x, y));
                        try {
                            sleep(1);
                        } catch (InterruptedException e) {
                        }

                        if (!ant.isVisible()) {
                            ants[i] = null;
                            species.remove(ant);

                            ant = new Ant(String.valueOf(antId++));
                            ant.setLocation(antX[i], antY[i]);
                            species.add(ant);
                            ants[i] = ant;
                        }
                    }
                    t += dt;
                }
                while ((SystemClock.uptimeMillis() <= (start + 15000L)) && isAlive() && !isInterrupted());
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

    public void onAntTouch(String antId) {
        //TODO send hit/miss to server via socket.
        AntSmash event = new AntSmash(antId, true);

        view.sendSmash(event);
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
            Ant ant = game.getAnt(event.antId);
            if (ant == null) {
                ant = game.addAnt(event.antId, event.speciesId);
                if (ant == null) {
                    Log.w(TAG, "Ant was not added!");
                    return;
                }
                view.addAnt(ant);
            }
            ant.setLocation(event.xPercent, event.yPercent);
            if (ant.isVisible()) {
                view.moveAnt(ant);
            } else {
                game.removeAnt(ant);
                view.removeAnt(ant);
            }
        }
        view.paint();
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
                view.smashAnt(ant, event.user);
            }
        }
        view.paint();
    }
}
