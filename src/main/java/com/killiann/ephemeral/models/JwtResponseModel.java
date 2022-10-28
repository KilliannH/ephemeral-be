package com.killiann.ephemeral.models;

import java.io.Serializable;

public class JwtResponseModel implements Serializable {
    public String token;
    public UserModel user;
    public JwtResponseModel(String token, UserModel user) {
        this.token = token;
        this.user = user;
    }
}