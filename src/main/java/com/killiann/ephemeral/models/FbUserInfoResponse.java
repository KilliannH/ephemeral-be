package com.killiann.ephemeral.models;

import java.io.Serializable;

public class FbUserInfoResponse implements Serializable {
    public String id;
    public String name;

    FbUserInfoResponse() {}

    FbUserInfoResponse(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
