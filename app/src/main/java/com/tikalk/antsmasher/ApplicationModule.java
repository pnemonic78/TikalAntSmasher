package com.tikalk.antsmasher;


import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;

import javax.inject.Singleton;

import com.tikalk.antsmasher.data.PrefsHelper;
import com.tikalk.antsmasher.teams.ViewModelFactory;

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


    @Provides
    ViewModelProvider.Factory provideTeamsViewModelFactory(ViewModelFactory factory){
        return factory;
    }
}
