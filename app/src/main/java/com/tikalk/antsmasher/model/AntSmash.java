package com.tikalk.antsmasher.model;

/**
 * Ant smash.
 */

public class AntSmash {

    public final int id;
    public final long timestamp;
    public final boolean user;

    public AntSmash(int id) {
        this(id, System.currentTimeMillis());
    }

    public AntSmash(int id, long timestamp) {
        this(id, timestamp, false);
    }

    public AntSmash(int id, boolean user) {
        this(id, System.currentTimeMillis(), user);
    }

    public AntSmash(int id, long timestamp, boolean user) {
        this.id = id;
        this.timestamp = timestamp;
        this.user = user;
    }
}
