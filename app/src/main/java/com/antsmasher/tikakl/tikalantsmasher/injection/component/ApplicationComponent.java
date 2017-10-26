package com.antsmasher.tikakl.tikalantsmasher.injection.component;

import android.app.Application;

import com.antsmasher.tikakl.tikalantsmasher.data.PrefsHelper;
import com.antsmasher.tikakl.tikalantsmasher.injection.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    //    Context context();
    Application provideApplication();
    PrefsHelper providePreferencesHelper();
}
