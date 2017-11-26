package com.tikalk.antsmasher.teams;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import java.util.List;

import com.tikalk.antsmasher.board.BoardViewModel;
import com.tikalk.antsmasher.data.PrefsHelper;
import com.tikalk.antsmasher.model.Game;
import com.tikalk.antsmasher.model.Player;
import com.tikalk.antsmasher.model.Team;
import com.tikalk.antsmasher.networking.GameRestService;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Team presenter.
 */

public class TeamViewModel extends AndroidViewModel {
    private static final String TAG = "TAG_TeamViewModel";

    public interface View {
        void onTeamJoined(Team team);
    }

    private View view;
    private MutableLiveData<List<Team>> teams;
    private Team team;
    private GameRestService gameRestService;
    PrefsHelper mPrefsHelper;
    private String user;

    @Inject
    public TeamViewModel(Application application, GameRestService gameRestService, PrefsHelper prefsHelper) {
        super(application);
        Log.i(TAG, "TeamViewModel: ");
        teams = new MutableLiveData<>();
        this.gameRestService = gameRestService;
        this.mPrefsHelper = prefsHelper;
        user = prefsHelper.getUserName();
    }

    public void setView(View view) {
        this.view = view;
    }

    public LiveData<List<Team>> getTeams(Context context) {
        loadTeams(context);
        return teams;
    }

    private void loadTeams(Context context) {
        Log.i(TAG, "about to load game teams from server...");

        gameRestService.getCurrentTeams()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<Team>>() {
                    @Override
                    public void onNext(List<Team> response) {
                        Log.i(TAG, "onNext: got teams!!, showing teams list");
                        Game game = BoardViewModel.createGame();
                        teams.setValue(game.getTeams());
                    }

                    @Override
                    public void onError(Throwable e) {
                        //TODO Remove this when server is up...
                        Log.e(TAG, "onNext: Something happened, can't get teams list from server...");
                        Game game = BoardViewModel.createGame();
                        teams.setValue(game.getTeams());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void teamClicked(Team team) {
        Log.i(TAG, "onNext: about to join the game");

        gameRestService.joinGame(team.getId(), user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Player>() {
                    @Override
                    public void onNext(Player player) {
                        Log.i(TAG, "onNext: joined!! opening game screen");

                        onTeamJoined(team);
                    }

                    @Override
                    public void onError(Throwable e) {
                        //TODO Remove this when server is up...
                        Log.e(TAG, "onError: something happened, can't join the game.. ");
                        onTeamJoined(team);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void onTeamJoined(Team team) {
        this.team = team;
        if (view != null) {
            view.onTeamJoined(team);
        }
    }

    public Team getTeam() {
        return team;
    }
}
