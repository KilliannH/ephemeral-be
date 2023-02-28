package com.killiann.ephemeral.controllers;

import com.killiann.ephemeral.exceptions.LocationNotFoundException;
import com.killiann.ephemeral.exceptions.VenueNotFoundException;
import com.killiann.ephemeral.models.Location;
import com.killiann.ephemeral.models.Venue;
import com.killiann.ephemeral.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class LocationController {
    @Autowired
    private LocationRepository locationRepository;

    @GetMapping("/locations")
    List<Location> all() {
        return locationRepository.findAll();
    }

    @GetMapping("/locations/{id}")
    Location one(@PathVariable Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new LocationNotFoundException(id));
    }

    @GetMapping("/locations/city/{city}")
    Location one(@PathVariable String city) {
        return locationRepository.findByCity(city)
                .orElseThrow(() -> new LocationNotFoundException(city));
    }

    @PostMapping("/locations")
    Location add(@RequestBody Location newLocation) {
        return locationRepository.save(newLocation);
    }

    @PutMapping("/locations/{id}")
    Location replaceLocation(@RequestBody Location newLocation, @PathVariable Long id) {

        return locationRepository.findById(id)
                .map(location -> {
                    location.setLng(newLocation.getLng());
                    location.setLat(newLocation.getLat());
                    location.setCity(newLocation.getCity());
                    location.setCountry(newLocation.getCountry());
                    location.setZipcode(newLocation.getZipcode());
                    return locationRepository.save(location);
                })
                .orElseThrow(() -> new LocationNotFoundException(id));
    }
}
