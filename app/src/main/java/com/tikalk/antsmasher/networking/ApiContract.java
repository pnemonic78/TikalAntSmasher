package com.tikalk.antsmasher.networking;

/**
 * Created by motibartov on 16/11/2017.
 */

public class ApiContract {

    //URL's
    public static final String SERVICE_BASE_URL = "http://10.100.10.62:8080";

    public static final int ADMIN_REST_PORT = 8080;
    public static final int ANT_PUBLISH_PORT = 6080;
    public static final int SMASH_PORT = 5080;

    //Requests Endpoints
    public static final String LOGIN_ENDPOINT = "/users";
    public static final String UPDATE_ENDPOINT = "/users";
    public static final String CREATE_PLAYER = "/players";
    public static final String GAMES_LATEST = "/games/latest";
    public static final String TEAMS_CURRENT = "/teams/current";
    public static final String LEADERS = "/players/leaders";
    public static final String LATEST_TEAMS = "/teams/latest";
    public static final String ANT_SPECIES = "/antspecies";

    //Query Params
    public static final String ID_PARAM = "id";
    public static final String UPDATE_PARAM = "name";
    public static final String TEAM_PARAM = "teamId";
    public static final String USERID_PARAM = "userId";


    //Websockets endpoints
    public static final String LR_MESSAGE = "lr-message";
    public static final String GAME_STATE_MESSAGE = "game-state-message";
    public static final String SMASH_MESSAGE = "smash-message/";
    public static final String SELF_SMASH_MESSAGE = "self_smash-message/";
    public static final String PLAY_SCORE_MESSAGE = "playerScore-message/";
    public static final String TEAM_SCORE_MESSAGE = "teamScore-message/";


}
