package com.bookmyshow.controller;

import com.bookmyshow.dto.ApiResponse;
import com.bookmyshow.dto.EventDTO;
import com.bookmyshow.dto.ReviewDTO;
import com.bookmyshow.entity.Event;
import com.bookmyshow.service.EventService;
import com.bookmyshow.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class EventController {

    private final EventService eventService;
    private final ReviewService reviewService;

    public EventController(EventService eventService, ReviewService reviewService) {
        this.eventService = eventService;
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EventDTO>>> getAllEvents(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sort) {

        List<EventDTO> events;
        if (search != null && !search.isEmpty()) {
            events = eventService.searchEventsByTitle(search);
        } else if (status != null) {
            events = eventService.getEventsByStatus(Event.EventStatus.valueOf(status.toUpperCase()));
        } else if (type != null) {
            events = eventService.getEventsByType(Event.EventType.valueOf(type.toUpperCase()));
        } else if (language != null) {
            events = eventService.getEventsByLanguage(language);
        } else if (genre != null) {
            events = eventService.getEventsByGenre(genre);
        } else {
            events = eventService.getAllEvents();
        }
        if (sort != null && !sort.isEmpty()) {
            events = eventService.sortEvents(events, sort);
        }
        return ResponseEntity.ok(ApiResponse.<List<EventDTO>>builder()
                .success(true)
                .message("Events retrieved successfully")
                .data(events)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EventDTO>> getEventById(@PathVariable Long id) {
        EventDTO event = eventService.getEventById(id);
        return ResponseEntity.ok(ApiResponse.<EventDTO>builder()
                .success(true)
                .message("Event retrieved successfully")
                .data(event)
                .build());
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<ApiResponse<List<ReviewDTO>>> getEventReviews(@PathVariable Long id) {
        List<ReviewDTO> reviews = reviewService.getReviewsByEventId(id);
        return ResponseEntity.ok(ApiResponse.<List<ReviewDTO>>builder()
                .success(true)
                .message("Reviews retrieved")
                .data(reviews)
                .build());
    }

    @PostMapping("/{id}/reviews")
    public ResponseEntity<ApiResponse<ReviewDTO>> addReview(
            @PathVariable Long id,
            @RequestBody ReviewDTO reviewDTO,
            @RequestHeader("X-User-Id") Long userId) {

        ReviewDTO created = reviewService.addReview(id, userId, reviewDTO.getRating(), reviewDTO.getComment());
        return ResponseEntity.ok(ApiResponse.<ReviewDTO>builder()
                .success(true)
                .message("Review added")
                .data(created)
                .build());
    }
}
