package com.bookmyshow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponse {
    private Long bookingId;
    private String bookingCode;
    private String eventTitle;
    private String venueName;
    private LocalDateTime showDateTime;
    private List<String> seatNumbers;
    private Integer numberOfSeats;
    private Double subtotal;
    private Double convenienceFee;
    private Double totalAmount;
    private String status;
    private LocalDateTime bookingDate;
}
