package com.tikalk.antsmasher.networking;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;


public interface RestApiService {

    @Headers("Content-Type: application/json")
    @GET(ApiContract.LOGIN_ENDPOINT)
    Observable<String> login(@Query(ApiContract.LOGIN_ENDPOINT) String username);

}
