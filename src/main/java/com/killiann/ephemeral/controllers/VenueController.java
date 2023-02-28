package com.killiann.ephemeral.controllers;

import com.killiann.ephemeral.exceptions.EventNotFoundException;
import com.killiann.ephemeral.exceptions.LocationNotFoundException;
import com.killiann.ephemeral.exceptions.UserNotFoundException;
import com.killiann.ephemeral.exceptions.VenueNotFoundException;
import com.killiann.ephemeral.models.Event;
import com.killiann.ephemeral.models.Location;
import com.killiann.ephemeral.models.UserModel;
import com.killiann.ephemeral.models.Venue;
import com.killiann.ephemeral.repositories.EventRepository;
import com.killiann.ephemeral.repositories.LocationRepository;
import com.killiann.ephemeral.repositories.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class VenueController {
    @Autowired
    private VenueRepository venueRepository;
    @Autowired
    private LocationRepository locationRepository;
    @GetMapping("/venues/limit")
    Page<Venue> limit(@RequestParam Integer start, @RequestParam Integer end) {
        return venueRepository.findAll(
                PageRequest.of(start, end));
    }

    @GetMapping("/venues")
    List<Venue> all() {
        return venueRepository.findAll();
    }

    @GetMapping("/venues/{id}")
    Venue one(@PathVariable Long id) {
        return venueRepository.findById(id)
                .orElseThrow(() -> new VenueNotFoundException(id));
    }

    @GetMapping("/venues/location/{locationId}")
    Set<Venue> byLocationId(@PathVariable Long locationId) {
        return venueRepository.findAllByLocationId(locationId)
                .orElseThrow(() -> new VenueNotFoundException(locationId));
    }

    @PostMapping("/venues")
    Venue add(@RequestBody Venue newVenue) {
        return venueRepository.save(newVenue);
    }

    @PutMapping("/venues/{id}")
    Venue replaceVenue(@RequestBody Venue newVenue, @PathVariable Long id) {

        return venueRepository.findById(id)
                .map(venue -> {
                    venue.setLng(newVenue.getLng());
                    venue.setLat(newVenue.getLat());
                    venue.setAddress(newVenue.getAddress());
                    venue.setName(newVenue.getName());
                    return venueRepository.save(venue);
                })
                .orElseThrow(() -> new VenueNotFoundException(id));
    }

    @PostMapping("/venues/{id}/location/{locationId}")
    Venue addLocation(@PathVariable Long id, @PathVariable Long locationId) {
        return venueRepository.findById(id)
                .map(venue -> {
                    Location location = locationRepository.findById(locationId)
                            .orElseThrow(() -> new LocationNotFoundException(locationId));
                    venue.setLocation(location);
                    return venueRepository.save(venue);
                }).orElseThrow(() -> new VenueNotFoundException(id));
    }

    @DeleteMapping("/venues/{id}/location/{locationId}")
    Venue removeLocation(@PathVariable Long id, @PathVariable Long locationId) {
        return venueRepository.findById(id)
                .map(venue -> {
                    // no need for location here,
                    // we just check if it doesn't exist
                    // just throw and do nothing
                    Location location = locationRepository.findById(locationId)
                            .orElseThrow(() -> new LocationNotFoundException(locationId));
                    venue.setLocation(null);
                    return venueRepository.save(venue);
                }).orElseThrow(() -> new VenueNotFoundException(id));
    }

}
