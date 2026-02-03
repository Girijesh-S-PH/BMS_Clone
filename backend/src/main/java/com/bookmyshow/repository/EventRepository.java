package com.bookmyshow.repository;

import com.bookmyshow.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByStatus(Event.EventStatus status);
    List<Event> findByEventType(Event.EventType eventType);
    List<Event> findByLanguageContainingIgnoreCase(String language);
    List<Event> findByGenreContainingIgnoreCase(String genre);
}
