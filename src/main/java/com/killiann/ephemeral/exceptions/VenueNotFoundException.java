package com.killiann.ephemeral.exceptions;

public class VenueNotFoundException extends RuntimeException {
    public VenueNotFoundException(Long id) {
        super("Could not find venue with id: " + id);
    }
}
