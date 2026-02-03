package com.bookmyshow.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "shows")
public class Show {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;

    @Column(nullable = false)
    private LocalDateTime showDateTime;

    @Column(nullable = false)
    private Double priceStandard = 150.0;

    @Column(nullable = false)
    private Double pricePremium = 250.0;

    @Column(nullable = false)
    private Double priceVip = 350.0;

    @Column(nullable = false)
    private Integer totalSeats = 0;

    @Column(nullable = false)
    private Integer availableSeats = 0;

    @Enumerated(EnumType.STRING)
    private ShowStatus status = ShowStatus.AVAILABLE;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public enum ShowStatus {
        AVAILABLE, FAST_FILLING, HOUSEFULL, CANCELLED
    }

    public Show() {}

    public Show(Long id, Event event, Venue venue, LocalDateTime showDateTime, Double priceStandard, Double pricePremium, Double priceVip, Integer totalSeats, Integer availableSeats, ShowStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.event = event;
        this.venue = venue;
        this.showDateTime = showDateTime;
        this.priceStandard = priceStandard;
        this.pricePremium = pricePremium;
        this.priceVip = priceVip;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }

    public Venue getVenue() { return venue; }
    public void setVenue(Venue venue) { this.venue = venue; }

    public LocalDateTime getShowDateTime() { return showDateTime; }
    public void setShowDateTime(LocalDateTime showDateTime) { this.showDateTime = showDateTime; }

    public Double getPriceStandard() { return priceStandard; }
    public void setPriceStandard(Double priceStandard) { this.priceStandard = priceStandard; }

    public Double getPricePremium() { return pricePremium; }
    public void setPricePremium(Double pricePremium) { this.pricePremium = pricePremium; }

    public Double getPriceVip() { return priceVip; }
    public void setPriceVip(Double priceVip) { this.priceVip = priceVip; }

    public Integer getTotalSeats() { return totalSeats; }
    public void setTotalSeats(Integer totalSeats) { this.totalSeats = totalSeats; }

    public Integer getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(Integer availableSeats) { this.availableSeats = availableSeats; }

    public ShowStatus getStatus() { return status; }
    public void setStatus(ShowStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static ShowBuilder builder() {
        return new ShowBuilder();
    }

    public static class ShowBuilder {
        private Long id;
        private Event event;
        private Venue venue;
        private LocalDateTime showDateTime;
        private Double priceStandard = 150.0;
        private Double pricePremium = 250.0;
        private Double priceVip = 350.0;
        private Integer totalSeats;
        private Integer availableSeats;
        private ShowStatus status = ShowStatus.AVAILABLE;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public ShowBuilder id(Long id) { this.id = id; return this; }
        public ShowBuilder event(Event event) { this.event = event; return this; }
        public ShowBuilder venue(Venue venue) { this.venue = venue; return this; }
        public ShowBuilder showDateTime(LocalDateTime showDateTime) { this.showDateTime = showDateTime; return this; }
        public ShowBuilder priceStandard(Double priceStandard) { this.priceStandard = priceStandard; return this; }
        public ShowBuilder pricePremium(Double pricePremium) { this.pricePremium = pricePremium; return this; }
        public ShowBuilder priceVip(Double priceVip) { this.priceVip = priceVip; return this; }
        public ShowBuilder totalSeats(Integer totalSeats) { this.totalSeats = totalSeats; return this; }
        public ShowBuilder availableSeats(Integer availableSeats) { this.availableSeats = availableSeats; return this; }
        public ShowBuilder status(ShowStatus status) { this.status = status; return this; }
        public ShowBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public ShowBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Show build() {
            Show show = new Show();
            show.id = this.id;
            show.event = this.event;
            show.venue = this.venue;
            show.showDateTime = this.showDateTime;
            show.priceStandard = this.priceStandard;
            show.pricePremium = this.pricePremium;
            show.priceVip = this.priceVip;
            show.totalSeats = this.totalSeats != null ? this.totalSeats : 0;
            show.availableSeats = this.availableSeats != null ? this.availableSeats : show.totalSeats;
            show.status = this.status;
            show.createdAt = this.createdAt != null ? this.createdAt : LocalDateTime.now();
            show.updatedAt = this.updatedAt != null ? this.updatedAt : LocalDateTime.now();
            return show;
        }
    }
}
