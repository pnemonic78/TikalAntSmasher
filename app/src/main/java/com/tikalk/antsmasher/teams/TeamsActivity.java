package com.tikalk.antsmasher.teams;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.tikalk.antsmasher.AntApplication;
import com.tikalk.antsmasher.R;
import com.tikalk.antsmasher.board.BoardActivity;
import com.tikalk.antsmasher.data.PrefsHelper;
import com.tikalk.antsmasher.login_screen.IpDialogFragment;
import com.tikalk.antsmasher.model.DeveloperTeam;
import com.tikalk.antsmasher.model.Player;
import com.tikalk.antsmasher.model.Team;
import com.tikalk.antsmasher.settings.SettingsActivity;
import com.tikalk.antsmasher.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeamsActivity extends AppCompatActivity implements
        TeamViewModel.View,
        TeamViewHolder.TeamViewHolderListener,
        Observer<List<Team>> {

    private static final String TAG = "TAG_TeamsActivity";
    @Inject
    protected PrefsHelper prefsHelper;

    @BindView(android.R.id.list)
    protected RecyclerView listView;

    private TeamViewModel presenter;
    private TeamAdapter adapter;

    @Inject
    @Named("Teams")
    ViewModelProvider.Factory mViewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AntApplication) getApplication()).getApplicationComponent().inject(this);
        setContentView(R.layout.activity_teams);
        ButterKnife.bind(this);

        adapter = new TeamAdapter(this);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(adapter);

        presenter = ViewModelProviders.of(this, mViewModelFactory).get(TeamViewModel.class);
        presenter.setView(this);
        presenter.getTeams().observe(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onChanged(@Nullable List<Team> teams) {
        adapter.setData(teams);
    }

    @Override
    public void onTeamClick(Team team) {
        presenter.teamClicked(team);
    }

    public void onTeamJoined(Team team, Player player) {
        Intent intent = new Intent(this, BoardActivity.class);
        intent.putExtra(BoardActivity.EXTRA_TEAM, team.getId());
        intent.putExtra(BoardActivity.EXTRA_PLAYER, player.getId());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void chooseDeveloperTeam() {
        DeveloperTeam[] teams = DeveloperTeam.values();
        final int length = teams.length;
        final CharSequence[] items = new CharSequence[length];
        for (int i = 0; i < teams.length; i++) {
            items[i] = teams[i].getName();
        }
        DeveloperTeam selected = DeveloperTeam.find(prefsHelper.getDeveloperTeam());

        new AlertDialog.Builder(this)
                .setTitle(R.string.dev_team_choose)
                .setNegativeButton(android.R.string.cancel, null)
                .setSingleChoiceItems(items, (selected != null) ? selected.ordinal() : -1, (dialog, which) -> {
                    DeveloperTeam team = teams[which];
                    prefsHelper.setDeveloperTeam(team.getId());
                    dialog.dismiss();
                })
                .show();
    }
}
