package com.killiann.ephemeral.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "venue")
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double lng;
    private Double lat;
    private String address;
    private String name;
    @JsonIgnoreProperties({"venues", "users"})
    @ManyToOne()
    @JoinColumn(name = "location")
    private Location location;

    @OneToMany(mappedBy = "venue")
    private Set<Event> events = new HashSet<>();

    /* Default constructor */
    public Venue() {}
    public Venue(Double lng, Double lat, String address, String name) {
        this.lng = lng;
        this.lat = lat;
        this.address = address;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Venue{" +
                "id=" + id +
                ", lng=" + lng +
                ", lat=" + lat +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
