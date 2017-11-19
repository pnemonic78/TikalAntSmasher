package com.tikalk.antsmasher.teams;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import java.util.List;

import com.tikalk.antsmasher.board.BoardViewModel;
import com.tikalk.antsmasher.model.Game;
import com.tikalk.antsmasher.model.Team;

/**
 * Team presenter.
 */

public class TeamViewModel extends ViewModel {

    public interface View {
        void onTeamJoined(Team team);
    }

    private View view;
    private MutableLiveData<List<Team>> teams;
    private Team team;

    public void setView(View view) {
        this.view = view;
    }

    public LiveData<List<Team>> getTeams(Context context) {
        if (teams == null) {
            teams = new MutableLiveData<>();
            loadTeams(context);
        }
        return teams;
    }

    private void loadTeams(Context context) {
        // TODO Do an asynchronous operation to fetch teams.
        Game game = new Game();
        BoardViewModel.populateGame(game);
        teams.setValue(game.getTeams());
    }

    public void teamClicked(Team team) {
        onTeamJoined(team); // TODO Do an asynchronous operation to join the team.
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
