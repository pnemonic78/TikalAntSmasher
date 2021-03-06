package com.tikalk.antsmasher.board;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.bumptech.glide.Glide;
import com.tikalk.antsmasher.AntApplication;
import com.tikalk.antsmasher.R;
import com.tikalk.antsmasher.data.PrefsHelper;
import com.tikalk.antsmasher.media.SoundHelper;
import com.tikalk.antsmasher.model.Ant;
import com.tikalk.antsmasher.model.Game;
import com.tikalk.antsmasher.model.Player;
import com.tikalk.antsmasher.model.Team;
import com.tikalk.antsmasher.teams.TeamViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Game board activity.
 */
public class BoardActivity extends AppCompatActivity implements
        BoardViewModel.View,
        Observer<Game>,
        BoardView.AntListener, GameOverDialogFragment.GameOverDialogListener, ProgressDialogFragment.ProgressDialogEventListener {

    private static final String TAG = "BoardActivity";

    public static final String EXTRA_TEAM = "team_id";
    public static final String EXTRA_PLAYER = "player_id";

    private static final int SOUND_MISS = 0;
    private static final int SOUND_MISTAKE = 1;
    private static final int SOUND_SMASH = 2;
    private static final int SOUND_SAME_TEAM = 3;
    private static final int SOUND_SMASH_OTHER = 4;


    private static final int NOT_STARTED = 1;
    private static final int STARTED = 2;
    private static final int FINISHED = 3;

    private int gameState = NOT_STARTED;

    @Inject
    protected PrefsHelper prefsHelper;

    @Inject
    @Named("Board")
    ViewModelProvider.Factory boardViewModelFactory;
    @Inject
    @Named("Teams")
    ViewModelProvider.Factory teamsViewModelFactory;

    @BindView(R.id.board)
    protected BoardView boardView;
    ProgressDialogFragment progressDialogFragment;
    @BindView(R.id.tvPlayerName)
    protected TextView playerScoreText;

    private BoardViewModel presenter;
    private TeamViewModel presenterTeams;
    private Game game;
    private SoundHelper soundHelper;
    private long teamId;
    private long playerId;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AntApplication) getApplication()).getApplicationComponent().inject(this);
        setContentView(R.layout.activity_board);
        ButterKnife.bind(this);

        teamId = getIntent().getLongExtra(EXTRA_TEAM, 0);
        playerId = getIntent().getLongExtra(EXTRA_PLAYER, 0);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.hide();
        }

        Glide.with(this)
                .asBitmap()
                .load(R.drawable.board)
                .into((ImageView) findViewById(R.id.board_bg));

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

        presenter = ViewModelProviders.of(this, boardViewModelFactory).get(BoardViewModel.class);
        presenter.setView(this);
        presenter.setPlayerId(playerId);
        presenter.setTeamId(teamId);
        getLifecycle().addObserver(presenter);
        presenter.getGame(true).observe(this, this);

        presenterTeams = ViewModelProviders.of(this, teamsViewModelFactory).get(TeamViewModel.class);

        soundHelper = new SoundHelper(this);

        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if ((vibrator != null) && vibrator.hasVibrator()) {
            this.vibrator = vibrator;
        }
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
            Player player = game.getPlayer(playerId);
            Team team;
            TextView teamScoreText;
            Bitmap bitmap;
            Drawable drawable;
            final int size = teams.size();
            final Resources res = getResources();
            final int antWidth = res.getDimensionPixelSize(R.dimen.ant_width);
            final int antHeight = res.getDimensionPixelSize(R.dimen.ant_height);

            for (int i = 0; i < size; i++) {

                team = teams.get(i);

                if (team.contains(player)) {
                    bitmap = boardView.getAntAlive(team.getAntSpecies());
                    drawable = new BitmapDrawable(res, bitmap);
                    drawable.setBounds(0, 0, antWidth, antHeight);
                    drawable.setAlpha(150);
                    playerScoreText.setCompoundDrawablesRelative(null, null, drawable, null);
                    playerScoreText.setText(prefsHelper.getUserName());
                    playerScoreText.setTextColor(team.getAntSpecies().getTint());
                }
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
    protected void onResume() {
        super.onResume();

        switch (gameState){
            case NOT_STARTED:
                if(progressDialogFragment != null && progressDialogFragment.getDialog() != null){
                    progressDialogFragment.dismiss();
                }
                progressDialogFragment = new ProgressDialogFragment();
                progressDialogFragment.show(getSupportFragmentManager(), "ProgressDialog");
                break;
            case STARTED:
                break;
            case FINISHED:
                Log.i(TAG, "onResume: game finished");
                break;
        }


    }

    @Override
    public void onGameStarted() {
        gameState = STARTED;
        if (prefsHelper.isInteractiveMusic()) {
            soundHelper.playMusic();
        }
        runOnUiThread(() -> {
            if (progressDialogFragment != null)
                progressDialogFragment.dismiss();
        });
    }

    @Override
    public void onGameFinished(List<Team> teams, Player winner) {
        Log.v(TAG, "onGameFinished: ");
        gameState = FINISHED;
        soundHelper.pauseMusic();
        if (!isDestroyed() && !isFinishing()) {
            presenter.stop();
            runOnUiThread(() -> showGameOverDialog(teams, winner));
            if (prefsHelper.isInteractiveSounds()) {
                soundHelper.playGameOver();
            }
        }
    }

    private void showGameOverDialog(List<Team> teams, Player winner) {
        Bundle scoreBoard = null;
        if(teams != null && winner != null){
            scoreBoard = new Bundle();
            scoreBoard.putParcelable(GameOverDialogFragment.EXTRA_TEAM1, teams.get(0));
            scoreBoard.putParcelable(GameOverDialogFragment.EXTRA_TEAM2, teams.get(1));
            scoreBoard.putParcelable(GameOverDialogFragment.EXTRA_TEAM3, teams.get(2));
            scoreBoard.putParcelable(GameOverDialogFragment.EXTRA_WINNER, winner);
            Log.i(TAG, "showGameOverDialog: " + scoreBoard);
        }

        GameOverDialogFragment dialog = new GameOverDialogFragment();
        Bundle args = new Bundle();
        args.putString(GameOverDialogFragment.EXTRA_TITLE, getString(R.string.app_name));

        if(scoreBoard != null){
            args.putString(GameOverDialogFragment.EXTRA_LABEL, getString(R.string.score_summary));
            args.putBundle(GameOverDialogFragment.EXTRA_SCOREBOARD, scoreBoard);
        }else {
            args.putString(GameOverDialogFragment.EXTRA_LABEL, getString(R.string.game_over));
        }
        dialog.setArguments(args);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(dialog, null);
        ft.commitAllowingStateLoss();
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
                vibrate();
                if (userTeam) {// Case 2: my user hits his my own team's ant (self hit).
                    showToast("Self Smash be careful");
                    playSound(SOUND_MISTAKE);
                } else {// Case 3: my user hits another team's ant.
                    showToast("Smash...");
                    playSound(SOUND_SMASH);
                }
            } else if (userTeam) {// Case 4: teammate hits our team's ant.
                showToast("Bad luck");
                playSound(SOUND_SAME_TEAM);
            } else {// Case 5: other user hits other team's ant.
                showToast("Lost it!");
                playSound(SOUND_SMASH_OTHER);
            }
        }
    }

    private void vibrate() {
        if (prefsHelper.isInteractiveVibrate() && (vibrator != null)) {
            vibrator.vibrate(20L);
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
        soundHelper.dispose();
    }

    @Override
    public void showFetchGameError(Throwable e) {
        //FIXME show error dialog.
        Toast.makeText(this, "Failed to fetch game: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDialogClosed() {
        finish();
    }

    @Override
    public void onProgressDialogClosed() {
        Log.i(TAG, "onProgressDialogClosed: ");
        presenter.startGame();
    }

    @Override
    public void onProgressBarCanceled() {
        finish();
    }

    private void showToast(String message){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BoardActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
