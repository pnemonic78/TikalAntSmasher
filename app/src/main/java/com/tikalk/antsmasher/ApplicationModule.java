package com.tikalk.antsmasher;


import android.app.Application;

import javax.inject.Singleton;

import com.tikalk.antsmasher.data.PrefsHelper;
import com.tikalk.antsmasher.login_screen.SplashPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Provide application-level dependencies.
 */
@Module
@Singleton
class ApplicationModule {
    Application mApplication;

    ApplicationModule(Application application) {
        mApplication = application;
    }


    @Provides
    PrefsHelper providePrefsHelper() {
        return new PrefsHelper(mApplication);
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return mApplication;
    }

}
