package com.tikalk.antsmasher;


import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;

import javax.inject.Named;
import javax.inject.Singleton;

import com.tikalk.antsmasher.board.BoardViewModelFactory;
import com.tikalk.antsmasher.data.PrefsHelper;
import com.tikalk.antsmasher.teams.TeamsViewModelFactory;

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
    @Named("Teams")
    ViewModelProvider.Factory provideTeamsViewModelFactory(TeamsViewModelFactory factory) {
        return factory;
    }

    @Provides
    @Named("Board")
    ViewModelProvider.Factory provideBoardViewModelFactory(BoardViewModelFactory factory) {
        return factory;
    }
}
