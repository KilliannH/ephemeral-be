package com.killiann.ephemeral.models;

import java.io.Serializable;

public class JwtResponseModel implements Serializable {
    public String token;
    public UserModel userModel;
    public JwtResponseModel(String token, UserModel userModel) {
        this.token = token;
        this.userModel = userModel;
    }
}