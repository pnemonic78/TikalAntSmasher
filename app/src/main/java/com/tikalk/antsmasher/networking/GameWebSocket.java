package com.tikalk.antsmasher.networking;

import android.content.Context;

import com.google.gson.Gson;

import com.tikalk.antsmasher.model.socket.AntSocketMessage;
import com.tikalk.antsmasher.service.AppService;

import okhttp3.Response;
import okhttp3.WebSocket;


public class GameWebSocket extends AppWebSocket {

    private AppService.AppServiceEventListener messageListener;
    private Gson antLocationGson;

    public GameWebSocket(String baseUrl, String deviceId, Context context) {
        super(baseUrl, deviceId, context);
        antLocationGson = new Gson();
    }


    public void setMessageListener(AppService.AppServiceEventListener messageListener) {
        this.messageListener = messageListener;
    }

    @Override
    protected void handleSocketOpen(WebSocket webSocket, Response response) {

    }

    @Override
    protected void handleNewMessage(WebSocket socket, String message) {
        AntSocketMessage socketMessage = antLocationGson.fromJson(message, AntSocketMessage.class);

        if (messageListener != null) {
            messageListener.onAntMoved(socketMessage.getAntLocation());
        }
    }

    @Override
    protected void handleSocketFailure(WebSocket webSocket, Throwable t, Response response) {

    }

    @Override
    protected void handleSocketClose(WebSocket webSocket, int code, String reason) {

    }
}
