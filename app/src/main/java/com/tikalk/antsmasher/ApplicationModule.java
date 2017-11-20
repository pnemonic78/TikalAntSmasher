package com.tikalk.antsmasher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.content.Context;

import javax.inject.Named;
import javax.inject.Singleton;

import com.tikalk.antsmasher.data.PrefsHelper;
import com.tikalk.antsmasher.model.SocketMessage;
import com.tikalk.antsmasher.model.SocketMessageSerializer;

import dagger.Module;
import dagger.Provides;

/**
 * Provide application-level dependencies.
 */
@Module
@Singleton
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
