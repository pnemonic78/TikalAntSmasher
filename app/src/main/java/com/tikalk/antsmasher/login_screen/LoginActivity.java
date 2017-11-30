package com.tikalk.antsmasher.login_screen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

    public static final long SPLASH_TIMEOUT = 3000;
    public static final long SPLASH_EDIT_TIMEOUT = 1000;

    @Inject
    LoginPresenter mLoginPresenter;

    @Inject
    PrefsHelper mPrefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.v(TAG, "onCreate: ");

        ((AntApplication) getApplication()).getApplicationComponent().inject(this);
        if (mLoginPresenter != null) {
            mLoginPresenter.setView(this);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mLoginPresenter.login();
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
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name))
                .setMessage("Login failed, please check your connection and try again.")
                .setIcon(ContextCompat.getDrawable(this, R.mipmap.ic_launcher))
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    Toast.makeText(LoginActivity.this, "Goodbye...", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .show();
    }

    @Override
    public void completeSplash(long timeout) {
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

