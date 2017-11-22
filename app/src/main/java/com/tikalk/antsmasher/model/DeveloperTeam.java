package com.tikalk.antsmasher.model;

import com.google.gson.annotations.SerializedName;

/**
 * Developer team.
 */

public class DeveloperTeam {

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;

    public DeveloperTeam(String id, String name) {
        setId(id);
        setName(name);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
