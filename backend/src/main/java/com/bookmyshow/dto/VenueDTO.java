package com.bookmyshow.dto;

public class VenueDTO {
    private Long id;
    private String name;
    private String city;
    private String address;
    private Integer totalRows;
    private Integer seatsPerRow;
    private String amenities;

    public VenueDTO() {}

    public VenueDTO(Long id, String name, String city, String address, Integer totalRows, Integer seatsPerRow, String amenities) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.address = address;
        this.totalRows = totalRows;
        this.seatsPerRow = seatsPerRow;
        this.amenities = amenities;
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

    public static VenueDTOBuilder builder() {
        return new VenueDTOBuilder();
    }

    public static class VenueDTOBuilder {
        private Long id;
        private String name;
        private String city;
        private String address;
        private Integer totalRows;
        private Integer seatsPerRow;
        private String amenities;

        public VenueDTOBuilder id(Long id) { this.id = id; return this; }
        public VenueDTOBuilder name(String name) { this.name = name; return this; }
        public VenueDTOBuilder city(String city) { this.city = city; return this; }
        public VenueDTOBuilder address(String address) { this.address = address; return this; }
        public VenueDTOBuilder totalRows(Integer totalRows) { this.totalRows = totalRows; return this; }
        public VenueDTOBuilder seatsPerRow(Integer seatsPerRow) { this.seatsPerRow = seatsPerRow; return this; }
        public VenueDTOBuilder amenities(String amenities) { this.amenities = amenities; return this; }

        public VenueDTO build() {
            return new VenueDTO(id, name, city, address, totalRows, seatsPerRow, amenities);
        }
    }
}
