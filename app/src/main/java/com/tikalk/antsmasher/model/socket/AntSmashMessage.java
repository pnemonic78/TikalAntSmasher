package com.tikalk.antsmasher.model.socket;


public class AntSmashMessage extends SocketMessage {

    private final AntSmash smash;

    public AntSmashMessage(AntSmash smash) {
        super("send", "toAddress");
        this.smash = smash;
    }
}
