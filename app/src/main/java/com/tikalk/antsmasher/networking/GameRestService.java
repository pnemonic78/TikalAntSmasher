package com.tikalk.antsmasher.networking;

import java.util.List;

import com.tikalk.antsmasher.model.Player;
import com.tikalk.antsmasher.model.Team;
import com.tikalk.antsmasher.model.User;
import com.tikalk.antsmasher.model.rest_response.CreateBody;
import com.tikalk.antsmasher.model.rest_response.LatestGame;
import com.tikalk.antsmasher.model.rest_response.LatestTeam;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;


public interface GameRestService {

    @Headers("Content-Type: application/json")
    @POST(ApiContract.LOGIN_ENDPOINT)
    Observable<User> createUser(@Body CreateBody createBody);

    @PUT(ApiContract.UPDATE_ENDPOINT)
    Observable<String> updateUser(@Query(ApiContract.UPDATE_ENDPOINT) String name);

    //createPlayer (join the game)
    @PUT(ApiContract.CREATE_PLAYER)
    Observable<Player> joinGame(@Query(ApiContract.TEAM_PARAM) long teamId, @Query(ApiContract.USERID_PARAM) String userId);

    @GET(ApiContract.GAMES)
    Observable<LatestGame> getLatestGame();

    @GET(ApiContract.TEAM_PARAM)
    Observable<List<Team>> getCurrentTeams();

    @GET(ApiContract.LEADERS)
    Observable<Player> getLeaderPlayer(); //Join the game

    @GET(ApiContract.LATEST_TEAMS)
    Observable<LatestTeam> getLatestTeams(); //Join the game

}
