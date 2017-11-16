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
    protected final Context mContext;

    public ApplicationModule(Context context) {
        mContext = context;
    }


    @Provides
    Context provideContext() {
        return mContext;
    }

    @Provides
    PrefsHelper providePrefsHelper() {
        return new PrefsHelper(mContext);
    }

}
