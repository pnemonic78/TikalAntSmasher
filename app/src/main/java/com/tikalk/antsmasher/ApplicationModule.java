package com.tikalk.antsmasher;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tikalk.antsmasher.data.PrefsConstants;
import com.tikalk.antsmasher.data.PrefsHelper;
import com.tikalk.antsmasher.model.SocketMessage;
import com.tikalk.antsmasher.model.SocketMessageSerializer;


import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Provide application-level dependencies.
 */
@Module
class ApplicationModule {
    private final Context mContext;

    ApplicationModule(Context context) {
        mContext = context;
    }


    @Provides
    Context provideContext() {
        return mContext;
    }

    @Provides
    PrefsHelper providePrefsHelper() {
        return new PrefsHelper(mContext);
    }


    @Provides
    @Singleton
    @Named("PlainGson")
    Gson providePlainGson(){
        return new Gson();
    }

    @Provides
    @Singleton
    @Named("SocketMessageGson")
    Gson provideSocketGson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(SocketMessage.class, new SocketMessageSerializer());
        return gsonBuilder.create();
    }

}
