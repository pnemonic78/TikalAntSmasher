package com.tikalk.antsmasher.login_screen;


import android.content.Context;
import android.util.Log;

import com.tikalk.antsmasher.base.BasePresenter;
import com.tikalk.antsmasher.data.PrefsConstants;
import com.tikalk.antsmasher.data.PrefsHelper;
import com.tikalk.antsmasher.networking.ApiClient;
import com.tikalk.antsmasher.networking.RestApiService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by tamirnoach on 23/10/2017.
 */

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presetner{

    public static final String TAG = "LoginPresenter";
    private Disposable mDisposable;

    private LoginContract.View view;
    private ApiClient apiClient;
    PrefsHelper prefsHelper;
    Context context;


    public LoginPresenter(Context context, LoginContract.View view, PrefsHelper prefsHelper) {
        this.view = view;
        apiClient = new ApiClient();
        this.prefsHelper = prefsHelper;
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
        String username = prefsHelper.getString(PrefsConstants.USER_NAME);
        if(username == null){
            view.showUserNameDialog();
        }else {
            checkUserId(username);
            //Splash
        }
    }

    private void checkUserId(String username) {

        if(prefsHelper.getString(PrefsConstants.USER_ID) == null){
            Log.i(TAG, "checkUserId: about to login to server");
            // loginWithRetrofit(username); //TODO enable once server is ready
            view.completeSplash(LoginActivity.SPLASH_EDIT_TIMEOUT); //TODO remove when server ready
        }else {
            view.completeSplash(LoginActivity.SPLASH_EDIT_TIMEOUT);
        }
    }

    private void loginWithRetrofit(String username) {
        Log.i(TAG, "getDataWithRetrofit called");
        RestApiService apiService = apiClient.getApiService();

        apiService.login(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String response) {
                        Log.i(TAG, "onNext: got user ID from server" + response);
                        prefsHelper.saveStringToPrefs(PrefsConstants.USER_ID, response);
                        view.completeSplash(LoginActivity.SPLASH_EDIT_TIMEOUT);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void saveUserName(String userName) {
        prefsHelper.saveUserName(userName);
        checkUserId(userName);
    }


}
