package com.tikalk.antsmasher.networking.websockets;

import android.content.Context;
import android.util.Log;

import com.tikalk.antsmasher.model.socket.AntLocation;
import com.tikalk.antsmasher.model.socket.GameStateBody;
import com.tikalk.antsmasher.model.socket.SocketMessage;
import com.tikalk.antsmasher.networking.ApiContract;

import okhttp3.Response;
import okhttp3.WebSocket;

public class GameWebSocket extends AppWebSocket {
    private static final String TAG = "TAG_GameWebSocket";

    public GameWebSocket(String baseUrl, String deviceId, Context context) {
        super(baseUrl, deviceId, context);
    }

    @Override
    protected void handleSocketOpen(WebSocket webSocket, Response response) {
        SocketMessage lr_register = new SocketMessage(SocketMessage.TYPE_REGISTER, ApiContract.LR_MESSAGE);
        Log.i(TAG, "handleSocketOpen: registering to ant-publish: " + socketMessageGson.toJson(lr_register));
        sendMessage(socketMessageGson.toJson(lr_register));

        SocketMessage game_state_register = new SocketMessage(SocketMessage.TYPE_REGISTER, ApiContract.GAME_STATE_MESSAGE);
        Log.i(TAG, "handleSocketOpen: registering to game state: " + socketMessageGson.toJson(game_state_register));
        sendMessage(socketMessageGson.toJson(game_state_register));
    }

    @Override
    protected void handleNewMessage(WebSocket socket, String message) {
        SocketMessage socketMessage = plainGson.fromJson(message, SocketMessage.class);

        switch (socketMessage.address) {
            case ApiContract.LR_MESSAGE:
                AntLocation location = plainGson.fromJson(socketMessage.body, AntLocation.class);
                Log.i(TAG, "handleNewMessage: location message: " + location);
                socketMessageListener.onAntMoved(location);
                break;
            case ApiContract.GAME_STATE_MESSAGE:
                GameStateBody state = plainGson.fromJson(socketMessage.body, GameStateBody.class);
                Log.i(TAG, "handleNewMessage: game state message: " + state);
                socketMessageListener.onGameStateMessage(state.getState());
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
