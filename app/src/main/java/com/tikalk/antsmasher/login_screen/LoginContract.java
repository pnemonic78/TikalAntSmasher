package com.tikalk.antsmasher.login_screen;

import com.tikalk.antsmasher.base.MvpView;

public interface LoginContract {

    interface View extends MvpView {
        void showUserNameDialog();

        void showLoginFailedDialog();

        void completeSplash(long timeout);

        void restartApp();
    }

    interface Presenter {
        void login();
        void saveUserName(String userName);
        void loginFailedMessageDismissed();
    }
}
