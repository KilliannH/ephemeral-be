package com.killiann.ephemeral.models;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
public class JwtResponseModel implements Serializable {

    @Id
    private long serialVersionUID;
    private final String token;
    public JwtResponseModel(String token) {
        this.token = token;
    }
    public String getToken() {
        return token;
    }
}