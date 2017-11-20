package com.tikalk.antsmasher;

import android.app.Application;


/**
 * Created by tamirnoach on 24/10/2017.
 */

public class AntApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onTerminate() {
        super.onTerminate();
        applicationComponent = null;
    }

    public ApplicationComponent getApplicationComponent() {
        if (applicationComponent == null) {
            applicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return applicationComponent;
    }
}
