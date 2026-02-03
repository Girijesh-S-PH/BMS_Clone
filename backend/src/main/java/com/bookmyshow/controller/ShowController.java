package com.bookmyshow.controller;

import com.bookmyshow.dto.ApiResponse;
import com.bookmyshow.dto.SeatDTO;
import com.bookmyshow.dto.ShowDTO;
import com.bookmyshow.service.SeatService;
import com.bookmyshow.service.ShowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shows")
@CrossOrigin(origins = "http://localhost:5173")
public class ShowController {

    private final ShowService showService;
    private final SeatService seatService;

    public ShowController(ShowService showService, SeatService seatService) {
        this.showService = showService;
        this.seatService = seatService;
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<ApiResponse<List<ShowDTO>>> getShowsByEvent(@PathVariable Long eventId) {
        List<ShowDTO> shows = showService.getShowsByEventId(eventId);
        return ResponseEntity.ok(ApiResponse.<List<ShowDTO>>builder()
                .success(true)
                .message("Shows retrieved successfully")
                .data(shows)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ShowDTO>> getShowById(@PathVariable Long id) {
        ShowDTO show = showService.getShowById(id);
        return ResponseEntity.ok(ApiResponse.<ShowDTO>builder()
                .success(true)
                .message("Show retrieved successfully")
                .data(show)
                .build());
    }

    @GetMapping("/{showId}/seats")
    public ResponseEntity<ApiResponse<List<SeatDTO>>> getSeatsByShow(@PathVariable Long showId) {
        List<SeatDTO> seats = seatService.getSeatsByShowId(showId);
        return ResponseEntity.ok(ApiResponse.<List<SeatDTO>>builder()
                .success(true)
                .message("Seats retrieved successfully")
                .data(seats)
                .build());
    }

    @PostMapping("/{showId}/lock-seats")
    public ResponseEntity<ApiResponse<List<SeatDTO>>> lockSeats(
            @PathVariable Long showId,
            @RequestBody List<Long> seatIds,
            @RequestHeader(value = "X-User-Id", required = false) Long userId
    ) {
        List<SeatDTO> seats = seatService.lockSeats(showId, seatIds, userId, 10);
        return ResponseEntity.ok(ApiResponse.<List<SeatDTO>>builder()
                .success(true)
                .message("Seats locked successfully")
                .data(seats)
                .build());
    }

    @PostMapping("/{showId}/release-seats")
    public ResponseEntity<ApiResponse<List<SeatDTO>>> releaseSeats(
            @PathVariable Long showId,
            @RequestBody List<Long> seatIds,
            @RequestHeader(value = "X-User-Id", required = false) Long userId
    ) {
        List<SeatDTO> seats = seatService.releaseSeats(showId, seatIds, userId);
        return ResponseEntity.ok(ApiResponse.<List<SeatDTO>>builder()
                .success(true)
                .message("Seats released successfully")
                .data(seats)
                .build());
    }

    @PostMapping("/{showId}/generate-seats")
    public ResponseEntity<ApiResponse<String>> generateSeatsForShow(@PathVariable Long showId) {
        try {
            String result = showService.generateSeatsForExistingShow(showId);
            return ResponseEntity.ok(ApiResponse.<String>builder()
                    .success(true)
                    .message(result)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.<String>builder()
                    .success(false)
                    .message("Failed to generate seats: " + e.getMessage())
                    .build());
        }
    }
}
