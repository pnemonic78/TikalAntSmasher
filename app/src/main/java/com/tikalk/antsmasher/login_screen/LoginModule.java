package com.tikalk.antsmasher.login_screen;

import javax.inject.Singleton;

import com.tikalk.antsmasher.data.PrefsHelper;
import com.tikalk.antsmasher.networking.rest.GameRestService;

import dagger.Module;
import dagger.Provides;

/**
 * Created by motibartov on 23/11/2017.
 */

@Module
public class LoginModule {

    @Provides
    @Singleton
    LoginPresenter provideLoginPresenter(PrefsHelper prefsHelper, GameRestService restApiService) {
        return new LoginPresenter(prefsHelper, restApiService);
    }
}
