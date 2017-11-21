package com.tikalk.antsmasher.login_screen;

import javax.inject.Inject;

import com.tikalk.antsmasher.SigninMvpView;
import com.tikalk.antsmasher.base.BasePresenter;

import io.reactivex.disposables.Disposable;

/**
 * Created by tamirnoach on 23/10/2017.
 */

public class SplashPresenter extends BasePresenter<SigninMvpView> {

    private Disposable mDisposable;

    @Override
    public void attachView(SigninMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Inject
    public SplashPresenter() {
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mDisposable != null) mDisposable.dispose();
    }

    public void loadGameGroups() {
        checkViewAttached();
    }
}
