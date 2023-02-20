package com.killiann.ephemeral.exceptions;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(Long id) {
        super("Could not find event with id: " + id);
    }

    public EventNotFoundException(String facebookId) { super("Could not find event with facebookId: " + facebookId); }
}
