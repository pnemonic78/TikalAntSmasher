package com.tikalk.antsmasher.model;

/**
 * Ant smash.
 */

public class AntSmash {

    public final String id;
    public final long timestamp;
    public final boolean user;

    public AntSmash(String id) {
        this(id, System.currentTimeMillis());
    }

    public AntSmash(String id, long timestamp) {
        this(id, timestamp, false);
    }

    public AntSmash(String id, boolean user) {
        this(id, System.currentTimeMillis(), user);
    }

    public AntSmash(String id, long timestamp, boolean user) {
        this.id = id;
        this.timestamp = timestamp;
        this.user = user;
    }
}
