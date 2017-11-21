package com.tikalk.antsmasher.model.socket;


public class AntSocketMessage extends SocketMessage {

    private final AntLocation antLocation;

    public AntSocketMessage(String type, String address, AntLocation location) {
        super(type, address);
        this.antLocation = location;
    }

    public AntLocation getAntLocation() {
        return antLocation;
    }
}
