package com.antsmasher.tikakl.tikalantsmasher.splash_signin;

import com.antsmasher.tikakl.tikalantsmasher.ui.base.BasePresenter;
import com.antsmasher.tikakl.tikalantsmasher.ui.views.SigninMvpView;

import javax.inject.Inject;

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
