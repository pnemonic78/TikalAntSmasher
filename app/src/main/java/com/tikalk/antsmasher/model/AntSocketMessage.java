package com.tikalk.antsmasher.model;


public class AntSocketMessage extends SocketMessage {

    String antLocation;

    public AntSocketMessage(String type, String address, String location) {
        super(type, address);
        this.antLocation = location;
    }

}
