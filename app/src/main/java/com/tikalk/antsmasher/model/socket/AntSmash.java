package com.tikalk.antsmasher.model.socket;

import com.google.gson.annotations.Expose;

/**
 * Ant smash to server.
 */

public class AntSmash {

    public final String antId;
    public final String type;

    @Expose(serialize = false, deserialize = false)
    public boolean smashedByUser = false;

    public AntSmash(String type, String id) {
        this.type = type;
        this.antId = id;
    }

    public AntSmash(String type, String id, boolean smashedByUser) {
        this.type = type;
        this.antId = id;
        this.smashedByUser = smashedByUser;
    }
}
