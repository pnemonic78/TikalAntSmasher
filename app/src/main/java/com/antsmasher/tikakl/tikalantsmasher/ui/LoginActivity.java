package com.antsmasher.tikakl.tikalantsmasher.ui;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import com.antsmasher.tikakl.tikalantsmasher.data.PrefsConstants;
import com.antsmasher.tikakl.tikalantsmasher.data.PrefsHelper;
import com.antsmasher.tikakl.tikalantsmasher.ui.presenters.MainPresenter;
import com.antsmasher.tikakl.tikalantsmasher.ui.views.LoginFragment;
import com.tikalk.antsmasher.R;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    @Inject
    MainPresenter mMainPresenter;

    @Inject
    PrefsHelper mPrefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_login);

        if(mPrefsHelper != null && mPrefsHelper.getString(PrefsConstants.USER_NAME) == null){
            addLoginFragment ();
        }
    }

    private void addLoginFragment(){
        FragmentManager manager = getFragmentManager();
        Fragment frag = manager.findFragmentByTag(LoginFragment.MY_TAG);
        if (frag != null) {
            manager.beginTransaction().remove(frag).commit();
        }

        DialogFragment editNameDialog = LoginFragment.newInstance ();
        editNameDialog.show (manager,LoginFragment.MY_TAG);
    }
    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
    }
}

