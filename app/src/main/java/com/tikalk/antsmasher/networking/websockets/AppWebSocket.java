package com.tikalk.antsmasher.networking.websockets;

import com.google.gson.Gson;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import com.tikalk.antsmasher.AntApplication;
import com.tikalk.antsmasher.model.socket.SocketMessage;
import com.tikalk.antsmasher.service.AppService;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.logging.HttpLoggingInterceptor;

public abstract class AppWebSocket implements Comparable<AppWebSocket> {

    private static final String TAG = "TAG_AppWebSocket";

    public static final char TYPE_OPEN = 'o';
    public static final char TYPE_HEARTBEAT = 'h';
    public static final char TYPE_ARRAY = 'a';
    public static final char TYPE_MESSAGE = 'm';
    public static final char TYPE_CLOSE = 'c';

    private static final int NORMAL_CLOSURE_STATUS = 1000;
    private boolean internetConnected = true;

    private WebSocket mSocket;
    private Request mRequest;

    private OkHttpClient client;
    private OkHttpClient.Builder okHttpClientBuilder;
    private String deviceId;
    private boolean connectionClosed = true;

    @Inject
    @Named("PlainGson")
    protected Gson socketMessageGson;

    private boolean socketOpened = false;

    private WeakReference<Context> weakContext;
    private String socketBaseUrl;
    private Disposable pingDisposable;

    protected AppService.AppServiceEventListener socketMessageListener;

    protected final Handler mHandler = new Handler(Looper.getMainLooper());

    protected AppWebSocket(String baseUrl, String session_id, Context context) {
        Log.v(TAG, "AppWebSocket created. url: " + baseUrl);
        ((AntApplication) context.getApplicationContext()).getApplicationComponent().inject(this);
        Uri uri = new Uri.Builder()
                .scheme("http")
                .encodedAuthority(baseUrl)
                .appendPath("123")
                .appendPath(session_id)
                .appendPath("websocket")
                .build();

        weakContext = new WeakReference<>(context);
        socketBaseUrl = baseUrl;
        this.deviceId = deviceId;
        initSocket(uri.toString());
    }

    private void initSocket(String socketUrl) {
        okHttpClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClientBuilder.addInterceptor(logging);
        client = okHttpClientBuilder.build();

        mRequest = new Request.Builder().url(socketUrl).build();
        Log.v(TAG, "initSocket: " + mRequest);
    }

    public void startSocket() {
        connectionClosed = false;
        openConnection();
    }

    synchronized public void openConnection() {
        if (mSocket == null) {
            Log.v(TAG, "openConnection: ");
            client.newWebSocket(mRequest, mSocketListener);
        }
    }

    public void closeConnection() {
        Log.v(TAG, "about to closeConnection: " + socketBaseUrl);

        if (mSocket != null || socketOpened) {
            Log.v(TAG, "closing: " + socketBaseUrl);
            mSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye");
            socketOpened = false;
            mSocket = null;
        }

        if (pingDisposable != null) {
            pingDisposable.dispose();
            pingDisposable = null;
            Log.v(TAG, "closeConnection: ping stopped for " + socketBaseUrl);
        }
    }

    public void stopSocket() {
        connectionClosed = true;
        closeConnection();
    }

    private void ping(WebSocket webSocket) {
        pingDisposable = Observable.interval(5000, TimeUnit.MILLISECONDS, Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (l) -> {
//                            Log.v(TAG, "startTimer: onNext " + l);
//                            observableRoadUsersList.subscribe(roadUserObserver);
                            if (socketOpened && weakContext.get() != null) {
                                webSocket.send("[\"{\\\"type\\\":\\\"ping\\\"}\"]");
                            }
                            Log.v(TAG, "ping socket: " + socketBaseUrl);
                        },
                        (throwable) -> {
                            Log.e(TAG, "ping exception: ", throwable);
                        },
                        () -> {
                            //do on complete
                            Log.v(TAG, "ping completed");
                        },
                        onSubscribe -> {
                        });
    }


