package com.tikalk.antsmasher.networking.rest;

import java.util.List;

import com.tikalk.antsmasher.model.AntSpecies;
import com.tikalk.antsmasher.model.Player;
import com.tikalk.antsmasher.model.Team;
import com.tikalk.antsmasher.model.User;
import com.tikalk.antsmasher.model.socket.PlayingTeam;
import com.tikalk.antsmasher.networking.requests.CreateUserRequest;
import com.tikalk.antsmasher.networking.response.GameResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

import static com.tikalk.antsmasher.networking.ApiContract.ANT_SPECIES;
import static com.tikalk.antsmasher.networking.ApiContract.CREATE_GAME;
import static com.tikalk.antsmasher.networking.ApiContract.CREATE_PLAYER;
import static com.tikalk.antsmasher.networking.ApiContract.GAMES_LATEST;
import static com.tikalk.antsmasher.networking.ApiContract.GAME_TIME;
import static com.tikalk.antsmasher.networking.ApiContract.ID_PARAM;
import static com.tikalk.antsmasher.networking.ApiContract.LATEST_TEAMS;
import static com.tikalk.antsmasher.networking.ApiContract.LEADERS;
import static com.tikalk.antsmasher.networking.ApiContract.LOGIN_ENDPOINT;
import static com.tikalk.antsmasher.networking.ApiContract.POPULATION;
import static com.tikalk.antsmasher.networking.ApiContract.START_GAME;
import static com.tikalk.antsmasher.networking.ApiContract.STEP_PER_SECOND;
import static com.tikalk.antsmasher.networking.ApiContract.TEAMS_CURRENT;
import static com.tikalk.antsmasher.networking.ApiContract.TEAM_PARAM;
import static com.tikalk.antsmasher.networking.ApiContract.UPDATE_ENDPOINT;
import static com.tikalk.antsmasher.networking.ApiContract.USERID_PARAM;

public interface GameRestService {

    @POST(LOGIN_ENDPOINT)
    Observable<User> createUser(@Body CreateUserRequest createBody);

    @PUT(UPDATE_ENDPOINT)
    Observable<User> updateUser(@Query(ID_PARAM) String id);

    @PUT(CREATE_PLAYER)
    Observable<Player> createPlayer(@Query(TEAM_PARAM) long teamId, @Query(USERID_PARAM) long userId);

    @GET(GAMES_LATEST)
    Observable<GameResponse> getLatestGame();

    @Headers("Content-Type: application/json")
    @POST(CREATE_GAME)
    Observable<GameResponse> createGame(@Query(GAME_TIME) int time, @Body() List<PlayingTeam> body);

    @PUT(START_GAME)
    Observable<GameResponse> startGame(@Query(POPULATION) int population, @Query(STEP_PER_SECOND) int stepsPerSecond);

    @GET(TEAMS_CURRENT)
    Observable<List<Team>> getCurrentTeams();

    @GET(LEADERS)
    Observable<List<Player>> getLeaderPlayer();

    @GET(LATEST_TEAMS)
    Observable<List<Team>> getLatestTeams();

    @GET(ANT_SPECIES)
    Observable<List<AntSpecies>> getAntSpecies();

}
