package com.tikalk.antsmasher.networking;

import android.content.Context;

import okhttp3.Response;
import okhttp3.WebSocket;


public class GameWebSocket extends AppWebSocket{


    public GameWebSocket(String baseUrl, String deviceId, Context context) {
        super(baseUrl, deviceId, context);
    }

    @Override
    protected void handleSocketOpen(WebSocket webSocket, Response response) {

    }

    @Override
    protected void handleNewMessage(WebSocket socket, String message) {

    }

    @Override
    protected void handleSocketFailure(WebSocket webSocket, Throwable t, Response response) {

    }

    @Override
    protected void handleSocketClose(WebSocket webSocket, int code, String reason) {

    }
}
