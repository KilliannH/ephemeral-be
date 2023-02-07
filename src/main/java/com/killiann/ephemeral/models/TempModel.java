package com.killiann.ephemeral.models;

import java.io.Serializable;

public class TempModel implements Serializable {
    public String accessToken;
    public FbUserInfoResponse userInfos;

    public TempModel() {
    }
    public TempModel(String accessToken, FbUserInfoResponse userInfos) {
        this.accessToken = accessToken;
        this.userInfos = userInfos;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
