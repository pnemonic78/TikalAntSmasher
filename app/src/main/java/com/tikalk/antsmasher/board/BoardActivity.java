package com.tikalk.antsmasher.board;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Looper;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.bumptech.glide.Glide;
import com.tikalk.antsmasher.AntApplication;
import com.tikalk.antsmasher.R;
import com.tikalk.antsmasher.data.PrefsHelper;
import com.tikalk.antsmasher.model.Ant;
import com.tikalk.antsmasher.model.Game;
import com.tikalk.antsmasher.model.Team;
import com.tikalk.antsmasher.teams.TeamViewModel;
import com.tikalk.antsmasher.utils.SoundHelper;

/**
 * Game board activity.
 */
public class BoardActivity extends AppCompatActivity implements
        BoardViewModel.View,
        Observer<Game>,
        BoardView.AntListener {

    private static final String TAG = "BoardActivity";

    public static final String EXTRA_TEAM = "team_id";
    public static final String EXTRA_PLAYER = "player_id";

    private static final int SOUND_MISS = 0;
    private static final int SOUND_MISTAKE = 1;
    private static final int SOUND_SMASH = 2;
    private static final int SOUND_SAME_TEAM = 3;
    private static final int SOUND_SMASH_OTHER = 4;

    @Inject
    protected PrefsHelper prefsHelper;

    @Inject
    @Named("Board")
    ViewModelProvider.Factory boardViewModelFactory;
    @Inject
    @Named("Teams")
    ViewModelProvider.Factory teamsViewModelFactory;

    private BoardView boardView;
    private BoardViewModel presenter;
    private TeamViewModel presenterTeams;
    private Game game;
    private SoundHelper soundHelper;
    private ProgressBar progressBar;
    private long teamId;
    private long playerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AntApplication) getApplication()).getApplicationComponent().inject(this);

        teamId = getIntent().getLongExtra(EXTRA_TEAM, 0);
        playerId = getIntent().getLongExtra(EXTRA_PLAYER, 0);

        setContentView(R.layout.activity_board);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.hide();
        }

        Glide.with(this)
                .asBitmap()
                .load(R.drawable.board)
                .into((ImageView) findViewById(R.id.board_bg));

        boardView = findViewById(R.id.board);
        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        boardView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        boardView.setAntListener(this);

        progressBar = findViewById(R.id.wait);

        presenter = ViewModelProviders.of(this, boardViewModelFactory).get(BoardViewModel.class);
        presenter.setView(this);
        presenter.setPlayerId(playerId);
        presenter.setTeamId(teamId);
        getLifecycle().addObserver(presenter);
        presenter.getGame().observe(this, this);

        presenterTeams = ViewModelProviders.of(this, teamsViewModelFactory).get(TeamViewModel.class);

        soundHelper = new SoundHelper(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus && (game != null)) {
            presenter.onBoardReady();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button.
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onChanged(@Nullable Game game) {
        this.game = game;

        if (game != null) {
            List<Team> teams = presenterTeams.getTeams().getValue();
            if (teams != null) {
                game.setTeams(teams);
            }
        }

        boardView.clear();
        if (hasWindowFocus()) {
            presenter.onBoardReady();
        }
    }

    @Override
    public void addAnt(Ant ant) {
        boardView.addAnt(ant);
    }

    @Override
    public void removeAnt(Ant ant) {
        boardView.removeAnt(ant);
    }

    @Override
    public void moveAnt(Ant ant) {
        boardView.moveTo(ant);
    }

    @Override
    public void paint() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            boardView.invalidate();
        } else {
            boardView.postInvalidate();
        }
    }

    @Override
    public void onGameStarted() {
        if (prefsHelper.isInteractiveMusic()) {
            soundHelper.playMusic();
        }
        runOnUiThread(() -> {
            progressBar.setVisibility(View.GONE);
        });
    }

    @Override
    public void onGameFinished() {
        Log.v(TAG, "onGameFinished: ");
        soundHelper.pauseMusic();
        if (!isDestroyed() && !isFinishing()) {
            runOnUiThread(() -> {
                showGameOverDialog();
                if (prefsHelper.isInteractiveSounds()) {
                    soundHelper.playGameOver();
                }
            });
        }
    }

    private void showGameOverDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name)).setMessage("Game Over").setIcon(ActivityCompat.getDrawable(this, R.mipmap.ic_launcher))
                .setPositiveButton(getText(android.R.string.ok), (dialogInterface, i) -> finish())
                .setCancelable(false)
                .show();
    }

    @Override
    public void onAntTouch(@Nullable String antId) {
        presenter.onAntTouch(antId);
    }

    @Override
    public void smashAnt(@Nullable Ant ant, boolean user) {
        Log.i(TAG, "smashAnt: by user:" + user);
        boardView.smashAnt(ant);

        if (ant == null) {// Case 1: this user hits nothing.
            playSound(SOUND_MISS);
        } else {
            final boolean userTeam = (game != null) && game.isSameTeam(teamId, ant);
            Log.i(TAG, "smashAnt: by userTeam:" + userTeam);
            if (user) {
                if (userTeam) {// Case 2: my user hits his my own team's ant (self hit).
                    playSound(SOUND_MISTAKE);
                } else {// Case 3: my user hits another team's ant.
                    playSound(SOUND_SMASH);
                }
            } else if (userTeam) {// Case 4: teammate hits our team's ant.
                playSound(SOUND_SAME_TEAM);
            } else {// Case 5: other user hits other team's ant.
                playSound(SOUND_SMASH_OTHER);
            }
        }
    }

    private void vibrate() {
        if (prefsHelper.isInteractiveVibrate()) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            if ((vibrator != null) && vibrator.hasVibrator()) {
                vibrator.vibrate(10L);
            }
        }
    }

    private void playSound(int type) {
        if (prefsHelper.isInteractiveSounds()) {
            switch (type) {
                case SOUND_MISS:
                    soundHelper.playMissed();
                    break;
                case SOUND_MISTAKE:
                    soundHelper.playNooo();
                    break;
                case SOUND_SAME_TEAM:
                    soundHelper.playOops();
                    break;
                case SOUND_SMASH:
                case SOUND_SMASH_OTHER:
                    soundHelper.playSmashedSound();
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        Log.v(TAG, "onDestroy");
        super.onDestroy();
        presenter.stop();
        soundHelper.dispose();
    }
}
