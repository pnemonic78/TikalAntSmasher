package com.tikalk.antsmasher.networking;

import com.google.gson.Gson;

import android.util.Log;

import javax.inject.Inject;

import com.tikalk.antsmasher.networking.rest.GameRestService;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitContainer {

    private static final String TAG = "TAG_RetrofitContainer";

    private Retrofit retrofit;
    private Gson gson;
    private OkHttpClient client;
    private String baseUrl;
    private GameRestService restService;

    @Inject
    public RetrofitContainer(Gson gson, OkHttpClient client, String baseUrl) {
        this.gson = gson;
        this.client = client;
        this.baseUrl = baseUrl;
        initRetrofit();
    }

    public void initRetrofit() {
        if (retrofit == null || !retrofit.baseUrl().equals(baseUrl)) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        Log.i(TAG, "initRetrofit: base url: " + retrofit.baseUrl());
    }

    public void updateBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        initRetrofit();
        this.restService = null;
    }

    public GameRestService getRestService() {
        if (this.restService == null) {
            this.restService = retrofit.create(GameRestService.class);
        }
        return this.restService;
    }
}
