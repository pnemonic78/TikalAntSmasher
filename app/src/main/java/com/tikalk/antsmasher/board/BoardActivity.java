package com.tikalk.antsmasher.board;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Looper;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import javax.inject.Inject;
import javax.inject.Named;

import com.tikalk.antsmasher.AntApplication;
import com.tikalk.antsmasher.R;
import com.tikalk.antsmasher.data.PrefsHelper;
import com.tikalk.antsmasher.model.Ant;
import com.tikalk.antsmasher.model.Game;
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

    @Inject
    protected PrefsHelper prefsHelper;

    @Inject @Named("Board")
    ViewModelProvider.Factory mBoardViewModelFactory;


    private BoardView boardView;
    private BoardViewModel presenter;
    private Game game;
    private SoundHelper soundHelper;
    private ProgressBar progressBar;
    private String teamId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AntApplication) getApplication()).getApplicationComponent().inject(this);

        teamId = getIntent().getStringExtra(EXTRA_TEAM);

        setContentView(R.layout.activity_board);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.hide();
        }

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

        presenter = ViewModelProviders.of(this, mBoardViewModelFactory).get(BoardViewModel.class);
        presenter.setView(this);
        getLifecycle().addObserver(presenter);
        presenter.getGame().observe(this, this);

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

        boardView.clear();
        if (game != null) {
            for (Ant ant : game.getAllAnts()) {
                boardView.addAnt(ant);
            }
        }
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
    public void smashAnt(@NonNull Ant ant, boolean user) {
        boardView.smashAnt(ant);
        final boolean sound = prefsHelper.isInteractiveSounds();
        if (user) {
            if (prefsHelper.isInteractiveVibrate()) {
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                if ((vibrator != null) && vibrator.hasVibrator()) {
                    vibrator.vibrate(10L);
                }
            }
            if (sound) {
                soundHelper.playSmashedSound();
            }
        } else if ((game != null) && game.isSameTeam(teamId, ant)) {
            if (sound) {
                //TODO play sound that smashed team's ant
            }
        } else {
            if (sound) {
                soundHelper.playOops();
            }
        }
        boardView.smashAnt(ant);
    }

    @Override
    protected void onDestroy() {
        Log.v(TAG, "onDestroy");
        super.onDestroy();
        presenter.stop();
        soundHelper.dispose();
    }
}
