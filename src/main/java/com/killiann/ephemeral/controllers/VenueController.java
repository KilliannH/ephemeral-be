package com.killiann.ephemeral.controllers;

import com.killiann.ephemeral.exceptions.EventNotFoundException;
import com.killiann.ephemeral.exceptions.VenueNotFoundException;
import com.killiann.ephemeral.models.Event;
import com.killiann.ephemeral.models.Venue;
import com.killiann.ephemeral.repositories.EventRepository;
import com.killiann.ephemeral.repositories.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class VenueController {
    @Autowired
    private VenueRepository venueRepository;

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
}
