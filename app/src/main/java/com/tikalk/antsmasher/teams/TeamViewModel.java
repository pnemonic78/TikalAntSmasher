package com.tikalk.antsmasher.teams;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import java.util.List;

import com.tikalk.antsmasher.board.BoardViewModel;
import com.tikalk.antsmasher.model.Game;
import com.tikalk.antsmasher.model.Team;
import com.tikalk.antsmasher.networking.GameRestService;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Team presenter.
 */

public class TeamViewModel extends ViewModel {
    private static final String TAG = "TAG_TeamViewModel";

    public interface View {
        void onTeamJoined(Team team);
    }

    private View view;
    private MutableLiveData<List<Team>> teams;
    private Team team;
    private GameRestService gameRestService;

    @Inject
    public TeamViewModel(GameRestService gameRestService){
        Log.v(TAG, "TeamViewModel: ");
        teams = new MutableLiveData<>();
        this.gameRestService = gameRestService;
    }

    public void setView(View view) {
        this.view = view;
    }

    public LiveData<List<Team>> getTeams(Context context) {
        loadTeams(context);
        return teams;
    }

    private void loadTeams(Context context) {
        Log.v(TAG, "about to load game teams from server...");

        gameRestService.getCurrentTeams("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String response) {
                        Log.v(TAG, "onNext: got teams!!, showing teams list");
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
        Log.v(TAG, "onNext: about to join the game");

        gameRestService.createPlayer("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        Log.v(TAG, "onNext: joined!! opening game screen");
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
