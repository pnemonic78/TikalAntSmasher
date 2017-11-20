package com.tikalk.antsmasher.model;


public class AntSmashMessage extends SocketMessage {

    private final AntSmash smash;

    public AntSmashMessage(AntSmash smash) {
        super("send", "toAddress");
        this.smash = smash;
    }
}
