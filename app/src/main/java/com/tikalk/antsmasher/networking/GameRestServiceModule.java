package com.tikalk.antsmasher.networking;

import android.app.Application;
import android.util.Log;

import com.google.gson.Gson;

import javax.inject.Named;
import javax.inject.Singleton;

import com.tikalk.antsmasher.data.PrefsHelper;
import com.tikalk.antsmasher.networking.rest.GameRestService;


import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


@Module
public class GameRestServiceModule {

    private static final String TAG = "TAG_RestServiceModule";

    @Provides
    public Retrofit provideRetrofit(OkHttpClient client, @Named("PlainGson") Gson gson, PrefsHelper prefsHelper, Application application) {
        try {

            String baseUrl = prefsHelper.getBaseUrl(application.getApplicationContext());

            Log.i(TAG, "provideRetrofit: " + baseUrl);

            String URL = ApiContract.buildAdminBaseUrl(baseUrl);

            return new Retrofit.Builder()
                    .baseUrl(URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        } catch (Exception e) {
            return null;
        }
    }

    @Provides
    GameRestService provideGameRestService(Retrofit retrofit) {
        return retrofit.create(GameRestService.class);
    }

    @Provides
    @Singleton
    RetrofitContainer provideRetrofitContainer(OkHttpClient client, @Named("PlainGson") Gson gson, PrefsHelper prefsHelper, Application application){
        String baseUrl = prefsHelper.getBaseUrl(application.getApplicationContext());
        return new RetrofitContainer(gson, client,baseUrl);
    }


}
