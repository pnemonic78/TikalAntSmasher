package com.tikalk.antsmasher.login_screen;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.net.URISyntaxException;

import javax.inject.Inject;

import com.tikalk.antsmasher.R;
import com.tikalk.antsmasher.base.BasePresenter;
import com.tikalk.antsmasher.data.PrefsHelper;
import com.tikalk.antsmasher.model.User;
import com.tikalk.antsmasher.networking.ApiContract;
import com.tikalk.antsmasher.networking.RetrofitContainer;
import com.tikalk.antsmasher.utils.Utils;

import io.reactivex.disposables.Disposable;

public class LoginPresenter extends BasePresenter<LoginContract.View> implements
        LoginContract.Presenter,
        LoginInterceptor.OnLoginFinishedListener {

    private static final String TAG = "TAG_LoginPresenter";

    private static final long SPLASH_TIMEOUT = 3000;
    private static final long SPLASH_EDIT_TIMEOUT = 1000;

    private Disposable mDisposable;

    private LoginContract.View view;
    private final Context context;

    private LoginManager loginManager;

    private PrefsHelper prefsHelper;


    private RetrofitContainer retrofitContainer;


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

//    @Override
//    public void checkBaseIp() {
//        if (prefsHelper.isServerAuthorityEmpty()) {
//            view.showEnterIpDialog();
//        } else {
//            login();
//        }
//    }

    public void login() {

        if(prefsHelper.getServerName() == null || prefsHelper.getServerName().isEmpty()){
            //Create server name preferences, set it to default
            prefsHelper.saveStringToPrefs(context.getString(R.string.server_name_key), context.getString(R.string.default_server_name));

            try {
                retrofitContainer.updateBaseUrl(ApiContract.buildAdminBaseUrl(prefsHelper.getServerName()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

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

    @Override
    public void loginFailedMessageDismissed() {
        view.completeSplash(0);
    }


}
