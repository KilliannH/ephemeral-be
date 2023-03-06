package com.killiann.ephemeral.controllers;

import com.killiann.ephemeral.exceptions.EventNotFoundException;
import com.killiann.ephemeral.models.Event;
import com.killiann.ephemeral.repositories.EventRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
@Api(value = "Event Controller", protocols = "http")
public class EventController {
    @Autowired
    private EventRepository eventRepository;

    @ApiOperation(value = "get events limit by start & end", response = Event.class, code = 200)
    @GetMapping("/events/limit")
    Page<Event> limit(@RequestParam Integer start, @RequestParam Integer end) {
        return eventRepository.findAll(
                PageRequest.of(start, end));
    }

    @GetMapping("/events")
    List<Event> all() {
        return eventRepository.findAll();
    }

    @GetMapping("/events/{id}")
    Event one(@PathVariable Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));
    }
}
