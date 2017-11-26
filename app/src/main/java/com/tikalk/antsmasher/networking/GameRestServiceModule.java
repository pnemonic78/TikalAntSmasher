package com.tikalk.antsmasher.networking;

import com.google.gson.Gson;
import com.tikalk.antsmasher.data.PrefsHelper;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by motibartov on 23/11/2017.
 */

@Module
public class GameRestServiceModule {

    public static String apiBaseUrl = "http://localhost";


    public GameRestServiceModule(){

    }

    @Provides
    GameRestService provideGameRestService(Retrofit gameRestRetrofit) {
        return gameRestRetrofit.create(GameRestService.class);
    }

    @Provides
    public Retrofit provideRetrofit(OkHttpClient client, @Named("PlainGson") Gson gson, PrefsHelper prefsHelper) {
        return new Retrofit.Builder()
                .baseUrl(ApiContract.ADMIN_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }


    public static void updateServiceBaseUrl(String baseUrl){
        apiBaseUrl = baseUrl;
    }

}
