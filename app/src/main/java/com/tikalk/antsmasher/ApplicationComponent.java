package com.tikalk.antsmasher;


import com.tikalk.antsmasher.login_screen.LoginActivity;
import com.tikalk.antsmasher.service.AppService;

import javax.inject.Singleton;

import dagger.Component;


@Component(modules = ApplicationModule.class)
@Singleton
public interface ApplicationComponent {
    
    void inject(LoginActivity loginActivity);

    void inject(AppService appService);

}
