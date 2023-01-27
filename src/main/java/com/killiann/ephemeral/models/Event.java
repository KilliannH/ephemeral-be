package com.killiann.ephemeral.models;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String facebookId;
    private String name;

    @ManyToOne()
    @JoinColumn(name = "location", nullable = false)
    private Location location;
    @ManyToOne()
    @JoinColumn(name="owner", nullable=false)
    private UserModel owner;

    @ManyToMany(mappedBy = "events")
    private Set<UserModel> attendees = new HashSet<>();

    private Long dateTime;

    public Event(String facebookId, String name, Location location, UserModel owner, Long dateTime) {
        this.facebookId = facebookId;
        this.name = name;
        this.location = location;
        this.owner = owner;
        this.dateTime = dateTime;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public UserModel getOwner() {
        return owner;
    }

    public void setOwner(UserModel owner) {
        this.owner = owner;
    }

    public Long getDateTime() {
        return dateTime;
    }

    public void setDateTime(Long dateTime) {
        this.dateTime = dateTime;
    }

    public Set<UserModel> getAttendees() {
        return attendees;
    }

    public void setAttendees(Set<UserModel> attendees) {
        this.attendees = attendees;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", facebookId='" + facebookId + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location.toString() + '\'' +
                ", owner=" + owner +
                ", dateTime=" + dateTime +
                '}';
    }
}
