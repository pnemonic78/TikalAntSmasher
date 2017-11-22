package com.tikalk.antsmasher.model.socket;

public class SmashSocketMessage extends SocketMessage<AntSmash> {

    public SmashSocketMessage(String type, String address, AntSmash smash) {
        super(type, address, smash);
    }
}
