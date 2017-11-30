package com.tikalk.antsmasher.networking;

import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;

import javax.inject.Named;

import com.tikalk.antsmasher.data.PrefsHelper;
import com.tikalk.antsmasher.networking.rest.GameRestService;

import java.net.URISyntaxException;

import dagger.Lazy;
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
    public Retrofit provideRetrofit(OkHttpClient client, @Named("PlainGson") Gson gson, PrefsHelper prefsHelper) {
        try {
            String URL = ApiContract.buildAdminBaseUrl(prefsHelper.getStringPref(PrefsHelper.BASE_IP));

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
}
