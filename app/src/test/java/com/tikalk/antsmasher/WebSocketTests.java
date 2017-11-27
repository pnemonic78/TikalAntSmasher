package com.tikalk.antsmasher;

import com.google.gson.Gson;

import junit.framework.TestSuite;

import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.tikalk.antsmasher.model.socket.AntLocation;
import com.tikalk.antsmasher.model.socket.SocketMessage;

import static com.tikalk.antsmasher.networking.ApiContract.LR_MESSAGE;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * @author moshe on 2017/11/26.
 */
public class WebSocketTests extends TestSuite {

    @Test
    public void testParse() throws Exception {
        InputStream in = getClass().getResourceAsStream("/ant_location_1.json");
        assertNotNull(in);
        Reader reader = new InputStreamReader(in);
        assertEquals('a', reader.read());

        Gson gson = new Gson();
        String[] socketMessages = gson.fromJson(reader, String[].class);
        reader.close();
        assertNotNull(socketMessages);
        assertEquals(1, socketMessages.length);
        SocketMessage socketMessage = gson.fromJson(socketMessages[0], SocketMessage.class);
        assertNotNull(socketMessage);
        assertEquals(SocketMessage.TYPE_RECORD, socketMessage.type);
        assertEquals(LR_MESSAGE, socketMessage.address);
        String body = socketMessage.body;
        assertNotNull(body);
        AntLocation location = gson.fromJson(body, AntLocation.class);
        assertNotNull(location);
        assertEquals("941dfa7a-9d1b-499b-9ba2-1d9e42dd7b2e", location.antId);
        assertEquals(3, location.speciesId);
        assertEquals(0.107f, location.xRate);
        assertEquals(0.22f, location.yRate);
    }

}
