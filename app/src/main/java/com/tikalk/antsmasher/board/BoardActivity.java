package com.tikalk.antsmasher.board;

import android.app.Service;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
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

import com.tikalk.antsmasher.R;
import com.tikalk.antsmasher.model.Ant;
import com.tikalk.antsmasher.model.Game;
import com.tikalk.antsmasher.model.socket.AntLocation;
import com.tikalk.antsmasher.model.socket.AntSmash;
import com.tikalk.antsmasher.service.AppService;

/**
 * Game board activity.
 */
public class BoardActivity extends AppCompatActivity implements
        BoardViewModel.View,
        Observer<Game>,
        BoardView.AntListener,
        AppService.AppServiceEventListener {

    private static final String TAG = "BoardActivity";

    private BoardView boardView;
    private BoardViewModel presenter;
    private Game game;
    private AppService.AppServiceProxy appService;//FIXME move to BoardViewModel
    private boolean serviceBound = false;
    private Intent serviceIntent;

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
    protected void onStart() {
        super.onStart();
        serviceIntent = new Intent(this, AppService.class);
        bindService(serviceIntent, connection, Service.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isChangingConfigurations()) {
            //This means that onStop called due to screen rotation, therefore it's crucial to unbind
            //the service and clean its reference to insure that the destroyed Activity reference will be released.
            //Service will rebound in onStart after mActivity recreation.
            //If this is not due to screen rotation service will be killed in onDestroy ( stopService() ).
            unbindService(connection); //This will not stop the service as it started with startService()
            serviceBound = false;
            appService = null;
        } else {
            /*
            *This means that onStop called due 2 possible reasons:
             1. Activity is going down because user hit the back button ( --> destroyed)
             The Service will be cleaned and stopped in onDestroy()
             2. The user is leaving to another mActivity such as web browser, a phone call, or hit the home button (--> stopped)
             The service will keep running in background until activity resumes
            * */
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
    public void onGameFinished() {
        if (!isDestroyed() && !isFinishing()) {
            runOnUiThread(() -> {
                showGameOverDialog();
            });
        }
    }

    private void showGameOverDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name)).setMessage("Game Over").setIcon(ActivityCompat.getDrawable(this, R.mipmap.ic_launcher))
                .setPositiveButton(getText(android.R.string.ok), (dialogInterface, i) -> finish())
                .show();
    }

    @Override
    public void onAntTouch(@Nullable String antId) {
        presenter.onAntTouch(antId);
    }

    @Override
    public void smashAnt(Ant ant, boolean user) {
        if (user && (ant != null)) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            if ((vibrator != null) && vibrator.hasVibrator()) {
                vibrator.vibrate(10L);
            }
        }
        boardView.smashAnt(ant);
    }

    @Override
    public void sendSmash(AntSmash event) {
        appService.smashAnt(event);
    }

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            AppService.LocalBinder binder = (AppService.LocalBinder) service;

            appService = binder.getService();
            serviceBound = true;
            appService.registerServiceEventListener(BoardActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i(TAG, "onServiceDisconnected: ");
            serviceBound = false;
            appService = null;
        }
    };

    @Override
    public void onAntMoved(AntLocation locationEvent) {
        presenter.onAntMoved(locationEvent);
    }

    @Override
    public void onAntSmashed(AntSmash smashEvent) {
        presenter.onAntSmashed(smashEvent);
    }

    @Override
    protected void onDestroy() {
        if (isFinishing()) {
            Log.i(TAG, "onDestroy: exiting...");
            if (appService != null && serviceBound) {
                unbindService(connection);
                stopService(serviceIntent);
            }
        }
        super.onDestroy();
        presenter.stop();
    }
}
