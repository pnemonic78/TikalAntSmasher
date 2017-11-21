package com.tikalk.antsmasher.model.socket;


public class AntSmashMessage extends SocketMessage {

    private final AntSmash smash;

    public AntSmashMessage(AntSmash smash) {
        super(TYPE_SEND, "TODO toAddress");
        this.smash = smash;
    }
}
