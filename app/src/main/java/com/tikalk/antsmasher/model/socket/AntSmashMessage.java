package com.tikalk.antsmasher.model.socket;

public class AntSmashMessage extends SocketMessage {

    public static final String TYPE_SMASH = "smash";

    public AntSmashMessage(String smash) {
        super(TYPE_SMASH, "TODO toAddress", smash);
    }
}
