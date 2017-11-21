package com.tikalk.antsmasher.board;

import android.app.Application;
import android.app.Service;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.text.format.DateUtils;
import android.util.Log;

import java.util.Random;

import com.tikalk.antsmasher.BuildConfig;
import com.tikalk.antsmasher.model.Ant;
import com.tikalk.antsmasher.model.AntSpecies;
import com.tikalk.antsmasher.model.Game;
import com.tikalk.antsmasher.model.Team;
import com.tikalk.antsmasher.model.socket.AntLocation;
import com.tikalk.antsmasher.model.socket.AntSmash;
import com.tikalk.antsmasher.service.AppService;
import com.tikalk.antsmasher.utils.SoundHelper;

/**
 * Board presenter.
 */

public class BoardViewModel extends AndroidViewModel implements
        LifecycleObserver,
        AppService.AppServiceEventListener {

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
         * Notification that the game has started.
         */
        void onGameStarted();

        /**
         * Notification that the game has finished.
         */
        void onGameFinished();

        void smashAnt(Ant ant, boolean user);
    }

    private static final long DELAY_REMOVE = 2 * DateUtils.SECOND_IN_MILLIS;

    private View view;
    private MutableLiveData<Game> game;
    private static final Random random = new Random();
    private Thread thread;
    private final Handler handler = new Handler();
    private AppService.AppServiceProxy appService;
    private boolean serviceBound = false;
    private Intent serviceIntent;

    public BoardViewModel(@NonNull Application application) {
        super(application);
    }

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

    /**
     * Start the game.
     */
    public void start() {
        // fake ants from the server.
        thread = new Thread() {

            @Override
            public void run() {
                onGameStarted();
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
                        species = ant.getSpecies();
                        if (ant.isAlive()) {
                            dy = antVelocityY[i] * random.nextFloat() * 0.02f;
                            x = antX[i] + (float) (Math.sin(t) / 10);
                            y = ant.getLocation().y + dy;
                            onAntMoved(new AntLocation(ant.getId(), species.getId(), x, y));
                        }
                        if (!ant.isVisible()) {
                            ants[i] = null;
                            species.remove(ant);

                            ant = new Ant(String.valueOf(antId++));
                            ant.setLocation(antX[i], antY[i]);
                            species.add(ant);
                            ants[i] = ant;
                        }
                        try {
                            sleep(2);
                        } catch (InterruptedException e) {
                        }
                    }
                    t += dt;
                }
                while ((SystemClock.uptimeMillis() <= (start + 15000L)) && isAlive() && !isInterrupted());
                onGameFinished();
            }
        };
        thread.start();
    }

    /**
     * Stop the game.
     */
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
        // Send hit/miss to server via socket.
        AntSmash event = new AntSmash(antId, true);
        appService.smashAnt(event);
        if (BuildConfig.DEBUG) {
            onAntSmashed(event);//TODO delete me!
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

    @Override
    public void onGameStarted() {
        view.paint();
        view.onGameStarted();
    }

    @Override
    public void onGameFinished() {
        view.onGameFinished();
    }

    @Override
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

    @Override
    public void onAntSmashed(AntSmash event) {
        Game game = getGame().getValue();
        if (game != null) {
            Ant ant = game.getAnt(event.id);
            if (ant != null) {
                ant.setAlive(false);
                view.smashAnt(ant, event.user);
                removeAntDelayed(game, ant, DELAY_REMOVE);
            }
        }
        view.paint();
    }

    private void removeAntDelayed(Game game, Ant ant, long delay) {
        handler.postDelayed(() -> {
            game.removeAnt(ant);
            view.removeAnt(ant);
            view.paint();
        }, delay);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void onStart() {
        Log.v(TAG, "onStart");
        final Context context = getApplication();
        serviceIntent = new Intent(context, AppService.class);
        context.bindService(serviceIntent, connection, Service.BIND_AUTO_CREATE);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onStop() {
        Log.v(TAG, "onStop");
        //This means that onStop called due to screen rotation, therefore it's crucial to unbind
        //the service and clean its reference to insure that the destroyed Activity reference will be released.
        //Service will rebound in onStart after mActivity recreation.
        //If this is not due to screen rotation service will be killed in onDestroy ( stopService() ).
        final Context context = getApplication();
        context.unbindService(connection); //This will not stop the service as it started with startService()
        serviceBound = false;
        appService = null;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy() {
        Log.v(TAG, "onDestroy");
        if (appService != null && serviceBound) {
            final Context context = getApplication();
            context.unbindService(connection);
            context.stopService(serviceIntent);
        }
    }

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            AppService.LocalBinder binder = (AppService.LocalBinder) service;

            appService = binder.getService();
            serviceBound = true;
            appService.registerServiceEventListener(BoardViewModel.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i(TAG, "onServiceDisconnected: ");
            serviceBound = false;
            appService = null;
        }
    };
}
