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
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import com.tikalk.antsmasher.data.PrefsHelper;
import com.tikalk.antsmasher.model.Ant;
import com.tikalk.antsmasher.model.Game;
import com.tikalk.antsmasher.model.GameState;
import com.tikalk.antsmasher.model.Player;
import com.tikalk.antsmasher.model.Team;
import com.tikalk.antsmasher.model.socket.AntLocation;
import com.tikalk.antsmasher.model.socket.AntSmash;
import com.tikalk.antsmasher.model.socket.PlayerScore;
import com.tikalk.antsmasher.model.socket.StartBody;
import com.tikalk.antsmasher.model.socket.TeamScore;
import com.tikalk.antsmasher.networking.RetrofitContainer;
import com.tikalk.antsmasher.networking.response.GameResponse;
import com.tikalk.antsmasher.networking.rest.GameRestService;
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
        void onGameFinished(List<Team> teams, Player winner);

        void smashAnt(@Nullable Ant ant, boolean user);

        void showFetchGameError(Throwable e);
    }

    private static final long DELAY_REMOVE = 2 * DateUtils.SECOND_IN_MILLIS;

    private View view;
    private final MutableLiveData<Game> game = new MutableLiveData<>();
    private final Handler handler = new Handler();
    private AppService.AppServiceProxy appService;
    private boolean serviceBound = false;
    private Intent serviceIntent;
    private GameRestService gameRestService;
    private long playerId;
    private long teamId;
    private Player player;
    private Team team;
    private List<Team> latestTeams;
    private PrefsHelper prefsHelper;

    @Inject
    public BoardViewModel(@NonNull Application application, RetrofitContainer retrofitContainer, PrefsHelper prefsHelper) {
        super(application);
        this.gameRestService = retrofitContainer.getRestService();
        this.playerId = prefsHelper.getPlayerId();
        this.teamId = prefsHelper.getTeamId();
        this.player = null;
        this.team = null;
        this.prefsHelper = prefsHelper;
    }

    public void setView(View view) {
        this.view = view;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
        this.player = null;
    }

    void setTeamId(long teamId) {
        this.teamId = teamId;
        this.team = null;
    }

    public LiveData<Game> getGame() {
        return getGame(false);
    }

    LiveData<Game> getGame(boolean fetch) {
        Game value = game.getValue();
        if (fetch || (value == null)) {
            loadGame();
        } else {
            this.player = null;
            this.team = null;
        }
        return game;
    }

    private Game getGameValue() {
        return getGame().getValue();
    }

    private void loadGame() {
        gameRestService.getLatestGame()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<GameResponse>() {
                    @Override
                    public void onNext(GameResponse response) {
                        Log.v(TAG, "onNext: have game " + response.state);
                        Game data = new Game();
                        data.setId(response.id);
                        data.setState(response.state);

                        player = null;
                        team = null;
                        game.postValue(data);
                        if(response.state == GameState.STARTED){
                            view.paint();
                            view.onGameStarted();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: Failed to fetch game: " + e.getLocalizedMessage(), e);
                        if (view != null) {
                            view.showFetchGameError(e);
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    /**
     * Start the game.
     */
    public void start() {
        onGameStarted();
    }

    /**
     * Stop the game.
     */
    void stop() {
    }

    void onAntTouch(String antId) {
        // Send hit/miss to server via socket.
        Game game = getGameValue();
        if (game != null) {
            AntSmash event = new AntSmash(antId == null ? AntSmash.TYPE_MISS : game.isSameTeam(teamId, antId) ? AntSmash.TYPE_SELF_HIT : AntSmash.TYPE_HIT, antId, playerId);
            //onAntSmashed(event);
            appService.smashAnt(event);
        }
    }

     void onBoardReady() {
        if (allowStart()) {
            start();
        }
    }

    private boolean allowStart() {
        Game game = getGameValue();
        return (game != null) && ((game.getState() == GameState.STARTED) || (game.getState() == GameState.RESUMED));
    }

    @Override
    public void onGameStarted() {
        view.paint();
        view.onGameStarted();
    }

    @Override
    public void onGameFinished() {
    }

    @Override
    public void onGameStateMessage(GameState state) {
        switch (state) {
            case STARTED:
            case RESUMED:
                view.paint();
                view.onGameStarted();
                break;
            case STOPPED:
            case FINISHED:
                gameRestService.getLatestTeams()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DisposableObserver<List<Team>>() {
                            @Override
                            public void onNext(List<Team> teams) {
                                Log.i(TAG, "onNext: got " + teams.size() + " teams");
                                latestTeams = teams;
                                getLeaderPlayer();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "onError: Failed to fetch teams: " + e.getLocalizedMessage(), e);
                            }

                            @Override
                            public void onComplete() {
                            }
                        });
                break;
        }
    }

    private void getLeaderPlayer() {
        gameRestService.getLeaderPlayer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<Player>>() {
                    @Override
                    public void onNext(List<Player> players) {
                        Log.i(TAG, "onNext: winner " + players.get(0).getName() + ", score: " + players.get(0).getScore());

                        if(players.isEmpty() || players.size() == 0){
                            view.onGameFinished(null, null);
                        }else {
                            view.onGameFinished(latestTeams, players.get(0));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: Failed to fetch lead player: " + e.getLocalizedMessage(), e);
                        view.onGameFinished(null, null);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    void startGame() {
        Log.i(TAG, "startGame: ");
        gameRestService.startGame(1, 3, new StartBody(prefsHelper.getUserName()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<GameResponse>() {
                    @Override
                    public void onNext(GameResponse response) {
                        Log.i(TAG, "onNext: " + response.state);
                     }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void onAntMoved(AntLocation event) {
        Game game = getGameValue();
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
        Game game = getGameValue();
        if (game != null) {
            Ant ant = game.getAnt(event.antId);
            if (ant != null) {
                ant.setAlive(false);
                removeAntDelayed(game, ant, DELAY_REMOVE);
            }
            view.smashAnt(ant, event.playerId == this.playerId);
        }
        view.paint();
    }

    private void removeAntDelayed(Game game, Ant ant, long delay) {
        handler.postDelayed(() -> {
            game.removeAnt(ant);
            if (view != null) {
                view.removeAnt(ant);
                view.paint();
            }
        }, delay);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void onStart() {
        Log.v(TAG, "onStart");
        final Context context = getApplication();
        serviceIntent = new Intent(context, AppService.class);
        context.bindService(serviceIntent, connection, Service.BIND_AUTO_CREATE);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy() {
        Log.v(TAG, "onDestroy");
        if (appService != null && serviceBound) {
            Log.i(TAG, "onDestroy: about to stop the service");
            final Context context = getApplication();
            context.unbindService(connection);
            context.stopService(serviceIntent);
            serviceBound = false;
            appService = null;
        }

        view = null;
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

    @Override
    public void onPlayerScore(PlayerScore event) {
        Player player = getPlayer();
        if ((player != null) && (player.getId() == event.playerId)) {
            player.setScore(event.score);
//            if (view != null) {
//                view.showScore(player, getGameValue().getTeams());
//            }
        }
    }

    @Override
    public void onTeamScore(TeamScore event) {
        Team team = getGameValue().getTeam(event.teamId);
        if (team != null) {
            team.setScore(event.score);
//            if (view != null) {
//                view.showScore(getPlayer(), getGameValue().getTeams());
//            }
        }
    }

    @Nullable
    private Player getPlayer() {
        if (player == null) {
            Game game = getGameValue();
            this.player = (game != null) ? game.getPlayer(playerId) : null;
        }
        return player;
    }

    @Nullable
    private Team getTeam() {
        if (team == null) {
            Game game = getGameValue();
            this.team = (game != null) ? game.getTeam(teamId) : null;
        }
        return team;
    }
}
