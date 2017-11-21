package com.tikalk.antsmasher.networking;

import android.content.Context;
import android.os.SystemClock;

import java.util.Random;

import com.tikalk.antsmasher.board.BoardViewModel;
import com.tikalk.antsmasher.model.Ant;
import com.tikalk.antsmasher.model.AntSpecies;
import com.tikalk.antsmasher.model.Game;
import com.tikalk.antsmasher.model.socket.AntLocation;
import com.tikalk.antsmasher.service.AppService;

import okhttp3.Response;
import okhttp3.WebSocket;

public class MockWebSocket extends AppWebSocket {

    private static final String TAG = "MockWebSocket";

    private Thread thread;
    private static final Random random = new Random();

    public MockWebSocket(String baseUrl, String deviceId, Context context) {
        super(baseUrl, deviceId, context);
    }

    @Override
    public void setMessageListener(AppService.AppServiceEventListener eventListener) {
        super.setMessageListener(eventListener);
        start(eventListener);
    }

    @Override
    protected void handleSocketOpen(WebSocket webSocket, Response response) {
    }

    @Override
    protected void handleNewMessage(WebSocket socket, String message) {
    }

    @Override
    protected void handleSocketFailure(WebSocket webSocket, Throwable t, Response response) {
        stop();
    }

    @Override
    protected void handleSocketClose(WebSocket webSocket, int code, String reason) {
        stop();
    }

    /**
     * Start the game.
     */
    private void start(final AppService.AppServiceEventListener listener) {
        // fake ants from the server.
        thread = new Thread() {

            @Override
            public void run() {
                try {
                    // wait for View to start drawing.
                    sleep(500L);
                } catch (InterruptedException e) {
                }
                listener.onGameStarted();
                final Game game = BoardViewModel.createGame();
                if (game == null) {
                    return;
                }
                final int teamSize = game.getTeams().size();
                final int size = 20;
                final Ant[] ants = new Ant[size];
                Ant ant;
                AntSpecies species;
                float dy;
                float x;
                float y;
                double t = 0;
                final double T = Math.PI * 2 * 3;
                final double dt = T / 300;
                float[] antX = new float[size];
                float[] antY = new float[size];
                float[] antVelocityY = new float[size];
                int antId = 1;
                int teamIndex;
                long start = SystemClock.uptimeMillis();

                for (int i = 0; i < size; i++) {
                    antX[i] = random.nextFloat();
                    antY[i] = 0f;
                    antVelocityY[i] = 1f + random.nextFloat();

                    teamIndex = random.nextInt(teamSize);
                    species = game.getTeams().get(teamIndex).getAntSpecies();

                    ant = new Ant(String.valueOf(antId++));
                    ant.setLocation(antX[i], antY[i]);
                    species.add(ant);
                    ants[i] = ant;
                }

                do {
                    for (int i = 0; i < size; i++) {
                        ant = ants[i];
                        species = ant.getSpecies();
                        if (ant.isAlive()) {
                            dy = antVelocityY[i] * random.nextFloat() * 0.02f;
                            x = antX[i] + (float) (Math.sin(t) / 10);
                            y = ant.getLocation().y + dy;
                            listener.onAntMoved(new AntLocation(ant.getId(), species.getId(), x, y));
                            ant.setLocation(x, y);
                        }
                        if (!ant.isVisible()) {
                            ants[i] = null;
                            species.remove(ant);

                            ant = new Ant(String.valueOf(antId++));
                            ant.setLocation(antX[i], antY[i]);
                            species.add(ant);
                            ants[i] = ant;
                        }
                        try {
                            sleep(2);
                        } catch (InterruptedException e) {
                        }
                    }
                    t += dt;
                }
                while ((SystemClock.uptimeMillis() <= (start + 15000L)) && isAlive() && !isInterrupted());
                listener.onGameFinished();
            }
        };
        thread.start();
    }

    /**
     * Stop the game.
     */
    public void stop() {
        if (thread != null) {
            thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException e) {
            }
        }
    }
}
