package com.tikalk.antsmasher.model.socket;

public class AntLocationMessage extends SocketMessage<AntLocation> {

    public static final String TYPE_LOCATION = "location";

    public AntLocationMessage(String address, AntLocation location) {
        super(TYPE_LOCATION, address, location);
    }
}
