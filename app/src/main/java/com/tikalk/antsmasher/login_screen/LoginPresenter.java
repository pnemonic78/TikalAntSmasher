package com.tikalk.antsmasher.login_screen;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.tikalk.antsmasher.base.BasePresenter;
import com.tikalk.antsmasher.data.PrefsHelper;
import com.tikalk.antsmasher.networking.ApiClient;
import com.tikalk.antsmasher.networking.GameRestService;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * Created by tamirnoach on 23/10/2017.
 */

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presetner, LoginInterceptor.OnLoginFinishedListener {

    public static final String TAG = "LoginPresenter";
    private Disposable mDisposable;

    private LoginContract.View view;
    private ApiClient apiClient;
    PrefsHelper prefsHelper;
    Context context;

    LoginManager loginManager;


    @Inject
    public LoginPresenter(Context context, PrefsHelper prefsHelper, GameRestService gameRestService) {
        apiClient = new ApiClient();
        this.prefsHelper = prefsHelper;
        loginManager = new LoginManager(gameRestService);
        this.context = context;
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
    public void login() {
        String username = prefsHelper.getUserName();
        if (TextUtils.isEmpty(username)) {
            view.showUserNameDialog();
        } else {
            checkUserId(username);
        }
    }

    private void checkUserId(String username) {
        if (TextUtils.isEmpty(prefsHelper.getUserId())) {
            Log.v(TAG, "checkUserId: about to createUser to server");
            loginManager.login(username, this);
        } else {
            view.completeSplash(LoginActivity.SPLASH_TIMEOUT);
        }
    }

    @Override
    public void saveUserName(String userName) {
        prefsHelper.setUserName(userName);
        checkUserId(userName);
    }

    @Override
    public void onLoginSuccess(String userId) {
        prefsHelper.setUserId(userId);
        view.completeSplash(LoginActivity.SPLASH_EDIT_TIMEOUT);
    }

    @Override
    public void onLoginFailed(Throwable e) {
        view.completeSplash(LoginActivity.SPLASH_EDIT_TIMEOUT);
        //view.loginFailed();
    }
}
