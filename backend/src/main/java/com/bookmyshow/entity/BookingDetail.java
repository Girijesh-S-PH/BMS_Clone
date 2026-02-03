package com.bookmyshow.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "booking_details")
public class BookingDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @Column(nullable = false)
    private String seatNumber;

    @Column(nullable = false)
    private String seatCategory;

    @Column(nullable = false)
    private Double price;

    public BookingDetail() {
    }

    public BookingDetail(Long id, Booking booking, String seatNumber, String seatCategory, Double price) {
        this.id = id;
        this.booking = booking;
        this.seatNumber = seatNumber;
        this.seatCategory = seatCategory;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getSeatCategory() {
        return seatCategory;
    }

    public void setSeatCategory(String seatCategory) {
        this.seatCategory = seatCategory;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public static BookingDetailBuilder builder() {
        return new BookingDetailBuilder();
    }

    public static class BookingDetailBuilder {
        private Long id;
        private Booking booking;
        private String seatNumber;
        private String seatCategory;
        private Double price;

        public BookingDetailBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public BookingDetailBuilder booking(Booking booking) {
            this.booking = booking;
            return this;
        }

        public BookingDetailBuilder seatNumber(String seatNumber) {
            this.seatNumber = seatNumber;
            return this;
        }

        public BookingDetailBuilder seatCategory(String seatCategory) {
            this.seatCategory = seatCategory;
            return this;
        }

        public BookingDetailBuilder price(Double price) {
            this.price = price;
            return this;
        }

        public BookingDetail build() {
            return new BookingDetail(id, booking, seatNumber, seatCategory, price);
        }
    }
}
