package com.bookmyshow.controller;

import com.bookmyshow.dto.EventDTO;
import com.bookmyshow.dto.ShowDTO;
import com.bookmyshow.dto.VenueDTO;
import com.bookmyshow.dto.ApiResponse;
import com.bookmyshow.service.EventService;
import com.bookmyshow.service.ShowService;
import com.bookmyshow.service.VenueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {

    private final EventService eventService;
    private final VenueService venueService;
    private final ShowService showService;

    public AdminController(EventService eventService, VenueService venueService, ShowService showService) {
        this.eventService = eventService;
        this.venueService = venueService;
        this.showService = showService;
    }

    // ==================== EVENT MANAGEMENT ====================

    @PostMapping("/events")
    public ResponseEntity<ApiResponse<EventDTO>> createEvent(@RequestBody EventDTO eventDTO) {
        EventDTO createdEvent = eventService.createEvent(eventDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<EventDTO>builder()
                        .success(true)
                        .message("Event created successfully")
                        .data(createdEvent)
                        .build());
    }

    @PutMapping("/events/{eventId}")
    public ResponseEntity<ApiResponse<EventDTO>> updateEvent(
            @PathVariable Long eventId,
            @RequestBody EventDTO eventDTO) {
        EventDTO updatedEvent = eventService.updateEvent(eventId, eventDTO);
        return ResponseEntity.ok(ApiResponse.<EventDTO>builder()
                .success(true)
                .message("Event updated successfully")
                .data(updatedEvent)
                .build());
    }

    @DeleteMapping("/events/{eventId}")
    public ResponseEntity<ApiResponse<Void>> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Event deleted successfully")
                .build());
    }

    // ==================== VENUE MANAGEMENT ====================

    @PostMapping("/venues")
    public ResponseEntity<ApiResponse<VenueDTO>> createVenue(@RequestBody VenueDTO venueDTO) {
        VenueDTO createdVenue = venueService.createVenue(venueDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<VenueDTO>builder()
                        .success(true)
                        .message("Venue created successfully")
                        .data(createdVenue)
                        .build());
    }

    @PutMapping("/venues/{venueId}")
    public ResponseEntity<ApiResponse<VenueDTO>> updateVenue(
            @PathVariable Long venueId,
            @RequestBody VenueDTO venueDTO) {
        VenueDTO updatedVenue = venueService.updateVenue(venueId, venueDTO);
        return ResponseEntity.ok(ApiResponse.<VenueDTO>builder()
                .success(true)
                .message("Venue updated successfully")
                .data(updatedVenue)
                .build());
    }

    @DeleteMapping("/venues/{venueId}")
    public ResponseEntity<ApiResponse<Void>> deleteVenue(@PathVariable Long venueId) {
        venueService.deleteVenue(venueId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Venue deleted successfully")
                .build());
    }

    // ==================== SHOW MANAGEMENT ====================

    @PostMapping("/shows")
    public ResponseEntity<ApiResponse<ShowDTO>> createShow(@RequestBody ShowDTO showDTO) {
        ShowDTO createdShow = showService.createShow(showDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<ShowDTO>builder()
                        .success(true)
                        .message("Show created successfully")
                        .data(createdShow)
                        .build());
    }

    @GetMapping("/shows")
    public ResponseEntity<ApiResponse<List<ShowDTO>>> getAllShows() {
        List<ShowDTO> shows = showService.getAllShows();
        return ResponseEntity.ok(ApiResponse.<List<ShowDTO>>builder()
                .success(true)
                .message("Shows retrieved successfully")
                .data(shows)
                .build());
    }

    @GetMapping("/shows/{showId}/update-availability")
    public ResponseEntity<ApiResponse<ShowDTO>> updateShowAvailability(@PathVariable Long showId) {
        ShowDTO updatedShow = showService.updateShowAvailability(showId);
        return ResponseEntity.ok(ApiResponse.<ShowDTO>builder()
                .success(true)
                .message("Show availability updated")
                .data(updatedShow)
                .build());
    }

    @DeleteMapping("/shows/{showId}")
    public ResponseEntity<ApiResponse<Void>> deleteShow(@PathVariable Long showId) {
        showService.deleteShow(showId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Show deleted successfully")
                .build());
    }

    // ==================== DASHBOARD STATS ====================

    @GetMapping("/stats/dashboard")
    public ResponseEntity<ApiResponse<?>> getDashboardStats() {
        // Simple dashboard stats endpoint
        DashboardStats stats = DashboardStats.builder()
                .totalEvents((long) eventService.getAllEvents().size())
                .totalVenues((long) venueService.getAllVenues().size())
                .build();
        return ResponseEntity.ok(ApiResponse.<DashboardStats>builder()
                .success(true)
                .message("Dashboard stats fetched")
                .data(stats)
                .build());
    }

        // Inner class for dashboard stats
        public static class DashboardStats {
                private Long totalEvents;
                private Long totalVenues;

                public DashboardStats() {
                }

                public DashboardStats(Long totalEvents, Long totalVenues) {
                        this.totalEvents = totalEvents;
                        this.totalVenues = totalVenues;
                }

                public Long getTotalEvents() {
                        return totalEvents;
                }

                public void setTotalEvents(Long totalEvents) {
                        this.totalEvents = totalEvents;
                }

                public Long getTotalVenues() {
                        return totalVenues;
                }

                public void setTotalVenues(Long totalVenues) {
                        this.totalVenues = totalVenues;
                }

                public static DashboardStatsBuilder builder() {
                        return new DashboardStatsBuilder();
                }

                public static class DashboardStatsBuilder {
                        private Long totalEvents;
                        private Long totalVenues;

                        public DashboardStatsBuilder totalEvents(Long totalEvents) {
                                this.totalEvents = totalEvents;
                                return this;
                        }

                        public DashboardStatsBuilder totalVenues(Long totalVenues) {
                                this.totalVenues = totalVenues;
                                return this;
                        }

                        public DashboardStats build() {
                                return new DashboardStats(totalEvents, totalVenues);
                        }
                }
        }
}
