package com.bookmyshow.controller;

import com.bookmyshow.dto.ApiResponse;
import com.bookmyshow.dto.BookingDTO;
import com.bookmyshow.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@CrossOrigin(origins = "http://localhost:5173")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BookingDTO>> createBooking(
            @RequestBody BookingDTO bookingDTO,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {

        Long effectiveUserId = (userId != null) ? userId : bookingDTO.getUserId();
        if (effectiveUserId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<BookingDTO>builder()
                            .success(false)
                            .message("User ID is required (X-User-Id header or userId in body)")
                            .build());
        }

        BookingDTO createdBooking = bookingService.createBooking(bookingDTO, effectiveUserId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<BookingDTO>builder()
                        .success(true)
                        .message("Booking created successfully")
                        .data(createdBooking)
                        .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BookingDTO>>> getUserBookings(
            @RequestHeader("X-User-Id") Long userId) {

        List<BookingDTO> bookings = bookingService.getUserBookings(userId);

        return ResponseEntity.ok(ApiResponse.<List<BookingDTO>>builder()
                .success(true)
                .message("Bookings retrieved successfully")
                .data(bookings)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingDTO>> getBookingById(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {

        BookingDTO booking = bookingService.getBookingById(id, userId);

        return ResponseEntity.ok(ApiResponse.<BookingDTO>builder()
                .success(true)
                .message("Booking retrieved successfully")
                .data(booking)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> cancelBooking(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {

        bookingService.cancelBooking(id, userId);

        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Booking cancelled successfully")
                .build());
    }
}
