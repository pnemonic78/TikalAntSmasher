package com.antsmasher.tikakl.tikalantsmasher;

import android.app.Application;
import android.content.Context;

import com.antsmasher.tikakl.tikalantsmasher.injection.component.ApplicationComponent;
import com.antsmasher.tikakl.tikalantsmasher.injection.component.DaggerApplicationComponent;
import com.antsmasher.tikakl.tikalantsmasher.injection.module.ApplicationModule;

/**
 * Created by tamirnoach on 24/10/2017.
 */

public class MyApplication extends Application {

    ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static MyApplication get(Context context) {
        return (MyApplication) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule (this))
                    .build();
        }
        return mApplicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }
}
