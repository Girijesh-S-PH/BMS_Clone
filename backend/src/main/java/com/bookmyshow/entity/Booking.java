package com.bookmyshow.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "venue_id")
    private Venue venue;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "show_id")
    private Show show;

    @Column(name = "booking_code", unique = true, length = 8)
    private String bookingCode;

    @Column(nullable = false)
    private Double totalAmount;

    @Column(nullable = false)
    private Integer numberOfSeats;

    @Column(nullable = false)
    private String seatNumbers;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Booking() {
    }

    public Booking(Long id, User user, Event event, Venue venue, Show show, String bookingCode,
                   Double totalAmount, Integer numberOfSeats, String seatNumbers, BookingStatus status,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.event = event;
        this.venue = venue;
        this.show = show;
        this.bookingCode = bookingCode;
        this.totalAmount = totalAmount;
        this.numberOfSeats = numberOfSeats;
        this.seatNumbers = seatNumbers;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(Integer numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public String getSeatNumbers() {
        return seatNumbers;
    }

    public void setSeatNumbers(String seatNumbers) {
        this.seatNumbers = seatNumbers;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static BookingBuilder builder() {
        return new BookingBuilder();
    }

    public static class BookingBuilder {
        private Long id;
        private User user;
        private Event event;
        private Venue venue;
        private Show show;
        private String bookingCode;
        private Double totalAmount;
        private Integer numberOfSeats;
        private String seatNumbers;
        private BookingStatus status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public BookingBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public BookingBuilder user(User user) {
            this.user = user;
            return this;
        }

        public BookingBuilder event(Event event) {
            this.event = event;
            return this;
        }

        public BookingBuilder venue(Venue venue) {
            this.venue = venue;
            return this;
        }

        public BookingBuilder show(Show show) {
            this.show = show;
            return this;
        }

        public BookingBuilder bookingCode(String bookingCode) {
            this.bookingCode = bookingCode;
            return this;
        }

        public BookingBuilder totalAmount(Double totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public BookingBuilder numberOfSeats(Integer numberOfSeats) {
            this.numberOfSeats = numberOfSeats;
            return this;
        }

        public BookingBuilder seatNumbers(String seatNumbers) {
            this.seatNumbers = seatNumbers;
            return this;
        }

        public BookingBuilder status(BookingStatus status) {
            this.status = status;
            return this;
        }

        public BookingBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public BookingBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Booking build() {
            return new Booking(id, user, event, venue, show, bookingCode, totalAmount, numberOfSeats,
                    seatNumbers, status, createdAt, updatedAt);
        }
    }
}
