package com.tikalk.antsmasher.teams;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import com.tikalk.antsmasher.R;
import com.tikalk.antsmasher.model.Team;

import static com.tikalk.graphics.ImageUtils.tintImage;

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
        final Resources res = context.getResources();
        Bitmap ant = BitmapFactory.decodeResource(res, R.drawable.ant_normal);
        List<Team> data = new ArrayList<>();//TODO delete me!
        data.add(new Team(30, "Black", tintImage(ant, Color.BLACK)));
        data.add(new Team(10, "Army", tintImage(ant, 0xFF008000)));
        data.add(new Team(20, "Fire", tintImage(ant, 0xFFCC0000)));
        teams.setValue(data);
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
