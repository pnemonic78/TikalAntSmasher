package com.tikalk.antsmasher.login_screen;


public interface LoginInterceptor {

    interface OnLoginFinishedListener{
        void onLoginSuccess(long userUniqueId);
        void onLoginFailed(Throwable e);
    }

    void login(String userName, OnLoginFinishedListener listener);
}
