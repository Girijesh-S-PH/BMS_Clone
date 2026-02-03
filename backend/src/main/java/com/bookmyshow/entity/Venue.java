package com.bookmyshow.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "venues")
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Integer totalRows;

    @Column(nullable = false)
    private Integer seatsPerRow;

    @Column(length = 500)
    private String amenities;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Venue() {}

    public Venue(Long id, String name, String city, String address, Integer totalRows, Integer seatsPerRow, String amenities, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.address = address;
        this.totalRows = totalRows;
        this.seatsPerRow = seatsPerRow;
        this.amenities = amenities;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Integer getTotalRows() { return totalRows; }
    public void setTotalRows(Integer totalRows) { this.totalRows = totalRows; }

    public Integer getSeatsPerRow() { return seatsPerRow; }
    public void setSeatsPerRow(Integer seatsPerRow) { this.seatsPerRow = seatsPerRow; }

    public String getAmenities() { return amenities; }
    public void setAmenities(String amenities) { this.amenities = amenities; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static VenueBuilder builder() {
        return new VenueBuilder();
    }

    public static class VenueBuilder {
        private Long id;
        private String name;
        private String city;
        private String address;
        private Integer totalRows;
        private Integer seatsPerRow;
        private String amenities;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public VenueBuilder id(Long id) { this.id = id; return this; }
        public VenueBuilder name(String name) { this.name = name; return this; }
        public VenueBuilder city(String city) { this.city = city; return this; }
        public VenueBuilder address(String address) { this.address = address; return this; }
        public VenueBuilder totalRows(Integer totalRows) { this.totalRows = totalRows; return this; }
        public VenueBuilder seatsPerRow(Integer seatsPerRow) { this.seatsPerRow = seatsPerRow; return this; }
        public VenueBuilder amenities(String amenities) { this.amenities = amenities; return this; }
        public VenueBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public VenueBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Venue build() {
            Venue venue = new Venue();
            venue.id = this.id;
            venue.name = this.name;
            venue.city = this.city;
            venue.address = this.address;
            venue.totalRows = this.totalRows;
            venue.seatsPerRow = this.seatsPerRow;
            venue.amenities = this.amenities;
            venue.createdAt = this.createdAt != null ? this.createdAt : LocalDateTime.now();
            venue.updatedAt = this.updatedAt != null ? this.updatedAt : LocalDateTime.now();
            return venue;
        }
    }
}
