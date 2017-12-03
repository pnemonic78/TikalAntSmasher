package com.tikalk.antsmasher.login_screen;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import javax.inject.Inject;

import com.tikalk.antsmasher.AntApplication;
import com.tikalk.antsmasher.base.BasePresenter;
import com.tikalk.antsmasher.data.PrefsHelper;
import com.tikalk.antsmasher.model.User;
import com.tikalk.antsmasher.networking.rest.GameRestService;
import com.tikalk.antsmasher.utils.Utils;

import io.reactivex.disposables.Disposable;

/**
 * Created by tamirnoach on 23/10/2017.
 */

public class LoginPresenter extends BasePresenter<LoginContract.View> implements
        LoginContract.Presenter,
        LoginInterceptor.OnLoginFinishedListener {

    private static final String TAG = "TAG_LoginPresenter";

    private static final long SPLASH_TIMEOUT = 3000;
    private static final long SPLASH_EDIT_TIMEOUT = 1000;

    private Disposable mDisposable;

    private LoginContract.View view;
    private Context context;

    LoginManager loginManager;

    PrefsHelper prefsHelper;


    @Inject
    GameRestService gameRestService;


    @Inject
    public LoginPresenter(Context context, PrefsHelper prefsHelper) {
        this.context = context;
        this.prefsHelper = prefsHelper;
    }

    public void setView(LoginContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mDisposable != null) mDisposable.dispose();
    }

    public void loadGameGroups() {
        checkViewAttached();
    }

    @Override
    public void checkBaseIp() {
        if (prefsHelper.isServerAuthorityEmpty()) {
            view.showEnterIpDialog();
        } else {
            login();
        }
    }

    @Override
    public void onIpEntered(String enteredIp) {
        boolean ipIsValid = Utils.validateIpAddress(enteredIp);
        if (!ipIsValid) {
            view.showInvalidIpDialog();
        } else {
            prefsHelper.setServerAuthority(enteredIp);
            Log.i(TAG, "onIpEntered: " + enteredIp);
            login();
        }
    }

    public void login() {
        String username = prefsHelper.getUserName();
        if (TextUtils.isEmpty(username)) {
            view.showUserNameDialog();
        } else {
            checkUserId(username);
        }
    }

    private void checkUserId(String username) {
        if (prefsHelper.getUserId() == 0) {
            Log.v(TAG, "checkUserId: about to createUser to server");
            ((AntApplication) (context.getApplicationContext())).getApplicationComponent().inject(this);
            loginManager = new LoginManager(gameRestService);
            loginManager.login(username, this);
        } else {
            view.completeSplash(SPLASH_TIMEOUT);
        }
    }

    @Override
    public void saveUserName(String userName) {
        prefsHelper.setUserName(userName);
        checkUserId(userName);
    }

    @Override
    public void onLoginSuccess(User user) {
        prefsHelper.setUserId(user.getId());
        view.completeSplash(SPLASH_EDIT_TIMEOUT);
    }

    @Override
    public void onLoginFailed(Throwable e) {
        view.showLoginFailedDialog();
        //  view.completeSplash(LoginActivity.SPLASH_EDIT_TIMEOUT);
    }
}
