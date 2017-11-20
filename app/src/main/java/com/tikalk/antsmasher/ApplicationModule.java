package com.tikalk.antsmasher;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
public class ApplicationModule {
    private final Context context;

    public ApplicationModule(Context context) {
        this.context = context.getApplicationContext();
    }

    @Provides
    Context provideContext() {
        return context;
    }

    @Provides
    PrefsHelper providePrefsHelper() {
        return new PrefsHelper(context);
    }

    @Provides
    @Named("PlainGson")
    @Singleton
    Gson providePlainGson() {
        return new Gson();
    }

    @Provides
    @Named("SocketMessageGson")
    @Singleton
    Gson provideSocketGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(SocketMessage.class, new SocketMessageSerializer());
        return gsonBuilder.create();
    }

}
