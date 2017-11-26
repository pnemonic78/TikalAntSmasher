package com.tikalk.antsmasher.service;

import com.google.gson.Gson;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.util.Log;

import javax.inject.Inject;
import javax.inject.Named;

import com.tikalk.antsmasher.AntApplication;
import com.tikalk.antsmasher.BuildConfig;
import com.tikalk.antsmasher.data.PrefsHelper;
import com.tikalk.antsmasher.model.socket.AntLocation;
import com.tikalk.antsmasher.model.socket.AntSmash;
import com.tikalk.antsmasher.model.socket.AntSmashMessage;
import com.tikalk.antsmasher.networking.AppWebSocket;
import com.tikalk.antsmasher.networking.GameNetworkContract;
import com.tikalk.antsmasher.networking.GameWebSocket;
import com.tikalk.antsmasher.networking.MockWebSocket;
import com.tikalk.antsmasher.networking.NetworkManager;


public class AppService extends Service {

    private static final String TAG = "AppService";

    public interface AppServiceEventListener {

        /**
         * Notification from the server that the game has started.
         */
        void onGameStarted();

        /**
         * Notification from the server that the game has finished.
         */
        void onGameFinished();

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
        networkManager = new NetworkManager();

        ((AntApplication) getApplication()).getApplicationComponent().inject(this);
        userName = prefsHelper.getUserName();

        startWebSockets();
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
        AntSmashMessage antSocketMessage = new AntSmashMessage(smash);
        if (gameWebSocket != null) {
            gameWebSocket.sendMessage(socketMessageGson.toJson(antSocketMessage));
        }
    }

    private void startWebSockets() {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "Debug, creating mock web socket");
            gameWebSocket = new MockWebSocket(userName, this);
        } else {
            Log.i(TAG, "Real web socket");
            gameWebSocket = new GameWebSocket(GameNetworkContract.GAME_SERVER_BASE_URL, userName, this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        networkManager.clear();
    }

}
