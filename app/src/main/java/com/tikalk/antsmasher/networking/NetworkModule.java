package com.tikalk.antsmasher.networking;

import com.google.gson.Gson;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by motibartov on 21/11/2017.
 */

@Module
public class NetworkModule {

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
        return new Gson();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
    }

}
