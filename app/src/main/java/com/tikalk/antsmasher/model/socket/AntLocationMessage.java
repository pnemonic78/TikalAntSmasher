package com.tikalk.antsmasher.model.socket;


public class AntLocationMessage extends SocketMessage {

    private final AntLocation antLocation;

    public AntLocationMessage(String type, String address, AntLocation location) {
        super(type, address);
        this.antLocation = location;
    }

    public AntLocation getAntLocation() {
        return antLocation;
    }
}
