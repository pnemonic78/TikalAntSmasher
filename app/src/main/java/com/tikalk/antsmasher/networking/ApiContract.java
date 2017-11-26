package com.tikalk.antsmasher.networking;

/**
 * Created by motibartov on 16/11/2017.
 */

public class ApiContract {

    //URL's
    public static final String SERVICE_BASE_URL = "http://localhost:8080";


    //Requests Endpoints
    public static final String LOGIN_ENDPOINT = "/users";
    public static final String UPDATE_ENDPOINT = "/users/:id";
    public static final String CREATE_PLAYER = "/players";
    public static final String GAMES = "/games/latest";
    public static final String TEAMS = "/teams/current";
    public static final String LEADERS = "/players/leaders";
    public static final String LATEST_TEAMS = "/teams/latest";



    //Query Params
    public static final String UPDATE_PARAM = "name";
    public static final String TEAM_PARAM = "teamId";
    public static final String USERID_PARAM = "userId";



}
