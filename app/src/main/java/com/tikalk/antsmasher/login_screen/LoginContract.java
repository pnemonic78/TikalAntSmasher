package com.tikalk.antsmasher.login_screen;

import com.tikalk.antsmasher.base.MvpView;

/**
 * Created by motibartov on 22/11/2017.
 */

public interface LoginContract {

    interface View extends MvpView<Presenter> {
        void showUserNameDialog();

        void showLoginFailedDialog();

        void completeSplash(long timeout);
    }

    interface Presenter<V extends View> extends com.tikalk.antsmasher.base.Presenter<V> {
        void onResume();

        void saveUserName(String userName);
    }
}
