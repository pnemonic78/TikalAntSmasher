package com.tikalk.antsmasher.networking.requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by motibartov on 26/11/2017.
 */

public class CreateUserRequest {

    @SerializedName("name")
    private final String name;

    public CreateUserRequest(String name) {
        this.name = name;
    }
}
