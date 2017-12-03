package com.tikalk.antsmasher.login_screen;

import android.app.Application;

import javax.inject.Singleton;

import com.tikalk.antsmasher.data.PrefsHelper;
import com.tikalk.antsmasher.networking.RetrofitContainer;

import dagger.Module;
import dagger.Provides;


@Module
public class LoginModule {

    @Provides
    @Singleton
    LoginPresenter provideLoginPresenter(Application context, PrefsHelper prefsHelper, RetrofitContainer retrofitContainer) {
        return new LoginPresenter(context, prefsHelper, retrofitContainer);
    }
}
