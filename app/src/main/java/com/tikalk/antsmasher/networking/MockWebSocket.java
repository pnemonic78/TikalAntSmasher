package com.tikalk.antsmasher.networking;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.tikalk.antsmasher.model.Ant;
import com.tikalk.antsmasher.model.AntLocation;
import com.tikalk.antsmasher.service.AppService;

import java.util.Random;

import okhttp3.Response;
import okhttp3.WebSocket;


public class MockWebSocket extends AppWebSocket {

    private static final String TAG = "MockWebSocket";

    private static final long moveDuration = 2000;
    private static final long jumpDuration = 500;
    private ValueAnimator upDownAnim = ValueAnimator.ofFloat(0f, 1.0f);
    private ValueAnimator sideAnimation = ValueAnimator.ofFloat(-0.4f, 0.6f);
    private AnimatorSet animatorSet = new AnimatorSet();

    private float xVal = 0.0f;
    private float yVal = 0.0f;

    private Ant ant;

    public MockWebSocket(String baseUrl, String deviceId, Context context) {
        super(baseUrl, deviceId, context);

        yVal = 0.5f;
        ant = new Ant();
        ant.setId("1");
        ant.setAlive(true);

        upDownAnim.setDuration(moveDuration).setInterpolator(new LinearInterpolator());
        upDownAnim.addUpdateListener(valueAnimator -> {
            yVal = (float) valueAnimator.getAnimatedValue();
            ant.setLocation(xVal, yVal);

            Log.i(TAG, "MockWebSocket: ant location = " + ant.getLocation());
//            if(socketMessageListener != null){
//                socketMessageListener.onAntMoved(new AntLocation(ant
//                        .getId(), ant.getLocation().x, ant.getLocation().y));
//            }
        });
        upDownAnim.setRepeatMode(ValueAnimator.RESTART);

        sideAnimation.setDuration(jumpDuration).setInterpolator(new AccelerateInterpolator(1.5f));
        sideAnimation.addUpdateListener(valueAnimator -> xVal = (float) valueAnimator.getAnimatedValue());
        sideAnimation.setRepeatMode(ValueAnimator.REVERSE);
        sideAnimation.setRepeatCount(ValueAnimator.INFINITE);
    }

    @Override
    public void setMessageListener(AppService.AppServiceEventListener mServiceListener) {
        Log.i(TAG, "setMessageListener: starting animation...");
        socketMessageListener = mServiceListener;
        animatorSet.playTogether(upDownAnim, sideAnimation);
        animatorSet.start();
    }

    @Override
    protected void handleSocketOpen(WebSocket webSocket, Response response) {
    }

    @Override
    protected void handleNewMessage(WebSocket socket, String message) {
    }

    @Override
    protected void handleSocketFailure(WebSocket webSocket, Throwable t, Response response) {
    }

    @Override
    protected void handleSocketClose(WebSocket webSocket, int code, String reason) {
    }

}
