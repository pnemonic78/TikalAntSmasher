package com.tikalk.antsmasher.service;

import com.google.gson.Gson;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import javax.inject.Inject;
import javax.inject.Named;

import com.tikalk.antsmasher.AntApplication;
import com.tikalk.antsmasher.BuildConfig;
import com.tikalk.antsmasher.data.PrefsConstants;
import com.tikalk.antsmasher.data.PrefsHelper;
import com.tikalk.antsmasher.model.socket.AntLocation;
import com.tikalk.antsmasher.model.socket.AntSmash;
import com.tikalk.antsmasher.model.socket.AntSmashMessage;
import com.tikalk.antsmasher.networking.ApiContract;
import com.tikalk.antsmasher.networking.AppWebSocket;
import com.tikalk.antsmasher.networking.GameWebSocket;
import com.tikalk.antsmasher.networking.MockWebSocket;
import com.tikalk.antsmasher.networking.NetworkManager;


public class AppService extends Service {

    private static final String TAG = "AppService";

    NetworkManager networkManager;
    AppServiceEventListener serviceEventListener;
    AppWebSocket gameWebSocket;
    Binder mBinder = new LocalBinder();
    String userName;

    @Inject
    PrefsHelper mPrefsHelper;

    @Inject
    @Named("SocketMessageGson")
    Gson socketMessageGson;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
        networkManager = new NetworkManager();

        ((AntApplication) getApplication()).getApplicationComponent().inject(this);
        userName = mPrefsHelper.getString(PrefsConstants.USER_NAME);

        startWebSockets();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind");
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public AppService getService() {
            // Return this instance of LocalService so clients can call public methods
            return AppService.this;
        }
    }

    public void registerServiceEventListener(AppServiceEventListener serviceEventListener) {
        this.serviceEventListener = serviceEventListener;
        serviceEventListener.onGameStarted();
        if (gameWebSocket != null) {
            gameWebSocket.setMessageListener(serviceEventListener);
        }
    }


    public void smashAnt(AntSmash smash) {
        Log.i(TAG, "smashAnt:");
        AntSmashMessage antSocketMessage = new AntSmashMessage(smash);
        if (gameWebSocket != null) {
            gameWebSocket.sendMessage(socketMessageGson.toJson(antSocketMessage));
        }
        serviceEventListener.onAntSmashed(smash);

    }

    public void startWebSockets() {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "Debug, creating mock web socket");
            gameWebSocket = new MockWebSocket(ApiContract.DEVICES_REST_URL, userName, this);
        } else {
            Log.i(TAG, "Debug, real web socket");
            gameWebSocket = new GameWebSocket(ApiContract.DEVICES_REST_URL, userName, this);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        networkManager.clear();
    }

    public interface AppServiceEventListener {
        void onAntMoved(AntLocation antLocation);

        void onAntSmashed(AntSmash smashed);
        void onGameStarted();
        void onGameOver();
    }

}
