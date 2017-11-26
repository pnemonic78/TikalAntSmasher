package com.tikalk.antsmasher.login_screen;


import com.tikalk.antsmasher.model.User;

public interface LoginInterceptor {

    interface OnLoginFinishedListener {
        void onLoginSuccess(User user);

        void onLoginFailed(Throwable e);
    }

    void login(String userName, OnLoginFinishedListener listener);
}
