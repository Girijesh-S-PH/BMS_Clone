package com.bookmyshow.dto;

public class SeatDTO {
    private Long id;
    private String seatNumber;
    private Integer rowNumber;
    private Integer columnNumber;
    private String category;
    private String status;
    private Long lockedByUserId;

    public SeatDTO() {
    }

    public SeatDTO(Long id, String seatNumber, Integer rowNumber, Integer columnNumber,
                   String category, String status, Long lockedByUserId) {
        this.id = id;
        this.seatNumber = seatNumber;
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
        this.category = category;
        this.status = status;
        this.lockedByUserId = lockedByUserId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    public Integer getColumnNumber() {
        return columnNumber;
    }

    public void setColumnNumber(Integer columnNumber) {
        this.columnNumber = columnNumber;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getLockedByUserId() {
        return lockedByUserId;
    }

    public void setLockedByUserId(Long lockedByUserId) {
        this.lockedByUserId = lockedByUserId;
    }

    public static SeatDTOBuilder builder() {
        return new SeatDTOBuilder();
    }

    public static class SeatDTOBuilder {
        private Long id;
        private String seatNumber;
        private Integer rowNumber;
        private Integer columnNumber;
        private String category;
        private String status;
        private Long lockedByUserId;

        public SeatDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public SeatDTOBuilder seatNumber(String seatNumber) {
            this.seatNumber = seatNumber;
            return this;
        }

        public SeatDTOBuilder rowNumber(Integer rowNumber) {
            this.rowNumber = rowNumber;
            return this;
        }

        public SeatDTOBuilder columnNumber(Integer columnNumber) {
            this.columnNumber = columnNumber;
            return this;
        }

        public SeatDTOBuilder category(String category) {
            this.category = category;
            return this;
        }

        public SeatDTOBuilder status(String status) {
            this.status = status;
            return this;
        }

        public SeatDTOBuilder lockedByUserId(Long lockedByUserId) {
            this.lockedByUserId = lockedByUserId;
            return this;
        }

        public SeatDTO build() {
            return new SeatDTO(id, seatNumber, rowNumber, columnNumber, category, status, lockedByUserId);
        }
    }
}
