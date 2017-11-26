package com.tikalk.antsmasher.model.socket;

public class AntLocationMessage extends SocketMessage<AntLocation> {


    public AntLocationMessage(String type, String address, AntLocation location) {
        super(type, address, location);
    }
}
