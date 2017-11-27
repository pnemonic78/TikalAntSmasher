package com.tikalk.antsmasher.model.socket;

public class AntHitMessage extends SocketMessage<AntSmash> {


    public AntSmash antSmash;

    public AntHitMessage(AntSmash smash) {
        super(TYPE_SEND, HIT_TRAIL_ADDRESS);
        this.antSmash = smash;
    }
}
