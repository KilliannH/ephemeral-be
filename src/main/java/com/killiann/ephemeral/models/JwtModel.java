package com.killiann.ephemeral.models;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
public class JwtModel implements Serializable {
    public String token;
    public JwtModel(String token) {
        this.token = token;
    }
}