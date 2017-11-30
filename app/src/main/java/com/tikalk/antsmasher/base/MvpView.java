package com.tikalk.antsmasher.base;

/**
 * Created by tamirnoach on 23/10/2017.
 */

public interface MvpView<P extends Presenter> {

    void setPresenter(P presenter);
}
