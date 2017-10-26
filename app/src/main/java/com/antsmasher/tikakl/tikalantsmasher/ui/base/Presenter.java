package com.antsmasher.tikakl.tikalantsmasher.ui.base;

/**
 * Created by tamirnoach on 23/10/2017.
 */

public interface Presenter<V extends MvpView> {

    void attachView(V mvpView);

    void detachView();
}