package com.tikalk.antsmasher.networking.websockets;

import android.content.Context;
import android.util.Log;

import com.tikalk.antsmasher.model.socket.AntSmash;
import com.tikalk.antsmasher.model.socket.HitSocketMessage;
import com.tikalk.antsmasher.model.socket.SocketMessage;
import com.tikalk.antsmasher.networking.ApiContract;
import com.tikalk.antsmasher.service.AppService;

import okhttp3.Response;
import okhttp3.WebSocket;

public class SmashWebSocket extends AppWebSocket {
    private static final String TAG = "TAG_SmashWebSocket";
    long playerId;

    public SmashWebSocket(String baseUrl, String sessionId, Context context, long playerId) {
        super(baseUrl, sessionId, context);
        this.playerId = playerId;
    }

    public void setMessageListener(AppService.AppServiceEventListener eventListener) {
        super.setMessageListener(eventListener);
    }

    @Override
    protected void handleSocketOpen(WebSocket webSocket, Response response) {
        SocketMessage smash_register = new SocketMessage(SocketMessage.TYPE_REGISTER, ApiContract.SMASH_ADDRESS);
        Log.i(TAG, "handleSocketOpen: registering to SMASH_MESSAGE: " + socketMessageGson.toJson(smash_register));
        sendMessage(socketMessageGson.toJson(smash_register));

        SocketMessage self_smash_register = new SocketMessage(SocketMessage.TYPE_REGISTER, ApiContract.SELF_SMASH_ADDRESS);
        Log.i(TAG, "handleSocketOpen: registering to SELF_SMASH_MESSAGE: " + socketMessageGson.toJson(self_smash_register));
        sendMessage(socketMessageGson.toJson(self_smash_register));

//        SocketMessage play_score_register = new SocketMessage(SocketMessage.TYPE_REGISTER, ApiContract.PLAY_SCORE_MESSAGE);
//        Log.i(TAG, "handleSocketOpen: registering to PLAY_SCORE_MESSAGE: " + socketMessageGson.toJson(play_score_register));
//        sendMessage(socketMessageGson.toJson(play_score_register));

//        SocketMessage team_score_register = new SocketMessage(SocketMessage.TYPE_REGISTER, ApiContract.TEAM_SCORE_MESSAGE);
//        Log.i(TAG, "handleSocketOpen: registering to TEAM_SCORE_MESSAGE: " + socketMessageGson.toJson(team_score_register));
//        sendMessage(socketMessageGson.toJson(team_score_register));
    }

    @Override
    protected void handleNewMessage(WebSocket socket, String message) {
        SocketMessage socketMessage = socketMessageGson.fromJson(message, SocketMessage.class);

        if (socketMessage.address.equals(ApiContract.SMASH_ADDRESS)) {
            HitSocketMessage smashMessage = socketMessageGson.fromJson(message, HitSocketMessage.class);
//            AntSmash smash = socketMessageGson.fromJson(smashMessage, AntSmash.class);
            Log.i(TAG, "handleNewMessage: got smash + " + smashMessage.antSmash);
            socketMessageListener.onAntSmashed(smashMessage.antSmash);
        }
    }

    @Override
    protected void handleSocketFailure(WebSocket webSocket, Throwable t, Response response) {
    }

    @Override
    protected void handleSocketClose(WebSocket webSocket, int code, String reason) {
    }
}