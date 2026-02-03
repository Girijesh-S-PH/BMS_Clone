package com.bookmyshow.service;

import com.bookmyshow.dto.EventDTO;
import com.bookmyshow.entity.Event;
import com.bookmyshow.entity.Show;
import com.bookmyshow.entity.Seat;
import com.bookmyshow.entity.Booking;
import com.bookmyshow.exception.ResourceNotFoundException;
import com.bookmyshow.repository.EventRepository;
import com.bookmyshow.repository.ShowRepository;
import com.bookmyshow.repository.SeatRepository;
import com.bookmyshow.repository.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final ShowRepository showRepository;
    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;

    public EventService(EventRepository eventRepository,
                        ShowRepository showRepository,
                        SeatRepository seatRepository,
                        BookingRepository bookingRepository) {
        this.eventRepository = eventRepository;
        this.showRepository = showRepository;
        this.seatRepository = seatRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EventDTO> getEventsByStatus(Event.EventStatus status) {
        return eventRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EventDTO> getEventsByType(Event.EventType eventType) {
        return eventRepository.findByEventType(eventType).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EventDTO> searchEventsByTitle(String title) {
        return eventRepository.findAll().stream()
                .filter(e -> e.getTitle().toLowerCase().contains(title.toLowerCase()))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EventDTO> getEventsByLanguage(String language) {
        return eventRepository.findByLanguageContainingIgnoreCase(language).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EventDTO> getEventsByGenre(String genre) {
        return eventRepository.findByGenreContainingIgnoreCase(genre).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public EventDTO getEventById(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));
        return convertToDTO(event);
    }

    public EventDTO createEvent(EventDTO eventDTO) {
        Event event = Event.builder()
                .title(eventDTO.getTitle())
                .description(eventDTO.getDescription())
                .posterUrl(eventDTO.getPosterUrl())
                .genre(eventDTO.getGenre())
                .language(eventDTO.getLanguage())
                .durationMinutes(eventDTO.getDurationMinutes())
                .rating(eventDTO.getRating())
                .trailerUrl(eventDTO.getTrailerUrl())
                .castList(eventDTO.getCast())
                .crew(eventDTO.getCrew())
                .status(Event.EventStatus.valueOf(eventDTO.getStatus()))
                .eventType(Event.EventType.valueOf(eventDTO.getEventType()))
                .releaseDate(eventDTO.getReleaseDate())
                .build();
        
        event = eventRepository.save(event);
        return convertToDTO(event);
    }

    public EventDTO updateEvent(Long eventId, EventDTO eventDTO) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));
        
        event.setTitle(eventDTO.getTitle());
        event.setDescription(eventDTO.getDescription());
        event.setPosterUrl(eventDTO.getPosterUrl());
        event.setGenre(eventDTO.getGenre());
        event.setLanguage(eventDTO.getLanguage());
        event.setDurationMinutes(eventDTO.getDurationMinutes());
        event.setRating(eventDTO.getRating());
        event.setTrailerUrl(eventDTO.getTrailerUrl());
        event.setCastList(eventDTO.getCast());
        event.setCrew(eventDTO.getCrew());
        event.setStatus(Event.EventStatus.valueOf(eventDTO.getStatus()));
        event.setEventType(Event.EventType.valueOf(eventDTO.getEventType()));
        event.setReleaseDate(eventDTO.getReleaseDate());
        
        event = eventRepository.save(event);
        return convertToDTO(event);
    }

    @Transactional
    public void deleteEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));

        // Delete all bookings linked to this event
        for (Booking booking : bookingRepository.findByEventId(eventId)) {
            bookingRepository.delete(booking);
        }

        // Delete all shows (and their seats) linked to this event
        for (Show show : showRepository.findByEventId(eventId)) {
            Long showId = show.getId();
            for (Seat seat : seatRepository.findByShowId(showId)) {
                seatRepository.delete(seat);
            }
            showRepository.delete(show);
        }

        eventRepository.delete(event);
    }

    public List<EventDTO> sortEvents(List<EventDTO> events, String sort) {
        if (events == null || sort == null) return events;
        if ("rating".equalsIgnoreCase(sort)) {
            return events.stream().sorted(Comparator.comparing(EventDTO::getRating, Comparator.nullsLast(Comparator.reverseOrder()))).collect(Collectors.toList());
        }
        if ("releaseDate".equalsIgnoreCase(sort)) {
            return events.stream().sorted(Comparator.comparing(EventDTO::getReleaseDate, Comparator.nullsLast(Comparator.reverseOrder()))).collect(Collectors.toList());
        }
        return events;
    }

    private EventDTO convertToDTO(Event event) {
        return EventDTO.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .posterUrl(event.getPosterUrl())
                .genre(event.getGenre())
                .language(event.getLanguage())
                .durationMinutes(event.getDurationMinutes())
                .rating(event.getRating())
                .reviewCount(event.getReviewCount())
                .trailerUrl(event.getTrailerUrl())
                .cast(event.getCastList())
                .crew(event.getCrew())
                .status(event.getStatus().toString())
                .eventType(event.getEventType().toString())
                .releaseDate(event.getReleaseDate())
                .build();
    }
}
