package com.antsmasher.tikakl.tikalantsmasher.splash_signin;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tikalk.antsmasher.MyApplication;
import com.antsmasher.tikakl.tikalantsmasher.data.PrefsConstants;
import com.antsmasher.tikakl.tikalantsmasher.data.PrefsHelper;
import com.tikalk.antsmasher.R;

import javax.inject.Inject;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements EditDialogFragment.EditDialogEventListener {

    public static final String TAG = "TAG_" + LoginActivity.class.getSimpleName();

    @Inject
    SplashPresenter mSplashPresenter;

    @Inject
    PrefsHelper mPrefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_login);
        Log.i(TAG, "onCreate: ");


        if(mPrefsHelper != null && mPrefsHelper.getString(PrefsConstants.USER_NAME) == null){

            Log.i(TAG, "About to open dialog");

            showLoginDialog ();
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doAfterLogin();
                }
            }, 3000);
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
        doAfterLogin();
    }

    private void doAfterLogin() {

    }
}

