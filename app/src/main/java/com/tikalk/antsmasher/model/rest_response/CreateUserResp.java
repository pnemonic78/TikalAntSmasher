package com.tikalk.antsmasher.model.rest_response;

/**
 * Created by motibartov on 26/11/2017.
 */

public class CreateUserResp {
    long id;
    String name;


    public CreateUserResp(int id, String name) {
        this.id = id;
        this.name = name;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
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
        return "CreateUserResp{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
