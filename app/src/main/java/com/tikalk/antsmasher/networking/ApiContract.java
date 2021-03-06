package com.tikalk.antsmasher.networking;

import android.net.Uri;

import java.net.URISyntaxException;


public class ApiContract {

    //URL's
    public static final String AUTHORITY = "ants.fuze.tikal.io";

    private static final int ANT_PUBLISH_PORT = 6080;
    private static final int SMASH_SERVICE_PORT = 5080;
    private static final int ADMIN_REST_PORT = 8080;

    private static final String REGISTRY_URL_PATH = "client.register";

    //Requests Endpoints
    public static final String LOGIN_ENDPOINT = "/users";
    public static final String UPDATE_ENDPOINT = "/users";
    public static final String CREATE_PLAYER = "/players";
    public static final String GAMES_LATEST = "/games/latest";
    public static final String CREATE_GAME = "/games";
    public static final String START_GAME = "/games/start";
    public static final String TEAMS_CURRENT = "/teams/current";
    public static final String LEADERS = "/players/leaders";
    public static final String LATEST_TEAMS = "/teams/latest";
    public static final String ANT_SPECIES = "/antspecies";

    //Query Params
    public static final String ID_PARAM = "id";
    public static final String UPDATE_PARAM = "name";
    public static final String TEAM_PARAM = "teamId";
    public static final String USERID_PARAM = "userId";
    public static final String GAME_TIME = "gameTime";
    public static final String POPULATION = "population";
    public static final String STEP_PER_SECOND = "stepsPerSecond";

    //Websockets endpoints
    public static final String LR_MESSAGE = "lr-message";
    public static final String GAME_STATE_MESSAGE = "game-state-message";
    public static final String PLAYER_SCORE_MESSAGE = "playerScore-message";
    public static final String TEAM_SCORE_MESSAGE = "teamScore-message";
    public static final String HIT_TRIAL_MESSAGE = "hit-trial-message";
    public static final String SELF_SMASH_MESSAGE = "self-smash-message";
    public static final String SMASH_MESSAGE = "smash-message";

    public static String buildAntPublishSocketUrl(String baseUrl) {
        return new Uri.Builder()
                .scheme(null)
                .encodedAuthority(baseUrl + ":" + ANT_PUBLISH_PORT)
                .appendPath(REGISTRY_URL_PATH)
                .build().toString();
    }

    public static String buildAntSmashSocketUrl(String baseUrl) {
        return new Uri.Builder()
                .scheme(null)
                .encodedAuthority(baseUrl + ":" + SMASH_SERVICE_PORT)
                .appendPath(REGISTRY_URL_PATH)
                .build().toString();
    }

    public static String buildAdminBaseUrl(String baseUrl) throws URISyntaxException {
        return new Uri.Builder()
                .scheme("http")
                .encodedAuthority(baseUrl + ":" + ADMIN_REST_PORT)
                .build().toString();
    }

}
