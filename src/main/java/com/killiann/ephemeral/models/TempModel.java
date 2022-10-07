package com.killiann.ephemeral.models;

import java.io.Serializable;

public class TempModel implements Serializable {
    public String accessToken;

    public TempModel() {
    }
    public TempModel(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
