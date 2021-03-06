package com.tikalk.antsmasher.teams;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Handler;
import android.util.Log;

import java.net.URISyntaxException;
import java.util.List;

import javax.inject.Inject;

import com.tikalk.antsmasher.data.PrefsHelper;
import com.tikalk.antsmasher.model.GameState;
import com.tikalk.antsmasher.model.Player;
import com.tikalk.antsmasher.model.Team;
import com.tikalk.antsmasher.model.socket.PlayingTeam;
import com.tikalk.antsmasher.networking.ApiContract;
import com.tikalk.antsmasher.networking.RetrofitContainer;
import com.tikalk.antsmasher.networking.response.GameResponse;
import com.tikalk.antsmasher.networking.rest.GameRestService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Team presenter.
 */

public class TeamViewModel extends AndroidViewModel implements LifecycleObserver {
    private static final String TAG = "TAG_TeamViewModel";

    public interface View {
        void onTeamJoined(Team team, Player player);

        void showFetchTeamsError(Throwable e);

        void showJoinTeamError(Throwable e);
        void dismissSwipeToRefresh(String message);
    }

    private View view;
    private final MutableLiveData<List<Team>> teams = new MutableLiveData<>();
    private Team team;
    private GameRestService gameRestService;
    private RetrofitContainer retrofitContainer;
    private PrefsHelper prefsHelper;
    private long userId;
    private final Handler handler = new Handler();
    private boolean allowPolling = true;
    private Runnable pollTeams;

    @Inject
    public TeamViewModel(Application application, RetrofitContainer retrofitContainer, PrefsHelper prefsHelper) {
        super(application);
        Log.v(TAG, "TeamViewModel: ");
        this.retrofitContainer = retrofitContainer;
        this.gameRestService = retrofitContainer.getRestService();
        this.prefsHelper = prefsHelper;
        this.userId = prefsHelper.getUserId();
    }

    public void setView(View view) {
        this.view = view;
    }

    public LiveData<List<Team>> getTeams() {
        if (teams.getValue() == null) {
            Log.v(TAG, "getTeams: load");
            loadTeams();
        } else {
            Log.v(TAG, "getTeams: return live data");
        }
        return teams;
    }

    void refreshTeams(){
        loadTeams();
    }

    void createGame(List<PlayingTeam> body){
        Log.i(TAG, "createGame: ");
        gameRestService.createGame(1, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<GameResponse>() {
                    @Override
                    public void onNext(GameResponse gameResponse) {
                        if(gameResponse.state == GameState.NOT_STARTED){
                            view.dismissSwipeToRefresh("Game Created, select team to join");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(e.getLocalizedMessage().contains("HTTP 400")){
                            checkGameState();
                        }else{
                            view.dismissSwipeToRefresh("Unable to create game");
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void checkGameState(){
        Log.i(TAG, "checkGameState: ");
        gameRestService.getLatestGame()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<GameResponse>() {
                    @Override
                    public void onNext(GameResponse gameResponse) {
                        if(gameResponse.state == GameState.NOT_STARTED){
                            view.dismissSwipeToRefresh("Game already created, select team to join");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.dismissSwipeToRefresh("Unable to get game state");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void loadTeams() {
        Log.v(TAG, "Load game teams from server...");

        gameRestService.getCurrentTeams()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<Team>>() {
                    @Override
                    public void onNext(List<Team> response) {
                        Log.v(TAG, "onNext: teams " + response.size() + ", teams: " + response);
                        teams.setValue(response);

                        // Poll until we have some teams.
                        if (response.isEmpty()) {
                            if (allowPolling) {
                                //FIXME use Rx instead of Handler.
                                if (pollTeams == null) {
                                    pollTeams = () -> loadTeams();
                                }
                                handler.postDelayed(pollTeams, 1000L);
                            }
                        } else {
                            pollTeams = null;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: Failed to fetch teams: " + e.getLocalizedMessage(), e);
                        if (view != null) {
                            view.showFetchTeamsError(e);
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.v(TAG, "onComplete: teams");
                    }
                });
    }

    void teamClicked(final Team team) {
        Log.v(TAG, "onNext: about to join the game team: " + team.getId() + ", user: " + userId);

        gameRestService.createPlayer(team.getId(), userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Player>() {
                    @Override
                    public void onNext(Player player) {
                        Log.v(TAG, "onNext: Joined team: " + team);
                        onTeamJoined(team, player);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: Failed to join team: " + e.getLocalizedMessage(), e);
                        if (view != null) {
                            view.showJoinTeamError(e);
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void onTeamJoined(Team team, Player player) {
        team.addPlayer(player);
        prefsHelper.setTeamId(team.getId());
        prefsHelper.setPlayerId(player.getId());
        this.team = team;
        if (view != null) {
            view.onTeamJoined(team, player);
        }
    }

    public Team getTeam() {
        return team;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void onStart() {
        allowPolling = true;
        loadTeams();

        Log.v(TAG, "onStart:");
        // Were we busy polling before we were rudely stopped.
//        if (pollTeams != null) {
//            Log.v(TAG, "About to load teams");
//        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onStop() {
        Log.v(TAG, "onStop");
        allowPolling = false;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy() {
        Log.v(TAG, "onDestroy");
        view = null;
    }

    void onServerNameChanged(String serverName){
        try {
            this.retrofitContainer.updateBaseUrl(ApiContract.buildAdminBaseUrl(serverName));
            this.gameRestService = this.retrofitContainer.getRestService();
            teams.setValue(null);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
