package com.tikalk.antsmasher.networking;

import com.google.gson.Gson;

import android.app.Application;

import java.net.URISyntaxException;

import javax.inject.Named;
import javax.inject.Singleton;

import com.tikalk.antsmasher.data.PrefsHelper;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;


@Module
public class GameRestServiceModule {

    private static final String TAG = "TAG_RestServiceModule";

    @Provides
    RetrofitContainer provideRetrofitContainer(OkHttpClient client, @Named("PlainGson") Gson gson, PrefsHelper prefsHelper, Application application) {
        String baseUrl = null;
        try {
            baseUrl = ApiContract.buildAdminBaseUrl(prefsHelper.getServerName());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return new RetrofitContainer(gson, client, baseUrl);
    }

}
