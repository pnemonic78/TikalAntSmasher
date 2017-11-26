package com.tikalk.antsmasher.teams;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import com.tikalk.antsmasher.board.BoardViewModel;
import com.tikalk.antsmasher.data.PrefsHelper;
import com.tikalk.antsmasher.model.AntSpecies;
import com.tikalk.antsmasher.model.Game;
import com.tikalk.antsmasher.model.Player;
import com.tikalk.antsmasher.model.Team;
import com.tikalk.antsmasher.networking.GameRestService;

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
    private final MutableLiveData<List<AntSpecies>> species = new MutableLiveData<>();
    private final MutableLiveData<List<Team>> teams = new MutableLiveData<>();
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
    }

    public void setView(View view) {
        this.view = view;
    }

    public LiveData<List<Team>> getTeams() {
        if (species.getValue() == null) {
            loadSpecies();
        } else {
            loadTeams();
        }
        return teams;
    }

    private void loadSpecies() {
        Log.v(TAG, "Load ant species...");

        gameRestService.getAntSpecies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<AntSpecies>>() {
                    @Override
                    public void onNext(List<AntSpecies> response) {
                        Log.v(TAG, "onNext: have ant species");
                        species.setValue(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: Failed to fetch ant species: " + e.getLocalizedMessage(), e);
                        //TODO view.showLoadSpeciesError();
                    }

                    @Override
                    public void onComplete() {
                        loadTeams();
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
                        Log.v(TAG, "onNext: teams");
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
        Log.v(TAG, "onNext: about to join the game");

        gameRestService.createPlayer(team.getId(), userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Player>() {
                    @Override
                    public void onNext(Player player) {
                        Log.v(TAG, "onNext: joined!! opening game screen");
                        team.addPlayer(player);
                        onTeamJoined(team);
                    }

                    @Override
                    public void onError(Throwable e) {
                        //TODO Remove this when server is up...
                        Log.e(TAG, "onError: something happened, can't join the game.", e);
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
