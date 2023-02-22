package com.killiann.ephemeral.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double lng;
    private Double lat;
    private String city;
    private Integer postcode;
    private String country;

    @OneToMany(mappedBy = "location")
    private Set<Venue> venues = new HashSet<>();

    @OneToMany(mappedBy = "preferredLocation")
    private Set<UserModel> users = new HashSet<>();

    /* Default constructor */
    public Location() {}
    public Location(Double lng, Double lat, String city,Integer postcode, String country) {
        this.lng = lng;
        this.lat = lat;
        this.city = city;
        this.postcode = postcode;
        this.country = country;
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

    public Integer getPostcode() { return this.postcode; }

    public void setPostcode(Integer postcode) { this.postcode = postcode; }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Set<Venue> getVenues() {
        return venues;
    }

    public void setVenues(Set<Venue> venues) {
        this.venues = venues;
    }

    public Set<UserModel> getUsers() {
        return users;
    }

    public void setUsers(Set<UserModel> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Venue{" +
                "id=" + id +
                ", lng=" + lng +
                ", lat=" + lat +
                ", city='" + city + '\'' +
                ", postcode='" + postcode + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
