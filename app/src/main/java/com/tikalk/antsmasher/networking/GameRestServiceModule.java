package com.tikalk.antsmasher.networking;

import com.google.gson.Gson;

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
    @Provides
    GameRestService provideGameRestService(Retrofit gameRestRetrofit) {

        return gameRestRetrofit.create(GameRestService.class);
    }

    @Provides
    public Retrofit provideRetrofit(OkHttpClient client, @Named("PlainGson") Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(ApiContract.SERVICE_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

}
