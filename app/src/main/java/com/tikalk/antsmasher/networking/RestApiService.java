package com.tikalk.antsmasher.networking;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;


public interface RestApiService {

    @Headers("Content-Type: application/json")
    @GET(ApiContract.DEVICES_REST_URL)
    Observable<ResponseBody> sendRestRequest(@Body String request);

}
