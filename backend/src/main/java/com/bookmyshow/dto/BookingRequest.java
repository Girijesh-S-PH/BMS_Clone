package com.bookmyshow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequest {
    private Long showId;
    private List<String> seatNumbers; // e.g., ["A1", "A2", "A3"]
}
