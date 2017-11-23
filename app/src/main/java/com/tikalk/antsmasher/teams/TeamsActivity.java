package com.tikalk.antsmasher.teams;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import com.tikalk.antsmasher.R;
import com.tikalk.antsmasher.board.BoardActivity;
import com.tikalk.antsmasher.model.Team;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeamsActivity extends AppCompatActivity implements
        TeamViewModel.View,
        TeamViewHolder.TeamViewHolderListener,
        Observer<List<Team>> {

    @BindView(android.R.id.list)
    protected RecyclerView listView;

    private TeamViewModel presenter;
    private TeamAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams);
        ButterKnife.bind(this);

        adapter = new TeamAdapter(this);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(adapter);

        presenter = ViewModelProviders.of(this).get(TeamViewModel.class);
        presenter.setView(this);
        presenter.getTeams(this).observe(this, this);
    }

    @Override
    public void onChanged(@Nullable List<Team> teams) {
        adapter.setData(teams);
    }

    @Override
    public void onTeamClick(Team team) {
        presenter.teamClicked(team);
    }

    public void onTeamJoined(Team team) {
        Intent intent = new Intent(this, BoardActivity.class);
        intent.putExtra(BoardActivity.EXTRA_TEAM, team.getId());
        startActivity(intent);
    }
}
