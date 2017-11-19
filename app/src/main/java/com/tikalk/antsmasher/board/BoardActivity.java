package com.tikalk.antsmasher.board;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tikalk.antsmasher.BuildConfig;
import com.tikalk.antsmasher.R;
import com.tikalk.antsmasher.model.Ant;
import com.tikalk.antsmasher.model.AntSpecies;
import com.tikalk.antsmasher.model.Game;
import com.tikalk.antsmasher.model.Team;

/**
 * Game board activity.
 */
public class BoardActivity extends AppCompatActivity implements
        BoardViewModel.View,
        Observer<Game>,
        BoardView.AntListener {

    private BoardView boardView;
    private BoardViewModel presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        presenter = ViewModelProviders.of(this).get(BoardViewModel.class);
        presenter.setView(this);
        presenter.getGame().observe(this, this);
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
        if (game != null) {
            boardView.clear();
            AntSpecies species;
            for (Team team : game.getTeams()) {
                species = team.getAntSpecies();

                for (Ant ant : species.getAllAnts()) {
                    boardView.addAnt(ant);
                }
            }
        }
    }

    @Override
    public void moveAnt(Ant ant) {
        boardView.moveTo(ant);
    }

    @Override
    public void paint() {
        boardView.postInvalidate();
    }

    @Override
    public void onGameFinished() {
        runOnUiThread(() -> {
            Toast.makeText(BoardActivity.this, "Game finished", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    @Override
    public void onAntTouch(@Nullable Integer antId) {
        presenter.onAntTouch(antId);
    }

    @Override
    public void smashAnt(Ant ant) {
        //TODO mini-vibrate
        boardView.smashAnt(ant);
    }
}
