package com.tikalk.antsmasher.login_screen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import javax.inject.Inject;

import com.tikalk.antsmasher.AntApplication;
import com.tikalk.antsmasher.R;
import com.tikalk.antsmasher.base.Presenter;
import com.tikalk.antsmasher.data.PrefsHelper;
import com.tikalk.antsmasher.service.AppService;
import com.tikalk.antsmasher.teams.TeamsActivity;
import com.tikalk.antsmasher.utils.Utils;

public class LoginActivity extends AppCompatActivity implements
        EditDialogFragment.EditDialogEventListener,
        LoginContract.View {

    private static final String TAG = "TAG_LoginActivity";

    public static final String ACTION_ASK_IP = "com.tikalk.antsmasher.action.ASK_IP";
    public static final String ACTION_ASK_NAME = "com.tikalk.antsmasher.action.ASK_USER_NAME";

    public static final String EXTRA_DISMISS = "dismiss_after";

    @Inject
    protected LoginPresenter mLoginPresenter;

    @Inject
    protected PrefsHelper mPrefsHelper;

    private boolean dismissAfterEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.v(TAG, "onCreate: ");

        ((AntApplication) getApplication()).getApplicationComponent().inject(this);
        mLoginPresenter.setView(this);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(@NonNull Intent intent) {
        final String action = intent.getAction();
        if (action == null) {
            return;
        }
        dismissAfterEdit = false;
        switch (action) {
            case ACTION_ASK_IP:
                dismissAfterEdit = intent.getBooleanExtra(EXTRA_DISMISS, false);
                break;
            case ACTION_ASK_NAME:
                dismissAfterEdit = intent.getBooleanExtra(EXTRA_DISMISS, false);
                showUserNameDialog();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLoginPresenter.login();
    }

    @Override
    public void onUserNameEntered(String value) {
        mLoginPresenter.saveUserName(value);
    }

    @Override
    public void showUserNameDialog() {
        EditDialogFragment dialog = new EditDialogFragment();
        Bundle args = new Bundle();
        args.putString(EditDialogFragment.EXTRA_TITLE, getString(R.string.login_dialog_header));
        args.putString(EditDialogFragment.EXTRA_LABEL, getString(R.string.login_dialog_body));
        args.putString(EditDialogFragment.EXTRA_VALUE, mPrefsHelper.getUserName());
        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), "EditUserName");
    }

    @Override
    public void showLoginFailedDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.login_dialog_header))
                .setMessage(R.string.login_dialog_failed)
                .setIcon(ActivityCompat.getDrawable(this, R.mipmap.ic_launcher))
                .setPositiveButton(R.string.ok_button, (dialogInterface, i) -> {
                    finish();
                })
                .show();
    }

    @Override
    public void completeSplash(long timeout) {
        if (dismissAfterEdit) {
            finish();
            return;
        }

        Intent service = new Intent(this, AppService.class);
        startService(service);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(this, TeamsActivity.class);
            startActivity(intent);
            finish();
        }, timeout);
    }

    @Override
    public void setPresenter(Presenter presenter) {
        mLoginPresenter = (LoginPresenter) presenter;
    }

    @Override
    public void restartApp() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.ip_dialog_title)
                .setMessage(R.string.ip_restart_reason)
                .setIcon(ActivityCompat.getDrawable(this, R.mipmap.ic_launcher))
                .setPositiveButton(R.string.restart_button, (dialogInterface, i) -> Utils.restartApp(this))
                .show();
    }
}

