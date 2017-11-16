package com.antsmasher.tikakl.tikalantsmasher.injection.module;

import android.app.Activity;
import android.content.Context;

import com.antsmasher.tikakl.tikalantsmasher.injection.component.ApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    @ApplicationScope
    Activity provideActivity() {
        return mActivity;
    }

    @Provides
    @ApplicationScope
    Context providesContext() {
        return mActivity;
    }
}
