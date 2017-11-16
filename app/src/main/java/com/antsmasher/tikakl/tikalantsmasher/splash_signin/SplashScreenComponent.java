package com.antsmasher.tikakl.tikalantsmasher.splash_signin;

import com.antsmasher.tikakl.tikalantsmasher.injection.component.ApplicationComponent;

import dagger.Component;



@LoginScope
@Component(dependencies = ApplicationComponent.class)
public interface SplashScreenComponent {
    void injectSplashScreen(LoginActivity loginActivity);
}
