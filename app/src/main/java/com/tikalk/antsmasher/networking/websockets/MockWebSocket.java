package com.tikalk.antsmasher.networking.websockets;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import java.util.Random;

import com.tikalk.antsmasher.model.Ant;
import com.tikalk.antsmasher.model.AntSpecies;
import com.tikalk.antsmasher.model.Game;
import com.tikalk.antsmasher.model.Team;
import com.tikalk.antsmasher.model.socket.AntLocation;
import com.tikalk.antsmasher.model.socket.SocketMessage;
import com.tikalk.antsmasher.service.AppService;

import okhttp3.Response;
import okhttp3.WebSocket;

public class MockWebSocket extends AppWebSocket {

    private static final String TAG = "MockWebSocket";

    private Thread server;
    private Game game;

    public MockWebSocket(String deviceId, Context context) {
        super("", deviceId, context);
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
    protected void handleNewMessage(WebSocket socket, SocketMessage message) {
//        if (HitSocketMessage.TYPE_SMASH.equals(message.type)) {
//            HitSocketMessage smashMessage = socketMessageGson.fromJson(message, HitSocketMessage.class);
//            AntSmash smash = socketMessageGson.toJson(message.body, AntSmash.class);
//            if ((game != null) && (smash.antId != null)) {
//                Ant ant = game.getAnt(smash.antId);
//                if (ant != null) {
//                    ant.setAlive(false);
//                    socketMessageListener.onAntSmashed(smash);
//                }
//            }
//        }
    }

    @Override
    protected void handleSocketFailure(WebSocket webSocket, Throwable t, Response response) {
        stop();
    }

    @Override
    protected void handleSocketClose(WebSocket webSocket, int code, String reason) {
        stop();
    }

    @Override
    public boolean sendMessage(String message) {
        Log.v(TAG, "sendMessage: " + message);
        //handleNewMessage(null, message);
        return true;
    }

    /**
     * Start the game.
     */
    private void start(final AppService.AppServiceEventListener listener) {
        // fake ants from the server.
        server = new Thread() {

            @Override
            public void run() {
                try {
                    // wait for View to start drawing.
                    sleep(500L);
                } catch (InterruptedException e) {
                }
                listener.onGameStarted();
                final Game game = createGame();
                if (game == null) {
                    return;
                }
                MockWebSocket.this.game = game;
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
                final Random random = new Random();

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
        server.start();
    }

    /**
     * Stop the game.
     */
    public void stop() {
        if (server != null) {
            server.interrupt();
            try {
                server.join();
            } catch (InterruptedException e) {
            }
        }
    }

    public static Game createGame() {
        Game game = new Game();
        populateGame(game);
        return game;
    }

    public static void populateGame(Game game) {
        game.setId(1);

        Team team = new Team(10, "Army");
        populateTeam(team);
        game.getTeams().add(team);

        team = new Team(20, "Fire");
        populateTeam(team);
        game.getTeams().add(team);

        team = new Team(30, "Black");
        populateTeam(team);
        game.getTeams().add(team);
    }

    public static void populateTeam(Team team) {
        AntSpecies species = team.getAntSpecies();
        switch ((int) team.getId()) {
            case 10:
                species.setId(1);
                break;
            case 20:
                species.setId(2);
                break;
            case 30:
                species.setId(3);
                break;
        }
    }
}
