package com.tikalk.antsmasher;

import javax.inject.Singleton;

import com.tikalk.antsmasher.login_screen.LoginActivity;
import com.tikalk.antsmasher.networking.AppWebSocket;
import com.tikalk.antsmasher.networking.NetworkModule;
import com.tikalk.antsmasher.service.AppService;
import com.tikalk.antsmasher.teams.TeamsActivity;

import dagger.Component;

@Component(modules = {ApplicationModule.class, NetworkModule.class})
@Singleton
public interface ApplicationComponent {

    void inject(LoginActivity activity);

    void inject(AppService service);

    void inject(AppWebSocket appWebSocket);

    void inject(TeamsActivity activity);
}
