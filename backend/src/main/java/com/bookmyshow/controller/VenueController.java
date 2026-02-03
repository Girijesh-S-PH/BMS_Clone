package com.bookmyshow.controller;

import com.bookmyshow.dto.ApiResponse;
import com.bookmyshow.dto.VenueDTO;
import com.bookmyshow.service.VenueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/venues")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class VenueController {

    private final VenueService venueService;

    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<VenueDTO>>> getAllVenues() {
        List<VenueDTO> venues = venueService.getAllVenues();
        return ResponseEntity.ok(ApiResponse.<List<VenueDTO>>builder()
                .success(true)
                .message("Venues retrieved successfully")
                .data(venues)
                .build());
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<ApiResponse<List<VenueDTO>>> getVenuesByCity(@PathVariable String city) {
        List<VenueDTO> venues = venueService.getVenuesByCity(city);
        return ResponseEntity.ok(ApiResponse.<List<VenueDTO>>builder()
                .success(true)
                .message("Venues retrieved successfully")
                .data(venues)
                .build());
    }

    @GetMapping("/{venueId}")
    public ResponseEntity<ApiResponse<VenueDTO>> getVenueById(@PathVariable Long venueId) {
        VenueDTO venue = venueService.getVenueById(venueId);
        return ResponseEntity.ok(ApiResponse.<VenueDTO>builder()
                .success(true)
                .message("Venue retrieved successfully")
                .data(venue)
                .build());
    }
}
