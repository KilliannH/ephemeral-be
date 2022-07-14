package com.killiann.ephemeral.models;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
public class JwtRequestModel implements Serializable {

    @Id
    private long serialVersionUID;
    private String username;
    private String password;
    public JwtRequestModel() {
    }
    public JwtRequestModel(String username, String password) {
        super();
        this.username = username; this.password = password;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}