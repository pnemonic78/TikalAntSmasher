package com.tikalk.antsmasher.networking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by motibartov on 16/11/2017.
 */

public class ApiClient {

    private static final String TAG = "ApiClient";

    //    private static final String BASE_URL = "https://planet.tikalk.com/timetracker/time.php/";
    private RestApiService restApiService;

    public RestApiService getApiService() {


        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClientBuilder.addInterceptor(logging);

        OkHttpClient okHttpClient = okHttpClientBuilder.build();

        restApiService = new Retrofit.Builder()
                .baseUrl(ApiContract.DEVICES_REST_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(RestApiService.class);

        return restApiService;

    }
}


