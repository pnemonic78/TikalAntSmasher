package com.antsmasher.tikakl.tikalantsmasher.injection.component;

import android.app.Application;

import com.antsmasher.tikakl.tikalantsmasher.data.PrefsHelper;
import com.antsmasher.tikakl.tikalantsmasher.injection.module.ApplicationModule;
import com.antsmasher.tikakl.tikalantsmasher.splash_signin.LoginActivity;

import dagger.Component;


@ApplicationScope
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    //    Context context();

    void injectViewModel(LoginActivity loginActivity);

    Application provideApplication();
    PrefsHelper providePreferencesHelper();
}
