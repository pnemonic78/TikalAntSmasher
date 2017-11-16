package com.tikalk.antsmasher;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.tikalk.antsmasher.data.PrefsConstants;
import com.tikalk.antsmasher.data.PrefsHelper;


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
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    Context provideContext() {
        return mApplication;
    }

    @Provides
    SharedPreferences providePrefsHelper() {
        return mApplication.getSharedPreferences (PrefsConstants.SHARED_PREFS_FILE,Context.MODE_PRIVATE);
    }

}
