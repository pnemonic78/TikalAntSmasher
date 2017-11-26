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

import static com.tikalk.antsmasher.networking.ApiContract.CREATE_PLAYER;
import static com.tikalk.antsmasher.networking.ApiContract.GAMES;
import static com.tikalk.antsmasher.networking.ApiContract.ID_PARAM;
import static com.tikalk.antsmasher.networking.ApiContract.LATEST_TEAMS;
import static com.tikalk.antsmasher.networking.ApiContract.LEADERS;
import static com.tikalk.antsmasher.networking.ApiContract.LOGIN_ENDPOINT;
import static com.tikalk.antsmasher.networking.ApiContract.TEAM_PARAM;
import static com.tikalk.antsmasher.networking.ApiContract.UPDATE_ENDPOINT;
import static com.tikalk.antsmasher.networking.ApiContract.USERID_PARAM;

public interface GameRestService {

    @Headers("Content-Type: application/json")
    @POST(LOGIN_ENDPOINT)
    Observable<User> createUser(@Body CreateBody createBody);

    @PUT(UPDATE_ENDPOINT)
    Observable<String> updateUser(@Query(ID_PARAM) String name);

    @PUT(CREATE_PLAYER)
    Observable<Player> joinGame(@Query(TEAM_PARAM) long teamId, @Query(USERID_PARAM) String userId);

    @GET(GAMES)
    Observable<LatestGame> getLatestGame();

    @GET(TEAM_PARAM)
    Observable<List<Team>> getCurrentTeams();

    @GET(LEADERS)
    Observable<Player> getLeaderPlayer(); //Join the game

    @GET(LATEST_TEAMS)
    Observable<LatestTeam> getLatestTeams(); //Join the game

}
