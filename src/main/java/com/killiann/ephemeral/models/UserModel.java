package com.killiann.ephemeral.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "user")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String facebookId;
    private String username;
    private String email;

    private String imageUrl;
    private String role;

    @OneToMany(mappedBy = "owner")
    private Set<Event> ownedEvents = new HashSet<>();

    @ManyToMany(mappedBy = "attendees")
    private Set<Event> savedEvents = new HashSet<>();

    public UserModel() {}

    public UserModel(String username, String facebookId, String email, String imageUrl, String role) {
        this.username = username;
        this.facebookId = facebookId;
        this.email = email;
        this.imageUrl = imageUrl;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public Set<Event> getOwnedEvents() {
        return ownedEvents;
    }

    public void setOwnedEvents(Set<Event> ownedEvents) {
        this.ownedEvents = ownedEvents;
    }

    public Set<Event> getSavedEvents() {
        return savedEvents;
    }

    public void setSavedEvents(Set<Event> savedEvents) {
        this.savedEvents = savedEvents;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                "facebookId='" + facebookId + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