    public boolean sendMessage(String message) {
        if (socketOpened && internetConnected) {
            final String text = "[" + socketMessageGson.toJson(message) + "]";
            Log.v(TAG, "Sending Message: " + text);
            return mSocket.send(text);
        } else {
            Log.w(TAG, "sendMessage: there was an attempt to send message from " + socketBaseUrl + ", but the socket is: " + socketOpened + " and internet connection = " + internetConnected);
        }
        return false;
    }

    public void updateInternetConnection(boolean isConnected) {
        Log.v(TAG, "updateInternetConnection: ");
        if (isConnected && !internetConnected) {
            Log.i(TAG, "Internet connected");
            openConnection();
        } else if (!isConnected && internetConnected) {
            Log.i(TAG, "Internet disconnected");
            closeConnection();
        }

        internetConnected = isConnected;
    }

    public void showToast(String message) {
        if (weakContext.get() != null) {
            mHandler.post(() -> Toast.makeText(weakContext.get(), message, Toast.LENGTH_SHORT).show());
        }
    }

    protected abstract void handleSocketOpen(WebSocket webSocket, Response response);

    protected abstract void handleNewMessage(WebSocket socket, String message);

    protected abstract void handleSocketFailure(WebSocket webSocket, Throwable t, Response response);

    protected abstract void handleSocketClose(WebSocket webSocket, int code, String reason);

    public void setMessageListener(AppService.AppServiceEventListener eventListener) {
        this.socketMessageListener = eventListener;
    }

    @Override
    public int compareTo(@NonNull AppWebSocket webSocket) {
        return this.equals(webSocket) ? 0 : -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppWebSocket)) return false;

        AppWebSocket that = (AppWebSocket) o;
        return socketBaseUrl.equals(that.socketBaseUrl);
    }

    @Override
    public int hashCode() {
        return socketBaseUrl.hashCode();
    }

    public String getSocketBaseUrl() {
        return socketBaseUrl;
    }

    private final WebSocketListener mSocketListener = new WebSocketListener() {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            Log.v(TAG, "onOpen: socket opened: " + socketBaseUrl + webSocket.toString());
            mSocket = webSocket;
            socketOpened = true;
            ping(mSocket);
            handleSocketOpen(webSocket, response);
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            Log.v(TAG, "onMessage socket: + " + socketBaseUrl + ", open=" + socketOpened + " text=~" + text + "~");

            final char type = text.charAt(0);
            // first check for messages that don't need a payload
            switch (type) {
                case TYPE_OPEN:
                case TYPE_HEARTBEAT:
                case TYPE_CLOSE:
                    return;
            }

            //FIXME only use gson
            String strippedString = text.replaceAll("\\\\", "").replace("}\"", "}").replace("\"{", "{").substring(text.indexOf("[") + 1);
            strippedString = strippedString.substring(0, strippedString.lastIndexOf("]"));

            SocketMessage message = new Gson().fromJson(strippedString, SocketMessage.class);
//                Log.v(TAG, "checking message type:  " + message.type);

            if (message.type.equals(SocketMessage.TYPE_ERROR)) {
                Log.e(TAG, "message type error: " + socketBaseUrl + ": " + message);
            } else {
                handleNewMessage(webSocket, strippedString);
            }
//            webSocketEventListener.onNewMessage(strippedString);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            Log.v(TAG, "onClosing: " + reason);
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
            socketOpened = false;
            Log.v(TAG, "onClosed: " + socketBaseUrl);
            handleSocketClose(webSocket, code, reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            super.onFailure(webSocket, t, response);
            Log.e(TAG, "onFailure: ", t);
            handleSocketFailure(webSocket, t, response);
            if (!connectionClosed) {
                recoverConnection();
            }
        }
    };

    void recoverConnection() {
        closeConnection();

        if (internetConnected) {
            mHandler.postDelayed(() -> {
                Log.v(TAG, "onFailure: internet is connected, trying to reopen socket");
                openConnection();
            }, 5000);
        } else {
            Log.v(TAG, "onFailure, internet disconnected...");
        }
    }

}

