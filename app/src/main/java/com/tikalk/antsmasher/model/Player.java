package com.tikalk.antsmasher.model;

/**
 * @author moshe on 2017/11/15.
 */

public class Player {

    private String name;

    public Player(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
