package com.tikalk.antsmasher.networking;

import android.util.Log;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NetworkManager {

    public static final String TAG = "TAG_" + NetworkManager.class.getSimpleName();
    Set<AppWebSocket> webSockets;
    io.reactivex.Observable<AppWebSocket> socketsObservable;

    public NetworkManager() {
        webSockets = new ConcurrentSkipListSet<>();
        socketsObservable = io.reactivex.Observable.fromIterable(webSockets);
        socketsObservable.subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread());
    }

    public boolean add(AppWebSocket webSocket) {
        return webSockets.add(webSocket);
    }

    public boolean remove(AppWebSocket webSocket) {
        webSocket.closeConnection();
        boolean status = webSockets.remove(webSocket);
        return status;
    }

    public void updateInternetConnection(boolean isInternetConnected) {

        socketsObservable.subscribe(new Observer<AppWebSocket>() {

            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(AppWebSocket webSocket) {
                webSocket.updateInternetConnection(isInternetConnected);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void clear() {

        socketsObservable.subscribe(new Observer<AppWebSocket>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(AppWebSocket webSocket) {
                Log.i(TAG, "clear socket: " + webSocket.socketBaseUrl);
                remove(webSocket);
                webSocket = null;
            }

            @Override
            public void onError(Throwable throwable) {
                Log.e(TAG, "onError: ", throwable);
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete: ");
                webSockets.clear();
            }
        });

    }

}
