package com.tikalk.antsmasher;

import android.app.Application;


/**
 * Created by tamirnoach on 24/10/2017.
 */

public class MyApplication extends Application {

    static ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule (this))
                .build();
    }

    public ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {

        }
        return mApplicationComponent;
    }

    // Needed to replace the component with a test specific one

    public static ApplicationComponent getmApplicationComponent(){
        return mApplicationComponent;
    }
}
