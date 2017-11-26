package com.tikalk.antsmasher.networking;

import android.content.Context;
import android.util.Log;

import com.tikalk.antsmasher.model.socket.AntLocationMessage;
import com.tikalk.antsmasher.model.socket.GameStateMessage;
import com.tikalk.antsmasher.model.socket.SocketMessage;
import com.tikalk.antsmasher.service.AppService;

import okhttp3.Response;
import okhttp3.WebSocket;

public class GameWebSocket extends AppWebSocket {
    private static final String TAG = "TAG_GameWebSocket";

    public GameWebSocket(String baseUrl, String deviceId, Context context) {
        super(baseUrl, deviceId, context);
    }

    public void setMessageListener(AppService.AppServiceEventListener eventListener) {
        super.setMessageListener(eventListener);
    }

    @Override
    protected void handleSocketOpen(WebSocket webSocket, Response response) {
        SocketMessage lr_register = new SocketMessage(SocketMessage.TYPE_REGISTER, ApiContract.LR_MESSAGE);
        Log.i(TAG, "handleSocketOpen: registering to collision socket: " + socketMessageGson.toJson(lr_register));
        sendMessage(socketMessageGson.toJson(lr_register));

        SocketMessage game_state_register = new SocketMessage(SocketMessage.TYPE_REGISTER, ApiContract.GAME_STATE_MESSAGE);
        sendMessage(socketMessageGson.toJson(game_state_register));
    }

    @Override
    protected void handleNewMessage(WebSocket socket, String message) {
        SocketMessage socketMessage = socketMessageGson.fromJson(message, SocketMessage.class);

        if (socketMessage.address.equals(ApiContract.LR_MESSAGE)) {
            AntLocationMessage antLocation = socketMessageGson.fromJson(message, AntLocationMessage.class);
            socketMessageListener.onAntMoved(antLocation.body);
        } else if (socketMessage.address.equals(ApiContract.GAME_STATE_MESSAGE)) {
            GameStateMessage stateMessage = socketMessageGson.fromJson(message, GameStateMessage.class);
            socketMessageListener.onGameStateMessage(stateMessage.body);
        }
    }

    @Override
    protected void handleSocketFailure(WebSocket webSocket, Throwable t, Response response) {
    }

    @Override
    protected void handleSocketClose(WebSocket webSocket, int code, String reason) {
    }
}
