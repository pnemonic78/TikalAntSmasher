package com.tikalk.antsmasher.login_screen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import javax.inject.Inject;

import com.tikalk.antsmasher.AntApplication;
import com.tikalk.antsmasher.R;
import com.tikalk.antsmasher.data.PrefsHelper;
import com.tikalk.antsmasher.service.AppService;
import com.tikalk.antsmasher.teams.TeamsActivity;

public class LoginActivity extends AppCompatActivity implements
        EditDialogFragment.EditDialogEventListener,
        LoginContract.View {

    private static final String TAG = "TAG_LoginActivity";

    public static final String ACTION_ASK_NAME = "action.ASK_USER_NAME";

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
            case ACTION_ASK_NAME:
                dismissAfterEdit = intent.getBooleanExtra(EXTRA_DISMISS, false);
                showUserNameDialog();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!dismissAfterEdit) {
            mLoginPresenter.onResume();
        }
    }

    private void showLoginDialog() {
        EditDialogFragment dialog = new EditDialogFragment();
        Bundle b = new Bundle();
        b.putString(EditDialogFragment.EXTRA_TITLE, getString(R.string.login_dialog_header));
        b.putString(EditDialogFragment.EXTRA_LABEL, getString(R.string.login_dialog_body));
        dialog.setArguments(b);
        dialog.show(getSupportFragmentManager(), "LoginDialog");
    }

    @Override
    public void onEditDone(String value) {
        mLoginPresenter.saveUserName(value);
    }

    @Override
    public void showUserNameDialog() {
        showLoginDialog();
    }

    @Override
    public void showLoginFailedDialog() {
        final Context context = this;
        new AlertDialog.Builder(context)
                .setTitle(getString(R.string.app_name))
                .setMessage("Login failed, please check your connection and try again.")
                .setIcon(ContextCompat.getDrawable(this, R.mipmap.ic_launcher))
                .setCancelable(false)
                .setPositiveButton(R.string.ok_button, (dialogInterface, i) -> {
                    Toast.makeText(context, "Goodbye...", Toast.LENGTH_SHORT).show();
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

        final Context context = this;
        Intent service = new Intent(context, AppService.class);
        startService(service);

        getWindow().getDecorView().postDelayed(() -> {
            Intent intent = new Intent(context, TeamsActivity.class);
            startActivity(intent);
            finish();
        }, timeout);
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
    }
}

