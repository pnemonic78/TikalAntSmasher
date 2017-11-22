package com.tikalk.antsmasher.login_screen;

import android.util.Log;

import com.tikalk.antsmasher.networking.RestApiService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by motibartov on 22/11/2017.
 */

public class LoginManager implements LoginInterceptor {

    public static final String TAG = "LoginManager";
    RestApiService loginService;

    LoginManager(RestApiService apiService){
        this.loginService = apiService;
    }

    @Override
    public void login(String userName, OnLoginFinishedListener listener) {

        loginService.login(userName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String response) {
                        Log.i(TAG, "onNext: got user ID from server" + response);
                        listener.onLoginSuccess(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: login error", e);
                        listener.onLoginFailed(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
