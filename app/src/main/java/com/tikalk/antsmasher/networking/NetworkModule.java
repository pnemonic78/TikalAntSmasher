package com.tikalk.antsmasher.networking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Named;
import javax.inject.Singleton;

import com.tikalk.antsmasher.model.socket.SocketMessage;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

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
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(SocketMessage.class, new SocketMessageSerializer());
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        OkHttpClient client;
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClientBuilder.addInterceptor(logging);
        client = okHttpClientBuilder.build();
        return client;
    }

}
