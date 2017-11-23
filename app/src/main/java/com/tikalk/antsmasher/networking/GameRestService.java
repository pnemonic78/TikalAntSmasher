package com.tikalk.antsmasher.networking;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;


public interface GameRestService {

//    @Headers("Content-Type: application/json")

    //Game API requests
    @POST(ApiContract.LOGIN_ENDPOINT)
    Observable<String> createUser(@Query(ApiContract.LOGIN_ENDPOINT) String username);

    @PUT(ApiContract.LOGIN_ENDPOINT)
    Observable<String> updateUser(@Query(ApiContract.LOGIN_ENDPOINT) String username);

    @PUT(ApiContract.LOGIN_ENDPOINT)
    Observable<String> createPlayer(@Query(ApiContract.LOGIN_ENDPOINT) String username); //Join the game

    @GET(ApiContract.LOGIN_ENDPOINT)
    Observable<String> getCurrentTeams(@Query(ApiContract.LOGIN_ENDPOINT) String username); //Join the game




    //Player API requests


}
