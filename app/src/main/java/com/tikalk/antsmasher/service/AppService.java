package com.tikalk.antsmasher.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.util.Log;

import javax.inject.Inject;
import javax.inject.Named;

import com.tikalk.antsmasher.AntApplication;
import com.tikalk.antsmasher.data.PrefsHelper;
import com.tikalk.antsmasher.model.GameState;
import com.tikalk.antsmasher.model.socket.AntLocation;
import com.tikalk.antsmasher.model.socket.AntSmash;
import com.tikalk.antsmasher.model.socket.HitSocketMessage;
import com.tikalk.antsmasher.model.socket.SocketMessage;
import com.tikalk.antsmasher.networking.ApiContract;
import com.tikalk.antsmasher.networking.websockets.AppWebSocket;
import com.tikalk.antsmasher.networking.websockets.GameWebSocket;
import com.tikalk.antsmasher.networking.websockets.NetworkManager;
import com.tikalk.antsmasher.networking.websockets.SmashWebSocket;


public class AppService extends Service {

    private static final String TAG = "TAG_AppService";

    public interface AppServiceEventListener {

        /**
         * Notification from the server that the game has started.
         */
        void onGameStarted();

        /**
         * Notification from the server that the game has finished.
         */
        void onGameFinished();

        void onGameStateMessage(GameState state);

        /**
         * Notification from the server that the ant has been moved.
         *
         * @param locationEvent the location event.
         */
        void onAntMoved(AntLocation locationEvent);

        /**
         * Notification from the server that the ant has been smashed.
         *
         * @param smashEvent the smash event.
         */
        void onAntSmashed(AntSmash smashEvent);
    }

    public interface AppServiceProxy {
        void registerServiceEventListener(AppServiceEventListener serviceEventListener);

        void smashAnt(AntSmash event);
    }

    private NetworkManager networkManager;
    private AppServiceEventListener serviceEventListener;
    private AppWebSocket gameWebSocket;
    private AppWebSocket smashWebSocket;
    private final LocalBinder binder = new LocalBinder();
    private String userName;

    @Inject
    PrefsHelper prefsHelper;

    @Inject
    @Named("SocketMessageGson")
    Gson socketMessageGson;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "onCreate");
        ((AntApplication) getApplication()).getApplicationComponent().inject(this);

        networkManager = new NetworkManager();
        userName = prefsHelper.getUserName();
    }

    @Override
    public LocalBinder onBind(Intent intent) {
        Log.v(TAG, "onBind");
        return binder;
    }

    public final class LocalBinder extends Binder implements AppServiceProxy {
        public AppServiceProxy getService() {
            // Return this instance so that clients can call public methods.
            return this;
        }

        @Override
        public void registerServiceEventListener(AppServiceEventListener serviceEventListener) {
            AppService.this.registerServiceEventListener(serviceEventListener);
            startWebSockets();
        }

        @Override
        public void smashAnt(AntSmash event) {
            AppService.this.smashAnt(event);
        }
    }

    private void registerServiceEventListener(AppServiceEventListener serviceEventListener) {
        this.serviceEventListener = serviceEventListener;
        if (gameWebSocket != null) {
            gameWebSocket.setMessageListener(serviceEventListener);
        }
    }

    private void smashAnt(AntSmash smash) {
        Log.i(TAG, "smashAnt: " + smash);

        if (smashWebSocket != null) {
            HitSocketMessage socketMessage = new HitSocketMessage(SocketMessage.TYPE_SEND, ApiContract.HIT_TRIAL_MESSAGE, smash);
            String messageJson = socketMessageGson.toJson(socketMessage);  //Convert the message to json string
            JsonParser jsonParser = new JsonParser();
            JsonObject jo = (JsonObject) jsonParser.parse(messageJson); //Creating JSON object from the message
            JsonObject body = jo.get("body").getAsJsonObject();  //Extract the body object from the outer object
            String buffer = body.toString(); //put it into a buffer
            jo.addProperty("body", buffer); //adding a "body" property, with the body json string as value (it will replace the existing body value with the new json string of the body)

//        SocketMessage socketMessage = new SocketMessage(SocketMessage.TYPE_REGISTER, "smash-message");
            smashWebSocket.sendMessage(jo.toString());
        }
    }

    private void startWebSockets() {
        String sessionId = prefsHelper.getGameId() + "_" + prefsHelper.getPlayerId();
        Log.i(TAG, "Real web socket");
        gameWebSocket = new GameWebSocket(ApiContract.ANT_PUBLISHER_URL, sessionId, this);
        smashWebSocket = new SmashWebSocket(ApiContract.SMASH_SERVICE_URL, sessionId, this, prefsHelper.getPlayerId());
        gameWebSocket.setMessageListener(serviceEventListener);
        smashWebSocket.setMessageListener(serviceEventListener);

        gameWebSocket.startSocket();
        smashWebSocket.startSocket();

        networkManager.add(gameWebSocket);
        networkManager.add(smashWebSocket);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
        networkManager.clear();
    }

}
