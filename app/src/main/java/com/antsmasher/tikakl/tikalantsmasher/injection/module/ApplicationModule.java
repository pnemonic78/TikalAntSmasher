package com.antsmasher.tikakl.tikalantsmasher.injection.module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.antsmasher.tikakl.tikalantsmasher.data.PrefsConstants;
import com.antsmasher.tikakl.tikalantsmasher.data.PrefsHelper;
import com.antsmasher.tikakl.tikalantsmasher.injection.component.ApplicationScope;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Provide application-level dependencies.
 */
@Module
public class ApplicationModule {
    protected final Application mApplication;
    protected final SharedPreferences mPrefsHelper;

    public ApplicationModule(Application application) {
        mApplication = application;
        mPrefsHelper = application.getSharedPreferences (PrefsConstants.SHARED_PREFS_FILE,Context.MODE_PRIVATE);
    }

    @Provides
    @ApplicationScope
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationScope
    Context provideContext() {
        return mApplication;
    }

    @Provides
    @ApplicationScope
    SharedPreferences providePrefsHelper() {
        return mApplication.getSharedPreferences (PrefsConstants.SHARED_PREFS_FILE,Context.MODE_PRIVATE);
    }

}
