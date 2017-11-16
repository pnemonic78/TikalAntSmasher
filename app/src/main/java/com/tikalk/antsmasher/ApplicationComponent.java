package com.tikalk.antsmasher;


import com.tikalk.antsmasher.login_screen.LoginActivity;

import dagger.Component;


@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    //    Context context();

    void injectViewModel(LoginActivity loginActivity);


}
