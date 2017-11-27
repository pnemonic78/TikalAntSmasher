package com.tikalk.antsmasher.teams;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import com.tikalk.antsmasher.data.PrefsHelper;
import com.tikalk.antsmasher.model.Player;
import com.tikalk.antsmasher.model.Team;
import com.tikalk.antsmasher.networking.rest.GameRestService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Team presenter.
 */

public class TeamViewModel extends AndroidViewModel {
    private static final String TAG = "TAG_TeamViewModel";

    public interface View {
        void onTeamJoined(Team team, Player player);
    }

    private View view;
    private MutableLiveData<List<Team>> teams;
    private Team team;
    private GameRestService gameRestService;
    private PrefsHelper prefsHelper;
    private long userId;

    @Inject
    public TeamViewModel(Application application, GameRestService gameRestService, PrefsHelper prefsHelper) {
        super(application);
        Log.v(TAG, "TeamViewModel: ");
        this.gameRestService = gameRestService;
        this.prefsHelper = prefsHelper;
        this.userId = prefsHelper.getUserId();
        teams = new MutableLiveData<>();
    }

    public void setView(View view) {
        this.view = view;
    }

    public LiveData<List<Team>> getTeams() {
        if (teams.getValue() == null) {
            Log.i(TAG, "getTeams:");
            loadTeams();
        }else {
            Log.i(TAG, "getTeams: return live data");
        }
        return teams;
    }

    public void refreshTeams(){
        loadTeams();
    }
    private void loadTeams() {
        Log.v(TAG, "Load game teams from server...");

        gameRestService.getCurrentTeams()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<Team>>() {
                    @Override
                    public void onNext(List<Team> response) {
                        Log.v(TAG, "onNext: teams " + response.size() + ", teams: " + response.toString());
                        teams.setValue(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: Failed to fetch teams: " + e.getLocalizedMessage(), e);
                        //TODO view.showLoadTeamsError();
                    }

                    @Override
                    public void onComplete() {
                        Log.v(TAG, "onComplete: teams");
                    }
                });
    }

    public void teamClicked(final Team team) {
        Log.v(TAG, "onNext: about to join the game " + userId);

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
                        //TODO view.showFailedJoinTeam();
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

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
