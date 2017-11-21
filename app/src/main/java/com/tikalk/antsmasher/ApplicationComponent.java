package com.tikalk.antsmasher;


import javax.inject.Singleton;

import com.tikalk.antsmasher.login_screen.LoginActivity;
import com.tikalk.antsmasher.networking.NetworkModule;
import com.tikalk.antsmasher.service.AppService;

import dagger.Component;


@Component(modules = {ApplicationModule.class, NetworkModule.class})
@Singleton
public interface ApplicationComponent {

    void inject(LoginActivity loginActivity);

    void inject(AppService appService);

}
