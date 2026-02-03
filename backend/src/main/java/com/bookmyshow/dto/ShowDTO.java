package com.bookmyshow.dto;

import java.time.LocalDateTime;

public class ShowDTO {
    private Long id;
    private Long eventId;
    private String eventTitle;
    private Long venueId;
    private String venueName;
    private String venueCity;
    private LocalDateTime showDateTime;
    private Double priceStandard;
    private Double pricePremium;
    private Double priceVip;
    private Integer totalSeats;
    private Integer availableSeats;
    private String status;

    public ShowDTO() {}

    public ShowDTO(Long id, Long eventId, String eventTitle, Long venueId, String venueName, String venueCity, LocalDateTime showDateTime,
                   Double priceStandard, Double pricePremium, Double priceVip, Integer totalSeats, Integer availableSeats, String status) {
        this.id = id;
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.venueId = venueId;
        this.venueName = venueName;
        this.venueCity = venueCity;
        this.showDateTime = showDateTime;
        this.priceStandard = priceStandard;
        this.pricePremium = pricePremium;
        this.priceVip = priceVip;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }

    public String getEventTitle() { return eventTitle; }
    public void setEventTitle(String eventTitle) { this.eventTitle = eventTitle; }

    public Long getVenueId() { return venueId; }
    public void setVenueId(Long venueId) { this.venueId = venueId; }

    public String getVenueName() { return venueName; }
    public void setVenueName(String venueName) { this.venueName = venueName; }

    public String getVenueCity() { return venueCity; }
    public void setVenueCity(String venueCity) { this.venueCity = venueCity; }

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

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public static ShowDTOBuilder builder() {
        return new ShowDTOBuilder();
    }

    public static class ShowDTOBuilder {
        private Long id;
        private Long eventId;
        private String eventTitle;
        private Long venueId;
        private String venueName;
        private String venueCity;
        private LocalDateTime showDateTime;
        private Double priceStandard;
        private Double pricePremium;
        private Double priceVip;
        private Integer totalSeats;
        private Integer availableSeats;
        private String status;

        public ShowDTOBuilder id(Long id) { this.id = id; return this; }
        public ShowDTOBuilder eventId(Long eventId) { this.eventId = eventId; return this; }
        public ShowDTOBuilder eventTitle(String eventTitle) { this.eventTitle = eventTitle; return this; }
        public ShowDTOBuilder venueId(Long venueId) { this.venueId = venueId; return this; }
        public ShowDTOBuilder venueName(String venueName) { this.venueName = venueName; return this; }
        public ShowDTOBuilder venueCity(String venueCity) { this.venueCity = venueCity; return this; }
        public ShowDTOBuilder showDateTime(LocalDateTime showDateTime) { this.showDateTime = showDateTime; return this; }
        public ShowDTOBuilder priceStandard(Double priceStandard) { this.priceStandard = priceStandard; return this; }
        public ShowDTOBuilder pricePremium(Double pricePremium) { this.pricePremium = pricePremium; return this; }
        public ShowDTOBuilder priceVip(Double priceVip) { this.priceVip = priceVip; return this; }
        public ShowDTOBuilder totalSeats(Integer totalSeats) { this.totalSeats = totalSeats; return this; }
        public ShowDTOBuilder availableSeats(Integer availableSeats) { this.availableSeats = availableSeats; return this; }
        public ShowDTOBuilder status(String status) { this.status = status; return this; }

        public ShowDTO build() {
            return new ShowDTO(id, eventId, eventTitle, venueId, venueName, venueCity, showDateTime, priceStandard, pricePremium, priceVip, totalSeats, availableSeats, status);
        }
    }
}
