package com.betting.app.repository;

import com.betting.app.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByCategory(String category);
    List<Event> findByStatus(String status);
    List<Event> findByCategoryAndStatus(String category, String status);
    List<Event> findByFeaturedTrue();
}
