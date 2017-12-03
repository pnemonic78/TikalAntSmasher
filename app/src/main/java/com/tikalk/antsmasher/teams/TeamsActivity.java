package com.tikalk.antsmasher.teams;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.tikalk.antsmasher.AntApplication;
import com.tikalk.antsmasher.R;
import com.tikalk.antsmasher.board.BoardActivity;
import com.tikalk.antsmasher.data.PrefsHelper;
import com.tikalk.antsmasher.model.Player;
import com.tikalk.antsmasher.model.Team;
import com.tikalk.antsmasher.settings.SettingsActivity;
import com.tikalk.antsmasher.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeamsActivity extends AppCompatActivity implements
        TeamViewModel.View,
        TeamViewHolder.TeamViewHolderListener,
        Observer<List<Team>>, SharedPreferences.OnSharedPreferenceChangeListener {

    public static final int TEAMS_ACTIVITY = 100;
    private static final String TAG = "TAG_TeamsActivity";
    @Inject
    protected PrefsHelper prefsHelper;

    @BindView(android.R.id.list)
    protected RecyclerView listView;

    @BindView(R.id.swipeContainer)
    protected SwipeRefreshLayout swipeContainer;

    private TeamViewModel presenter;
    private TeamAdapter adapter;

    @Inject
    @Named("Teams")
    ViewModelProvider.Factory mViewModelFactory;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AntApplication) getApplication()).getApplicationComponent().inject(this);
        setContentView(R.layout.activity_teams);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        ButterKnife.bind(this);

        adapter = new TeamAdapter(this);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(adapter);

        presenter = ViewModelProviders.of(this, mViewModelFactory).get(TeamViewModel.class);
        presenter.setView(this);
        getLifecycle().addObserver(presenter);
        presenter.getTeams().observe(this, this);

        swipeContainer.setOnRefreshListener(() -> presenter.refreshTeams());
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onChanged(@Nullable List<Team> teams) {
        adapter.setData(teams);
        swipeContainer.setRefreshing(false);
    }

    @Override
    public void onTeamClick(Team team) {
        presenter.teamClicked(team);
    }

    public void onTeamJoined(Team team, Player player) {
        Intent intent = new Intent(this, BoardActivity.class);
        intent.putExtra(BoardActivity.EXTRA_TEAM, team.getId());
        intent.putExtra(BoardActivity.EXTRA_PLAYER, player.getId());
        startActivityForResult(intent, TEAMS_ACTIVITY);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TEAMS_ACTIVITY) {
            presenter.refreshTeams();
        }
    }

    @Override
    public void showFetchTeamsError(Throwable e) {
        //FIXME show error dialog.
        Toast.makeText(this, "Failed to fetch teams: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showJoinTeamError(Throwable e) {
        //FIXME show error dialog.
        Toast.makeText(this, "Failed to join team: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(PrefsHelper.BASE_IP)) {
            String prefIp = sharedPreferences.getString(key, "");
            Log.i(TAG, "prefsIp: " + prefIp);
            if (prefIp.isEmpty() || !Utils.validateIpAddress(prefIp)) {
                Log.i(TAG, "onSharedPreferenceChanged: invalid ip");
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Invalid IP");
                builder.setMessage("Please enter valid IP in format:\n\nXXX.XXX.XXX.XXX");
                builder.setIcon(ContextCompat.getDrawable(this, R.mipmap.ic_launcher));
                builder.setPositiveButton(R.string.ok_button, (dialogInterface, i) -> {});
                builder.show();
            } else {
                Log.i(TAG, "onSharedPreferenceChanged: restart app dialog");
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("New IP");
                builder.setMessage("IP Changed.\nRestart app to apply changes...");
                builder.setIcon(ContextCompat.getDrawable(this, R.mipmap.ic_launcher));
                builder.setPositiveButton("Restart", (dialogInterface, i) -> restartApplication());
                builder.show();
            }
        }
    }

    private void restartApplication() {
        Intent i = getPackageManager().
                getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}
