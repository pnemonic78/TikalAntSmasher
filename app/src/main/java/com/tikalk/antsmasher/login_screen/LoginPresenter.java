package com.tikalk.antsmasher.login_screen;

import android.text.TextUtils;
import android.util.Log;

import javax.inject.Inject;

import com.tikalk.antsmasher.base.BasePresenter;
import com.tikalk.antsmasher.data.PrefsHelper;
import com.tikalk.antsmasher.model.User;
import com.tikalk.antsmasher.networking.rest.GameRestService;

import io.reactivex.disposables.Disposable;

/**
 * Created by tamirnoach on 23/10/2017.
 */

public class LoginPresenter extends BasePresenter<LoginContract.View> implements
        LoginContract.Presenter<LoginContract.View>,
        LoginInterceptor.OnLoginFinishedListener {

    private static final String TAG = "TAG_LoginPresenter";

    private static final long SPLASH_TIMEOUT = 3000;
    private static final long SPLASH_EDIT_TIMEOUT = 1000;

    private Disposable mDisposable;

    private LoginContract.View view;
    private PrefsHelper prefsHelper;

    LoginManager loginManager;

    @Inject
    public LoginPresenter(PrefsHelper prefsHelper, GameRestService gameRestService) {
        this.prefsHelper = prefsHelper;
        this.loginManager = new LoginManager(gameRestService);
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
    public void onResume() {
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
