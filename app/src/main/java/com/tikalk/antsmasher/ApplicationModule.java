package com.tikalk.antsmasher;


import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;

import javax.inject.Named;
import javax.inject.Singleton;

import com.tikalk.antsmasher.board.BoardVmFactory;
import com.tikalk.antsmasher.data.PrefsHelper;
import com.tikalk.antsmasher.teams.TeamsVmFactory;

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


    @Provides @Named("Teams")
    ViewModelProvider.Factory provideTeamsViewModelFactory(TeamsVmFactory factory){
        return factory;
    }

    @Provides @Named("Board")
    ViewModelProvider.Factory provideBoardViewModelFactory(BoardVmFactory factory){
        return factory;
    }
}
