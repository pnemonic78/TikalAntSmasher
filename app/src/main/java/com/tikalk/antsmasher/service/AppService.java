package com.tikalk.antsmasher.service;

import com.google.gson.Gson;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.util.Log;

import javax.inject.Inject;
import javax.inject.Named;

import com.tikalk.antsmasher.AntApplication;
import com.tikalk.antsmasher.data.PrefsHelper;
import com.tikalk.antsmasher.model.DeveloperTeam;
import com.tikalk.antsmasher.model.GameState;
import com.tikalk.antsmasher.model.socket.AntLocation;
import com.tikalk.antsmasher.model.socket.AntSmash;
import com.tikalk.antsmasher.model.socket.AntHitMessage;
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
    private DeveloperTeam developerTeam;

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
        developerTeam = DeveloperTeam.find(prefsHelper.getDeveloperTeam());
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
        AntHitMessage antSocketMessage = new AntHitMessage(smash);
        if (gameWebSocket != null) {
            Log.i(TAG, "smashAnt: about to send antSmash");
            smashWebSocket.sendMessage(socketMessageGson.toJson(antSocketMessage));
        }
    }

    private void startWebSockets() {

        String sessionId = prefsHelper.getGameId() + "_" + prefsHelper.getPlayerId();
        Log.i(TAG, "Real web socket");
        gameWebSocket = new GameWebSocket(ApiContract.ANT_PUBLISHER_URL, sessionId, this);
        smashWebSocket = new SmashWebSocket(ApiContract.SMASH_SERVICE_URL, sessionId, this, prefsHelper.getPlayerId());
        gameWebSocket.setMessageListener(serviceEventListener);
        smashWebSocket.setMessageListener(serviceEventListener);

        networkManager.add(gameWebSocket);
        networkManager.add(smashWebSocket);
        gameWebSocket.openConnection();
        //  smashWebSocket.openConnection();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
        networkManager.clear();
    }

}
