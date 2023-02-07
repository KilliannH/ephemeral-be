package com.killiann.ephemeral.models;
import com.google.gson.JsonObject;

import java.io.Serializable;

public class FbUserInfoResponse implements Serializable {
    // Here id represents a facebookId
    public String id;
    public String name;
    public String email;
    public JsonObject picture;
    public String imageUrl;

    FbUserInfoResponse() {}

    FbUserInfoResponse(String id, String name, String email, String imageUrl) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
    }

    FbUserInfoResponse(String id, String name, String email, JsonObject picture) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }
}
