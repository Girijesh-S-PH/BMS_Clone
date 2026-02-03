package com.bookmyshow.dto;

import java.time.LocalDateTime;
import java.util.List;

public class BookingDTO {
    private Long id;
    private Long userId;
    private Long showId;
    private Long eventId;
    private Long venueId;
    private String eventName;
    private String venueName;
    private String posterUrl;
    private LocalDateTime showDateTime;
    private List<Long> seatIds;
    private String seatNumbers;
    private Integer numberOfSeats;
    private Double totalAmount;
    private String bookingCode;
    private String status;
    private LocalDateTime createdAt;

    public BookingDTO() {
    }

    public BookingDTO(Long id, Long userId, Long showId, Long eventId, Long venueId, String eventName,
                      String venueName, String posterUrl, LocalDateTime showDateTime, List<Long> seatIds, String seatNumbers,
                      Integer numberOfSeats, Double totalAmount, String bookingCode, String status,
                      LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.showId = showId;
        this.eventId = eventId;
        this.venueId = venueId;
        this.eventName = eventName;
        this.venueName = venueName;
        this.posterUrl = posterUrl;
        this.showDateTime = showDateTime;
        this.seatIds = seatIds;
        this.seatNumbers = seatNumbers;
        this.numberOfSeats = numberOfSeats;
        this.totalAmount = totalAmount;
        this.bookingCode = bookingCode;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getShowId() {
        return showId;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getVenueId() {
        return venueId;
    }

    public void setVenueId(Long venueId) {
        this.venueId = venueId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public LocalDateTime getShowDateTime() {
        return showDateTime;
    }

    public void setShowDateTime(LocalDateTime showDateTime) {
        this.showDateTime = showDateTime;
    }

    public List<Long> getSeatIds() {
        return seatIds;
    }

    public void setSeatIds(List<Long> seatIds) {
        this.seatIds = seatIds;
    }

    public String getSeatNumbers() {
        return seatNumbers;
    }

    public void setSeatNumbers(String seatNumbers) {
        this.seatNumbers = seatNumbers;
    }

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(Integer numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static BookingDTOBuilder builder() {
        return new BookingDTOBuilder();
    }

    public static class BookingDTOBuilder {
        private Long id;
        private Long userId;
        private Long showId;
        private Long eventId;
        private Long venueId;
        private String eventName;
        private String venueName;
        private String posterUrl;
        private LocalDateTime showDateTime;
        private List<Long> seatIds;
        private String seatNumbers;
        private Integer numberOfSeats;
        private Double totalAmount;
        private String bookingCode;
        private String status;
        private LocalDateTime createdAt;

        public BookingDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public BookingDTOBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public BookingDTOBuilder showId(Long showId) {
            this.showId = showId;
            return this;
        }

        public BookingDTOBuilder eventId(Long eventId) {
            this.eventId = eventId;
            return this;
        }

        public BookingDTOBuilder venueId(Long venueId) {
            this.venueId = venueId;
            return this;
        }

        public BookingDTOBuilder eventName(String eventName) {
            this.eventName = eventName;
            return this;
        }

        public BookingDTOBuilder venueName(String venueName) {
            this.venueName = venueName;
            return this;
        }

        public BookingDTOBuilder posterUrl(String posterUrl) {
            this.posterUrl = posterUrl;
            return this;
        }

        public BookingDTOBuilder showDateTime(LocalDateTime showDateTime) {
            this.showDateTime = showDateTime;
            return this;
        }

        public BookingDTOBuilder seatIds(List<Long> seatIds) {
            this.seatIds = seatIds;
            return this;
        }

        public BookingDTOBuilder seatNumbers(String seatNumbers) {
            this.seatNumbers = seatNumbers;
            return this;
        }

        public BookingDTOBuilder numberOfSeats(Integer numberOfSeats) {
            this.numberOfSeats = numberOfSeats;
            return this;
        }

        public BookingDTOBuilder totalAmount(Double totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public BookingDTOBuilder bookingCode(String bookingCode) {
            this.bookingCode = bookingCode;
            return this;
        }

        public BookingDTOBuilder status(String status) {
            this.status = status;
            return this;
        }

        public BookingDTOBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public BookingDTO build() {
            return new BookingDTO(id, userId, showId, eventId, venueId, eventName, venueName, posterUrl,
                    showDateTime, seatIds, seatNumbers, numberOfSeats, totalAmount, bookingCode,
                    status, createdAt);
        }
    }
}
