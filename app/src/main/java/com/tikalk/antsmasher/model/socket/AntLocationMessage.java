package com.tikalk.antsmasher.model.socket;

public class AntLocationMessage extends SocketMessage {


    AntLocation antLocation;


    public AntLocationMessage(String type, String address, AntLocation location) {
        super(type, address);

        this.antLocation = location;
    }


    public AntLocation getAntLocation() {
        return antLocation;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", antLocation=" + antLocation;
    }
}
