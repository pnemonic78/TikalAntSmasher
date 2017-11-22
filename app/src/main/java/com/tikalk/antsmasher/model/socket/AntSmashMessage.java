package com.tikalk.antsmasher.model.socket;

public class AntSmashMessage extends SocketMessage<AntSmash> {

    public static final String TYPE_SMASH = "smash";

    public AntSmashMessage(AntSmash smash) {
        super(TYPE_SMASH, "TODO toAddress", smash);
    }
}
