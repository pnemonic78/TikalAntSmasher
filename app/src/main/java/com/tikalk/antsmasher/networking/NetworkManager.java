package com.tikalk.antsmasher.networking;

import android.util.Log;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NetworkManager {

    private static final String TAG = "NetworkManager";

    private final Set<AppWebSocket> webSockets;
    private Observable<AppWebSocket> socketsObservable;

    public NetworkManager() {
        webSockets = new ConcurrentSkipListSet<>();
        socketsObservable = Observable.fromIterable(webSockets)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public boolean add(AppWebSocket webSocket) {
        return webSockets.add(webSocket);
    }

    public boolean remove(AppWebSocket webSocket) {
        webSocket.closeConnection();
        return webSockets.remove(webSocket);
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
                Log.v(TAG, "clear socket: " + webSocket.getSocketBaseUrl());
                remove(webSocket);
            }

            @Override
            public void onError(Throwable throwable) {
                Log.e(TAG, "onError: " + throwable.getLocalizedMessage(), throwable);
            }

            @Override
            public void onComplete() {
                Log.v(TAG, "onComplete");
                webSockets.clear();
            }
        });

    }

}
