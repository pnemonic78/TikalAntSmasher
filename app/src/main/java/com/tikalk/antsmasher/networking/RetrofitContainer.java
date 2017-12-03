package com.tikalk.antsmasher.networking;

import com.google.gson.Gson;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitContainer {

    private Retrofit retrofit;
    private Gson gson;
    private OkHttpClient client;
    private String baseUrl;


    @Inject
    public RetrofitContainer(Gson gson, OkHttpClient client, String baseUrl){
        this.gson = gson;
        this.client = client;
        this.baseUrl = baseUrl;
    }



    public Retrofit getRetrofit() {
       retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

       return retrofit;
    }

    public void updateBaseUrl(String baseUrl){
        this.baseUrl = baseUrl;
        getRetrofit();
    }
}
