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
import com.tikalk.antsmasher.model.DeveloperTeam;
import com.tikalk.antsmasher.model.Team;
import com.tikalk.antsmasher.settings.SettingsActivity;
import com.tikalk.antsmasher.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeamsActivity extends AppCompatActivity implements
        TeamViewModel.View,
        TeamViewHolder.TeamViewHolderListener,
        Observer<List<Team>> , IpDialogFragment.EditDialogEventListener{

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
        presenter.getTeams(this).observe(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(prefsHelper.getStringPref(PrefsHelper.ADMIN_IP))
                || TextUtils.isEmpty(prefsHelper.getStringPref(PrefsHelper.ANTS_IP))
                || TextUtils.isEmpty(prefsHelper.getStringPref(PrefsHelper.SMASH_IP))) {
//            chooseDeveloperTeam();
            enterIpDialog();
        }
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

    private void enterIpDialog() {
        IpDialogFragment dialogFragment = new IpDialogFragment();
        Bundle b = new Bundle();
        b.putString("Title", getString(R.string.dev_ip_dialog_header));
        b.putString("Message", getString(R.string.dev_ip_dialog_body));
        b.putString(PrefsHelper.ANTS_IP, prefsHelper.getStringPref(PrefsHelper.ANTS_IP));
        b.putString(PrefsHelper.ADMIN_IP, prefsHelper.getStringPref(PrefsHelper.ADMIN_IP));
        b.putString(PrefsHelper.SMASH_IP, prefsHelper.getStringPref(PrefsHelper.SMASH_IP));
        dialogFragment.setArguments(b);
        dialogFragment.show(getSupportFragmentManager(), "IpDialog");
    }


    @Override
    public void onEditDone(String enteredAntIp, String enteredAdminIp, String enteredSmashIp) {

        if(!Utils.validateIpAddress(enteredAntIp) || !Utils.validateIpAddress(enteredAdminIp) || !Utils.validateIpAddress(enteredSmashIp)){
            Log.i(TAG, "onEditDone: ip invalid");
            new AlertDialog.Builder(this)
                    .setIcon(ContextCompat.getDrawable(this, R.mipmap.ic_launcher))
                    .setTitle(R.string.invalid_ip_header)
                    .setMessage(R.string.invalid_ip_body)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            enterIpDialog();
                        }
                    }).create().show();
        }else{
            prefsHelper.saveStringPref(PrefsHelper.ANTS_IP, "http://" + enteredAntIp);
            prefsHelper.saveStringPref(PrefsHelper.ADMIN_IP, "http://" + enteredAdminIp);
            prefsHelper.saveStringPref(PrefsHelper.SMASH_IP, "http://" + enteredSmashIp);
        }

        Log.i(TAG, "onEditDone: ip valid");



    }
}
