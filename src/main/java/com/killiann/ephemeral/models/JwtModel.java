package com.killiann.ephemeral.models;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
public class JwtModel implements Serializable {

    @Id
    private long serialVersionUID;
    private String token;
    public JwtModel(String token) {
        this.token = token;
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}