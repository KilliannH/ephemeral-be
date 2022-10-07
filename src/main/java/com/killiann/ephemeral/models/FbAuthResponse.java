package com.killiann.ephemeral.models;

import java.io.Serializable;

public class FbAuthResponse implements Serializable {
    private String facebookId;
    private String accessToken;

    FbAuthResponse(String facebookId, String accessToken) {
        this.facebookId = facebookId;
        this.accessToken = accessToken;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
