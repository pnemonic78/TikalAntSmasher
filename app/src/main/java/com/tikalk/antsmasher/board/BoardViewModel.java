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
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.text.format.DateUtils;
import android.util.Log;

import javax.inject.Inject;

import com.tikalk.antsmasher.model.Ant;
import com.tikalk.antsmasher.model.AntSpecies;
import com.tikalk.antsmasher.model.Game;
import com.tikalk.antsmasher.model.GameState;
import com.tikalk.antsmasher.model.Team;
import com.tikalk.antsmasher.model.socket.AntLocation;
import com.tikalk.antsmasher.model.socket.AntSmash;
import com.tikalk.antsmasher.networking.rest.GameRestService;
import com.tikalk.antsmasher.networking.response.GameResponse;
import com.tikalk.antsmasher.service.AppService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Board presenter.
 */

public class BoardViewModel extends AndroidViewModel implements
        LifecycleObserver,
        AppService.AppServiceEventListener {

    private static final String TAG = "TAG_BoardViewModel";

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

        void smashAnt(@NonNull Ant ant, boolean user);
    }

    private static final long DELAY_REMOVE = 2 * DateUtils.SECOND_IN_MILLIS;

    private View view;
    private final MutableLiveData<Game> game = new MutableLiveData<>();
    private final Handler handler = new Handler();
    private AppService.AppServiceProxy appService;
    private boolean serviceBound = false;
    private Intent serviceIntent;
    private GameRestService gameRestService;

    @Inject
    public BoardViewModel(@NonNull Application application, GameRestService gameRestService) {
        super(application);

        this.gameRestService = gameRestService;
    }

    public void setView(View view) {
        this.view = view;
    }

    public LiveData<Game> getGame() {
        if (game.getValue() == null) {
            loadGame();
        }
        return game;
    }

    private void loadGame() {
        gameRestService.getLatestGame()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<GameResponse>() {
                    @Override
                    public void onNext(GameResponse response) {
                        Log.v(TAG, "onNext: have game");
                        Game data = new Game();
                        data.setId(response.id);
                        data.setState(response.state);
                        game.postValue(data);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: Failed to fetch game: " + e.getLocalizedMessage(), e);
                        //TODO view.showFetchGameError();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public static Game createGame() {
        Game game = new Game();
        populateGame(game);//TODO delete me!
        return game;
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
        AntSpecies species = team.getAntSpecies();
        switch ((int) team.getId()) {
            case 10:
                species.setId(1);
                break;
            case 20:
                species.setId(2);
                break;
            case 30:
                species.setId(3);
                break;
        }
    }

    /**
     * Start the game.
     */
    public void start() {
    }

    /**
     * Stop the game.
     */
    public void stop() {
    }

    public void onAntTouch(String antId) {
        // Send hit/miss to server via socket.
        AntSmash event = new AntSmash(antId, true);
        onAntSmashed(event);
        appService.smashAnt(event);
    }

    public void onBoardReady() {
        if (allowStart()) {
            start();
        }
    }

    public boolean allowStart() {
        Game game = this.game.getValue();
        return (game != null) && ((game.getState() == GameState.STARTED) || (game.getState() == GameState.RESUMED));
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
    public void onGameStateMessage(GameState state) {
        switch (state) {
            case STARTED:
            case RESUMED:
                view.paint();
                view.onGameStarted();
                break;
            case FINISH:
                view.onGameFinished();
                break;
        }
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
            ant.setLocation(event.xRate, event.yRate);
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
            Log.v(TAG, "onServiceConnected: " + componentName);
            AppService.LocalBinder binder = (AppService.LocalBinder) service;

            appService = binder.getService();
            serviceBound = true;
            appService.registerServiceEventListener(BoardViewModel.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.v(TAG, "onServiceDisconnected: " + componentName);
            serviceBound = false;
            appService = null;
        }
    };
}
