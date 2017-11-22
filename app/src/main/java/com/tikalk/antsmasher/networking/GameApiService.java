package com.tikalk.antsmasher.networking;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface GameApiService {

//    @Headers("Content-Type: application/json")

    //Game API requests
    @GET(ApiContract.LOGIN_ENDPOINT)
    Observable<String> login(@Query(ApiContract.LOGIN_ENDPOINT) String username);

    //Player API requests


}
