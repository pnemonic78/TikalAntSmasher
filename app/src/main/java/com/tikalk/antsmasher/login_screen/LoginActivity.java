package com.tikalk.antsmasher.login_screen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import javax.inject.Inject;

import com.tikalk.antsmasher.AntApplication;
import com.tikalk.antsmasher.R;
import com.tikalk.antsmasher.base.Presenter;
import com.tikalk.antsmasher.data.PrefsHelper;
import com.tikalk.antsmasher.service.AppService;
import com.tikalk.antsmasher.teams.TeamsActivity;

public class LoginActivity extends AppCompatActivity implements
        EditDialogFragment.EditDialogEventListener,
        IpDialogFragment.IpDialogEventListener,
        LoginContract.View {

    private static final String TAG = "TAG_LoginActivity";

    @Inject
    protected LoginPresenter mLoginPresenter;

    @Inject
    protected PrefsHelper mPrefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.v(TAG, "onCreate: ");

        ((AntApplication) getApplication()).getApplicationComponent().inject(this);
        mLoginPresenter.setView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLoginPresenter.checkBaseIp();
    }

    @Override
    public void onUserNameEntered(String value) {
        mLoginPresenter.saveUserName(value);
    }

    @Override
    public void showEnterIpDialog() {
        IpDialogFragment dialog = new IpDialogFragment();
        Bundle b = new Bundle();
        b.putString(IpDialogFragment.EXTRA_TITLE, "Servers Base URL");
        b.putString(IpDialogFragment.EXTRA_LABEL, "Enter Server Base IP");
        b.putString(IpDialogFragment.EXTRA_VALUE, mLoginPresenter.getServerAuthority());
        dialog.setArguments(b);
        dialog.show(getSupportFragmentManager(), "EditAuthority");
    }

    @Override
    public void showInvalidIpDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Invalid IP Address")
                .setMessage("Please enter a valid IP in format:\nxxx.xxx.xxx.xxx")
                .setIcon(ActivityCompat.getDrawable(this, R.mipmap.ic_launcher))
                .setPositiveButton("Try Again", (dialogInterface, i) -> mLoginPresenter.checkBaseIp())
                .show();
    }

    @Override
    public void showUserNameDialog() {
        EditDialogFragment dialog = new EditDialogFragment();
        Bundle b = new Bundle();
        b.putString(EditDialogFragment.EXTRA_TITLE, getString(R.string.login_dialog_header));
        b.putString(EditDialogFragment.EXTRA_LABEL, getString(R.string.login_dialog_body));
        b.putString(IpDialogFragment.EXTRA_VALUE, mLoginPresenter.getUserName());
        dialog.setArguments(b);
        dialog.show(getSupportFragmentManager(), "EditUserName");
    }

    @Override
    public void showLoginFailedDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name))
                .setMessage("Login failed, please check your connection and try again.")
                .setIcon(ActivityCompat.getDrawable(this, R.mipmap.ic_launcher))
                .setCancelable(false)
                .setPositiveButton(R.string.ok_button, (dialogInterface, i) -> {
                    Toast.makeText(LoginActivity.this, "Goodbye...", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .show();
    }

    @Override
    public void completeSplash(long timeout) {
        Intent service = new Intent(LoginActivity.this, AppService.class);
        startService(service);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(LoginActivity.this, TeamsActivity.class);
            startActivity(intent);
            finish();
        }, timeout);
    }

    @Override
    public void onIpEntered(String input) {
        mLoginPresenter.onIpEntered(input);
    }

    @Override
    public void setPresenter(Presenter presenter) {
    }
}

