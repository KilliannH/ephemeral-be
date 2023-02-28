package com.killiann.ephemeral.exceptions;

public class LocationNotFoundException extends RuntimeException {
    public LocationNotFoundException(Long id) {
        super("Could not find location with id: " + id);
    }
    public LocationNotFoundException(String city) {
        super("Could not find location with city: " + city);
    }
}
