package com.tikalk.antsmasher;


import com.tikalk.antsmasher.login_screen.LoginActivity;
import com.tikalk.antsmasher.service.AppService;

import dagger.Component;


@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    //    Context context();

    void injectLoginScreen(LoginActivity loginActivity);
    void injectAppService(AppService appService);

}
