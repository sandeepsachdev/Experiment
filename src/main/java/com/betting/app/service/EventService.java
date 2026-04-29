package com.betting.app.service;

import com.betting.app.model.Event;
import com.betting.app.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getEvents(String category, String status) {
        if (category != null && status != null) {
            return eventRepository.findByCategoryAndStatus(category.toUpperCase(), status.toUpperCase());
        }
        if (category != null) {
            return eventRepository.findByCategory(category.toUpperCase());
        }
        if (status != null) {
            return eventRepository.findByStatus(status.toUpperCase());
        }
        return eventRepository.findAll();
    }

    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public List<String> getCategories() {
        return eventRepository.findAll().stream()
            .map(Event::getCategory)
            .distinct()
            .sorted()
            .toList();
    }
}
