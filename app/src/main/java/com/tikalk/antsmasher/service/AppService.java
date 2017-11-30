package com.tikalk.antsmasher.service;

import com.google.gson.Gson;

import android.app.Service;
import android.content.Context;
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
import com.tikalk.antsmasher.model.socket.PlayerScore;
import com.tikalk.antsmasher.model.socket.SocketMessage;
import com.tikalk.antsmasher.model.socket.TeamScore;
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

        /**
         * Notification from the server that the player score has changed.
         *
         * @param scoreEvent the score event.
         */
        void onPlayerScore(PlayerScore scoreEvent);

        /**
         * Notification from the server that the team score has changed.
         *
         * @param scoreEvent the score event.
         */
        void onTeamScore(TeamScore scoreEvent);
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
    protected PrefsHelper prefsHelper;

    @Inject
    @Named("SocketMessageGson")
    protected Gson socketMessageGson;

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
            String body = socketMessageGson.toJson(smash);
            SocketMessage socketMessage = new SocketMessage(SocketMessage.TYPE_SEND, ApiContract.HIT_TRIAL_MESSAGE, body);
            String message = socketMessageGson.toJson(socketMessage);
            smashWebSocket.sendMessage(message);
        }
    }

    private void startWebSockets() {
        String sessionId = prefsHelper.getGameId() + "_" + prefsHelper.getPlayerId();
        Log.i(TAG, "Start real web sockets");
        final Context context = this;

        gameWebSocket = new GameWebSocket(ApiContract.ANT_PUBLISHER_URL, sessionId, context);
        gameWebSocket.setMessageListener(serviceEventListener);
        gameWebSocket.startSocket();

        smashWebSocket = new SmashWebSocket(ApiContract.SMASH_SERVICE_URL, sessionId, context);
        smashWebSocket.setMessageListener(serviceEventListener);
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
