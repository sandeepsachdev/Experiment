package com.betting.app.controller;

import com.betting.app.model.Event;
import com.betting.app.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<Event> getEvents(
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String status) {
        return eventService.getEvents(category, status);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable Long id) {
        return eventService.getEventById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/categories")
    public List<String> getCategories() {
        return eventService.getCategories();
    }
}
