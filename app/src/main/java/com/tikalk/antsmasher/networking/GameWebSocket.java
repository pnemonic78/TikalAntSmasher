package com.tikalk.antsmasher.networking;

import com.google.gson.Gson;

import android.content.Context;

import com.tikalk.antsmasher.model.socket.AntLocationMessage;
import com.tikalk.antsmasher.service.AppService;

import okhttp3.Response;
import okhttp3.WebSocket;

public class GameWebSocket extends AppWebSocket {

    private AppService.AppServiceEventListener messageListener;
    private Gson gson;

    public GameWebSocket(String baseUrl, String deviceId, Context context) {
        super(baseUrl, deviceId, context);
        gson = new Gson();
    }

    public void setMessageListener(AppService.AppServiceEventListener messageListener) {
        this.messageListener = messageListener;
    }

    @Override
    protected void handleSocketOpen(WebSocket webSocket, Response response) {
    }

    @Override
    protected void handleNewMessage(WebSocket socket, String message) {
        AntLocationMessage locationMessage = gson.fromJson(message, AntLocationMessage.class);

        if (messageListener != null) {
            messageListener.onAntMoved(locationMessage.getAntLocation());
        }
    }

    @Override
    protected void handleSocketFailure(WebSocket webSocket, Throwable t, Response response) {
    }

    @Override
    protected void handleSocketClose(WebSocket webSocket, int code, String reason) {
    }
}
