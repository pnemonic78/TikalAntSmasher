package com.tikalk.antsmasher.networking;

import android.content.Context;

import com.tikalk.antsmasher.service.AppService;

import okhttp3.Response;
import okhttp3.WebSocket;

public class MockWebSocket extends AppWebSocket {

    private static final String TAG = "MockWebSocket";

    public MockWebSocket(String baseUrl, String deviceId, Context context) {
        super(baseUrl, deviceId, context);
    }

    @Override
    public void setMessageListener(AppService.AppServiceEventListener eventListener) {
        super.setMessageListener(eventListener);
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
