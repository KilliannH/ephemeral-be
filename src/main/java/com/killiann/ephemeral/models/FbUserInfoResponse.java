package com.killiann.ephemeral.models;

import com.google.gson.JsonObject;

import java.io.Serializable;

public class FbUserInfoResponse implements Serializable {
    public String id;
    public String name;
    public String email;
    public JsonObject picture;

    FbUserInfoResponse() {}

    FbUserInfoResponse(String id, String name, String email, JsonObject picture) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }
}
