package com.tikalk.antsmasher.networking.websockets;

import android.content.Context;
import android.util.Log;

import com.tikalk.antsmasher.model.socket.AntSmash;
import com.tikalk.antsmasher.model.socket.PlayerScore;
import com.tikalk.antsmasher.model.socket.SocketMessage;
import com.tikalk.antsmasher.model.socket.TeamScore;
import com.tikalk.antsmasher.service.AppService;

import okhttp3.Response;
import okhttp3.WebSocket;

import static com.tikalk.antsmasher.networking.ApiContract.PLAYER_SCORE_MESSAGE;
import static com.tikalk.antsmasher.networking.ApiContract.SELF_SMASH_MESSAGE;
import static com.tikalk.antsmasher.networking.ApiContract.SMASH_MESSAGE;
import static com.tikalk.antsmasher.networking.ApiContract.TEAM_SCORE_MESSAGE;

public class SmashWebSocket extends AppWebSocket {
    private static final String TAG = "TAG_SmashWebSocket";

    public SmashWebSocket(String baseUrl, String sessionId, Context context) {
        super(baseUrl, sessionId, context);
    }

    public void setMessageListener(AppService.AppServiceEventListener eventListener) {
        super.setMessageListener(eventListener);
    }

    @Override
    protected void handleSocketOpen(WebSocket webSocket, Response response) {
        SocketMessage smash_register = new SocketMessage(SocketMessage.TYPE_REGISTER, SMASH_MESSAGE);
        Log.i(TAG, "handleSocketOpen: registering to SMASH_MESSAGE: " + socketMessageGson.toJson(smash_register));
        sendMessage(socketMessageGson.toJson(smash_register));

        SocketMessage self_smash_register = new SocketMessage(SocketMessage.TYPE_REGISTER, SELF_SMASH_MESSAGE);
        Log.i(TAG, "handleSocketOpen: registering to SELF_SMASH_MESSAGE: " + socketMessageGson.toJson(self_smash_register));
        sendMessage(socketMessageGson.toJson(self_smash_register));

        SocketMessage play_score_register = new SocketMessage(SocketMessage.TYPE_REGISTER, PLAYER_SCORE_MESSAGE);
        Log.i(TAG, "handleSocketOpen: registering to PLAYER_SCORE_MESSAGE: " + socketMessageGson.toJson(play_score_register));
        sendMessage(socketMessageGson.toJson(play_score_register));

        SocketMessage team_score_register = new SocketMessage(SocketMessage.TYPE_REGISTER, TEAM_SCORE_MESSAGE);
        Log.i(TAG, "handleSocketOpen: registering to TEAM_SCORE_MESSAGE: " + socketMessageGson.toJson(team_score_register));
        sendMessage(socketMessageGson.toJson(team_score_register));
    }

    @Override
    protected void handleNewMessage(WebSocket socket, String message) {
        SocketMessage socketMessage = socketMessageGson.fromJson(message, SocketMessage.class);

        switch (socketMessage.address) {
            case SMASH_MESSAGE:
                AntSmash smash = socketMessageGson.fromJson(socketMessage.body, AntSmash.class);
                //Log.i(TAG, "handleNewMessage: smashed: " + smash);
                socketMessageListener.onAntSmashed(smash);
                break;
            case SELF_SMASH_MESSAGE:
                AntSmash smashSelf = socketMessageGson.fromJson(socketMessage.body, AntSmash.class);
                //Log.i(TAG, "handleNewMessage: self-smashed: " + smashSelf);
                socketMessageListener.onAntSmashed(smashSelf);
                break;
            case PLAYER_SCORE_MESSAGE:
                PlayerScore playerScore = socketMessageGson.fromJson(socketMessage.body, PlayerScore.class);
                //Log.i(TAG, "handleNewMessage: player score: " + playerScore);
                socketMessageListener.onPlayerScore(playerScore);
                break;
            case TEAM_SCORE_MESSAGE:
                TeamScore teamScore = socketMessageGson.fromJson(socketMessage.body, TeamScore.class);
                //Log.i(TAG, "handleNewMessage: team score: " + teamScore);
                socketMessageListener.onTeamScore(teamScore);
                break;
        }
    }

    @Override
    protected void handleSocketFailure(WebSocket webSocket, Throwable t, Response response) {
    }

    @Override
    protected void handleSocketClose(WebSocket webSocket, int code, String reason) {
    }
}
