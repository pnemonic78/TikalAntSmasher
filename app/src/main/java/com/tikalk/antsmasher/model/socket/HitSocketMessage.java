package com.tikalk.antsmasher.model.socket;

import com.google.gson.annotations.SerializedName;

public class HitSocketMessage extends SocketMessage {


    @SerializedName("body")
    public AntSmash antSmash;

    public HitSocketMessage(String type, String address, AntSmash smash) {
        super(type, address);
        this.antSmash = smash;
    }
}
