package com.tikalk.antsmasher.login_screen;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.tikalk.antsmasher.base.BasePresenter;
import com.tikalk.antsmasher.data.PrefsHelper;
import com.tikalk.antsmasher.networking.ApiClient;

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


    public LoginPresenter(Context context, LoginContract.View view, PrefsHelper prefsHelper) {
        this.view = view;
        apiClient = new ApiClient();
        this.prefsHelper = prefsHelper;
        loginManager = new LoginManager(apiClient.getApiService());
        view.setPresenter(this);
        this.context = context;
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
            Log.i(TAG, "checkUserId: about to login to server");
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
