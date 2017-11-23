package com.tikalk.antsmasher.model;

import com.google.gson.annotations.SerializedName;

/**
 * Developer team.
 */

public enum DeveloperTeam {

    DEV_TEAM_1("dev_team_1", "Dev. Team 1", "http://socket1.tikalk.com", "http://socket2.tikalk.com", "http://socket3.tikalk.com"),
    DEV_TEAM_2("dev_team_2", "Dev. Team 2", "http://socket1.tikalk.com", "http://socket2.tikalk.com", "http://socket3.tikalk.com"),
    DEV_TEAM_3("dev_team_3", "Dev. Team 3", "http://socket1.tikalk.com", "http://socket2.tikalk.com", "http://socket3.tikalk.com"),
    DEV_TEAM_4("dev_team_4", "Dev. Team 4", "http://socket1.tikalk.com", "http://socket2.tikalk.com", "http://socket3.tikalk.com"),
    DEV_TEAM_5("dev_team_5", "Dev. Team 5", "http://socket1.tikalk.com", "http://socket2.tikalk.com", "http://socket3.tikalk.com"),
    DEV_TEAM_6("dev_team_6", "Dev. Team 6", "http://socket1.tikalk.com", "http://socket2.tikalk.com", "http://socket3.tikalk.com");

    @SerializedName("id")
    private final String id;
    @SerializedName("name")
    private final String name;
    @SerializedName("address1")
    private final String address1;
    @SerializedName("address2")
    private final String address2;
    @SerializedName("address3")
    private final String address3;

    DeveloperTeam(String id, String name, String address1, String address2, String address3) {
        this.id = id;
        this.name = name;
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getAddress3() {
        return address3;
    }

    @Override
    public String toString() {
        return getName();
    }

    public static DeveloperTeam find(String id) {
        if (id == null) {
            return null;
        }
        for (DeveloperTeam team : values()) {
            if (team.id.equals(id)) {
                return team;
            }
        }
        return null;
    }
}
