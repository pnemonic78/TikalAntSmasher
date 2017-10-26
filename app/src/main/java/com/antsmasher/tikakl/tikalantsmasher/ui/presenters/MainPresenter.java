package com.antsmasher.tikakl.tikalantsmasher.ui.presenters;

import android.support.annotation.NonNull;

import com.antsmasher.tikakl.tikalantsmasher.MyApplication;
import com.antsmasher.tikakl.tikalantsmasher.ui.base.BasePresenter;
import com.antsmasher.tikakl.tikalantsmasher.ui.base.MvpView;
import com.antsmasher.tikakl.tikalantsmasher.ui.views.SigninMvpView;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by tamirnoach on 23/10/2017.
 */

public class MainPresenter extends BasePresenter<SigninMvpView> {

    private Disposable mDisposable;

    @Override
    public void attachView(SigninMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Inject
    public MainPresenter() {
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
