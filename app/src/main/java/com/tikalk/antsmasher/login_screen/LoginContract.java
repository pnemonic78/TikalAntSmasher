package com.tikalk.antsmasher.login_screen;

import com.tikalk.antsmasher.base.MvpView;

/**
 * Created by motibartov on 22/11/2017.
 */

public interface LoginContract {

    interface View extends MvpView {
        void showEnterIpDialog();

        void showInvalidIpDialog();

        void showUserNameDialog();

        void showLoginFailedDialog();

        void completeSplash(long timeout);
    }

    interface Presenter {
        void checkBaseIp();

        void onIpEntered(String enteredIp);

        void saveUserName(String userName);
    }
}
