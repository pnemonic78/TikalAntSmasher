package com.tikalk.antsmasher.login_screen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tikalk.antsmasher.MyApplication;
import com.tikalk.antsmasher.R;
import com.tikalk.antsmasher.data.PrefsConstants;
import com.tikalk.antsmasher.service.AppService;
import com.tikalk.antsmasher.teams.TeamsActivity;

import javax.inject.Inject;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements EditDialogFragment.EditDialogEventListener {

    public static final String TAG = "TAG_" + LoginActivity.class.getSimpleName();
    public static final long SPLASH_TIMEOUT = 3000;
    public static final long SPLASH_EDIT_TIMEOUT = 1000;

    @Inject
    SplashPresenter mSplashPresenter;

    @Inject
    com.tikalk.antsmasher.data.PrefsHelper mPrefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_login);
        Log.i(TAG, "onCreate: ");

        MyApplication.getmApplicationComponent().injectLoginScreen(this);

        if(mPrefsHelper != null && mPrefsHelper.getString(PrefsConstants.USER_NAME) == null){

            Log.i(TAG, "About to open dialog");

            showLoginDialog ();
        }else{
            splash(SPLASH_TIMEOUT);
        }
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

        mPrefsHelper.saveUserName(value);
        splash(SPLASH_EDIT_TIMEOUT);
    }

    void splash(long splashTimeout){
        Intent service = new Intent(LoginActivity.this, AppService.class);
        startService(service);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(LoginActivity.this, TeamsActivity.class);
            startActivity(intent);
            finish();
        }, splashTimeout);
    }
   }

