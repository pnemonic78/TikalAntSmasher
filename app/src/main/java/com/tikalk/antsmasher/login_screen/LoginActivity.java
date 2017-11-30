package com.tikalk.antsmasher.login_screen;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

public class LoginActivity extends AppCompatActivity implements EditDialogFragment.EditDialogEventListener, IpDialogFragment.IpDialogEventListener, LoginContract.View {

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
        mLoginPresenter.checkBaseIp();
    }

    private void showLoginDialog() {
        EditDialogFragment dialog = new EditDialogFragment();
        Bundle b = new Bundle();
        b.putString("Title", getString(R.string.login_dialog_header));
        b.putString("Message", getString(R.string.login_dialog_body));
        dialog.setArguments(b);
        dialog.show(getSupportFragmentManager(), "EditDialog");
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
    }


    @Override
    public void onEditDone(String value) {
        mLoginPresenter.saveUserName(value);
    }


    @Override
    public void showEnterIpDialog() {
        IpDialogFragment dialog = new IpDialogFragment();
        Bundle b = new Bundle();
        b.putString("Title", "Servers Base Url");
        b.putString("Message", "Enter Server Base Url");
        dialog.setArguments(b);
        dialog.show(getSupportFragmentManager(), "IpDialo");

    }

    @Override
    public void showInvalidIpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Invalid IP Address");
        builder.setMessage("Please enter a valid IP in format:\nxxx.xxx.xxx.xxx");
        builder.setIcon(ContextCompat.getDrawable(this, R.mipmap.ic_launcher));
        builder.setPositiveButton("Try Again", (dialogInterface, i) -> mLoginPresenter.checkBaseIp());
        builder.create().show();
    }

    @Override
    public void showUserNameDialog() {
        showLoginDialog();
    }


    @Override
    public void showLoginFailedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage("Login filed, please check your connection and try again.");
        builder.setIcon(ContextCompat.getDrawable(this, R.mipmap.ic_launcher));
        builder.setCancelable(false);
        builder.setPositiveButton("OK", (dialogInterface, i) -> {
            Toast.makeText(LoginActivity.this, "Goodbye...", Toast.LENGTH_SHORT).show();
            finish();
        });

        builder.create().show();

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
    public void setPresenter(Object presenter) {

    }


    @Override
    public void onIpEntered(String input) {
        mLoginPresenter.onIpEntered(input);
    }
}

