package com.tikalk.antsmasher;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import junit.framework.TestSuite;

import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.tikalk.antsmasher.model.GameState;
import com.tikalk.antsmasher.model.socket.AntLocation;
import com.tikalk.antsmasher.model.socket.AntSmash;
import com.tikalk.antsmasher.model.socket.GameStateBody;
import com.tikalk.antsmasher.model.socket.SocketMessage;
import com.tikalk.antsmasher.networking.websockets.AppWebSocket;

import static com.tikalk.antsmasher.networking.ApiContract.GAME_STATE_MESSAGE;
import static com.tikalk.antsmasher.networking.ApiContract.LR_MESSAGE;
import static com.tikalk.antsmasher.networking.ApiContract.SMASH_MESSAGE;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * @author moshe on 2017/11/26.
 */
public class WebSocketTests extends TestSuite {

    @Test
    public void parseLocation1() throws Exception {
        InputStream in = getClass().getResourceAsStream("/ant_location_1.json");
        assertNotNull(in);
        Reader reader = new InputStreamReader(in);
        assertEquals(AppWebSocket.TYPE_ARRAY, reader.read());

        Gson gson = new Gson();
        String[] content = gson.fromJson(reader, String[].class);
        reader.close();
        assertNotNull(content);
        assertEquals(1, content.length);
        SocketMessage socketMessage = gson.fromJson(content[0], SocketMessage.class);
        assertNotNull(socketMessage);
        assertEquals(SocketMessage.TYPE_RECORD, socketMessage.type);
        assertEquals(LR_MESSAGE, socketMessage.address);
        JsonElement body = socketMessage.body;
        assertNotNull(body);
        AntLocation event = gson.fromJson(body.getAsString(), AntLocation.class);
        assertNotNull(event);
        assertEquals("941dfa7a-9d1b-499b-9ba2-1d9e42dd7b2e", event.antId);
        assertEquals(3, event.speciesId);
        assertEquals(0.107f, event.xRate);
        assertEquals(0.22f, event.yRate);
    }

    @Test
    public void parseSmash1() throws Exception {
        InputStream in = getClass().getResourceAsStream("/ant_smash_1.json");
        assertNotNull(in);
        Reader reader = new InputStreamReader(in);
        assertEquals(AppWebSocket.TYPE_ARRAY, reader.read());

        Gson gson = new Gson();
        String[] content = gson.fromJson(reader, String[].class);
        reader.close();
        assertNotNull(content);
        assertEquals(1, content.length);
        SocketMessage socketMessage = gson.fromJson(content[0], SocketMessage.class);
        assertNotNull(socketMessage);
        assertEquals(SocketMessage.TYPE_RECORD, socketMessage.type);
        assertEquals(SMASH_MESSAGE, socketMessage.address);
        JsonElement body = socketMessage.body;
        assertNotNull(body);
        AntSmash event = gson.fromJson(body.getAsString(), AntSmash.class);
        assertNotNull(event);
        assertEquals("11122", event.antId);
        //TODO assertEquals(9, event.playerId);
    }

    @Test
    public void parseGameStarted() throws Exception {
        InputStream in = getClass().getResourceAsStream("/game_state_started.json");
        assertNotNull(in);
        Reader reader = new InputStreamReader(in);
        assertEquals(AppWebSocket.TYPE_ARRAY, reader.read());

        Gson gson = new Gson();
        String[] content = gson.fromJson(reader, String[].class);
        reader.close();
        assertNotNull(content);
        assertEquals(1, content.length);
        SocketMessage socketMessage = gson.fromJson(content[0], SocketMessage.class);
        assertNotNull(socketMessage);
        assertEquals(SocketMessage.TYPE_RECORD, socketMessage.type);
        assertEquals(GAME_STATE_MESSAGE, socketMessage.address);
        JsonElement body = socketMessage.body;
        assertNotNull(body);
        GameStateBody event = gson.fromJson(body, GameStateBody.class);
        assertNotNull(event);
        assertEquals(GameState.STARTED, event.getState());
    }

    @Test
    public void parseGameStopped() throws Exception {
        InputStream in = getClass().getResourceAsStream("/game_state_stopped.json");
        assertNotNull(in);
        Reader reader = new InputStreamReader(in);
        assertEquals(AppWebSocket.TYPE_ARRAY, reader.read());

        Gson gson = new Gson();
        String[] content = gson.fromJson(reader, String[].class);
        reader.close();
        assertNotNull(content);
        assertEquals(1, content.length);
        SocketMessage socketMessage = gson.fromJson(content[0], SocketMessage.class);
        assertNotNull(socketMessage);
        assertEquals(SocketMessage.TYPE_RECORD, socketMessage.type);
        assertEquals(GAME_STATE_MESSAGE, socketMessage.address);
        JsonElement body = socketMessage.body;
        assertNotNull(body);
        GameStateBody event = gson.fromJson(body, GameStateBody.class);
        assertNotNull(event);
        assertEquals(GameState.STOPPED, event.getState());
    }
}
