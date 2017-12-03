package com.tikalk.antsmasher.login_screen;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import javax.inject.Inject;

import com.tikalk.antsmasher.base.BasePresenter;
import com.tikalk.antsmasher.data.PrefsHelper;
import com.tikalk.antsmasher.model.User;
import com.tikalk.antsmasher.networking.ApiContract;
import com.tikalk.antsmasher.networking.RetrofitContainer;
import com.tikalk.antsmasher.networking.rest.GameRestService;
import com.tikalk.antsmasher.utils.Utils;

import java.net.URISyntaxException;

import io.reactivex.disposables.Disposable;

/**
 * Created by tamirnoach on 23/10/2017.
 */

public class LoginPresenter extends BasePresenter<LoginContract.View> implements
        LoginContract.Presenter,
        LoginInterceptor.OnLoginFinishedListener {

    public static final String TAG = "TAG_LoginPresenter";
    private Disposable mDisposable;

    private LoginContract.View view;
    private Context context;

    private LoginManager loginManager;

    PrefsHelper prefsHelper;


    RetrofitContainer retrofitContainer;


    @Inject
    public LoginPresenter(Context context, PrefsHelper prefsHelper, RetrofitContainer retrofitContainer) {
        this.context = context;
        this.prefsHelper = prefsHelper;
        this.retrofitContainer = retrofitContainer;
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
        String baseIp = prefsHelper.getServerAuthority();
        Log.i(TAG, "checkBaseIp: " + baseIp);

        if (prefsHelper.isServerAuthorityEmpty()) {
            view.showEnterIpDialog();
        } else {
            Log.i(TAG, "checkBaseIp: proceed to login");
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
            try {
                retrofitContainer.updateBaseUrl(ApiContract.buildAdminBaseUrl(enteredIp));
                Log.i(TAG, "onIpEntered: " + enteredIp);
                login();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
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
            //((AntApplication) (context.getApplicationContext())).getApplicationComponent().inject(this);
            loginManager = new LoginManager(retrofitContainer.getRestService());
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
    public void onLoginSuccess(User user) {
        prefsHelper.setUserId(user.getId());
        view.completeSplash(LoginActivity.SPLASH_EDIT_TIMEOUT);
    }

    @Override
    public void onLoginFailed(Throwable e) {
        view.showLoginFailedDialog();
        //  view.completeSplash(LoginActivity.SPLASH_EDIT_TIMEOUT);
    }
}
