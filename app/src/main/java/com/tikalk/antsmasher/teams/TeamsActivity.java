package com.tikalk.antsmasher.teams;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.tikalk.antsmasher.R;
import com.tikalk.antsmasher.model.Team;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tikalk.graphics.ImageUtils.tintImage;

public class TeamsActivity extends AppCompatActivity implements TeamViewHolder.TeamViewHolderListener {

    @BindView(android.R.id.list)
    protected RecyclerView listView;

    private TeamAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams);
        ButterKnife.bind(this);

        Bitmap ant = BitmapFactory.decodeResource(getResources(), R.drawable.ant);
        List<Team> data = new ArrayList<>();//TODO delete me!
        data.add(new Team(10, "Army", tintImage(ant, Color.GREEN)));
        data.add(new Team(20, "Fire", tintImage(ant, Color.RED)));
        data.add(new Team(30, "Black", tintImage(ant, Color.BLACK)));
        adapter = new TeamAdapter(this);
        adapter.setData(data);

        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(adapter);
    }

    @Override
    public void onTeamClick(Team team) {
        joinTeam(team);
    }

    private void joinTeam(Team team) {
        Toast.makeText(this, "Team joined: " + team.getName(), Toast.LENGTH_SHORT).show();
        //TODO tell the ViewModel, and it will send REST call.
    }
}
