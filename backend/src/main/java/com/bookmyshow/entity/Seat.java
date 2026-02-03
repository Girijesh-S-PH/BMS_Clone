package com.bookmyshow.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "seats")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "show_id", nullable = false)
    private Show show;

    @Column(nullable = false, length = 3)
    private String seatNumber; // Format: A1, A2, ...

    @Column(name = "seat_row", nullable = false)
    private Integer rowNumber;

    @Column(name = "seat_col", nullable = false)
    private Integer seatColumn;

    @Enumerated(EnumType.STRING)
    private SeatCategory category = SeatCategory.STANDARD;

    @Enumerated(EnumType.STRING)
    private SeatStatus status = SeatStatus.AVAILABLE;

    @Column(name = "locked_until")
    private LocalDateTime lockedUntil;

    @Column(name = "locked_by_user_id")
    private Long lockedByUserId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Seat() {
    }

    public Seat(Long id, Show show, String seatNumber, Integer rowNumber, Integer seatColumn,
                SeatCategory category, SeatStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.show = show;
        this.seatNumber = seatNumber;
        this.rowNumber = rowNumber;
        this.seatColumn = seatColumn;
        this.category = category;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
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

    public Integer getSeatColumn() {
        return seatColumn;
    }

    public void setSeatColumn(Integer seatColumn) {
        this.seatColumn = seatColumn;
    }

    public SeatCategory getCategory() {
        return category;
    }

    public void setCategory(SeatCategory category) {
        this.category = category;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }

    public LocalDateTime getLockedUntil() {
        return lockedUntil;
    }

    public void setLockedUntil(LocalDateTime lockedUntil) {
        this.lockedUntil = lockedUntil;
    }

    public Long getLockedByUserId() {
        return lockedByUserId;
    }

    public void setLockedByUserId(Long lockedByUserId) {
        this.lockedByUserId = lockedByUserId;
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

    public static SeatBuilder builder() {
        return new SeatBuilder();
    }

    public static class SeatBuilder {
        private Long id;
        private Show show;
        private String seatNumber;
        private Integer rowNumber;
        private Integer seatColumn;
        private SeatCategory category;
        private SeatStatus status;
        private LocalDateTime createdAt;

        public SeatBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public SeatBuilder show(Show show) {
            this.show = show;
            return this;
        }

        public SeatBuilder seatNumber(String seatNumber) {
            this.seatNumber = seatNumber;
            return this;
        }

        public SeatBuilder rowNumber(Integer rowNumber) {
            this.rowNumber = rowNumber;
            return this;
        }

        public SeatBuilder seatColumn(Integer seatColumn) {
            this.seatColumn = seatColumn;
            return this;
        }

        public SeatBuilder category(SeatCategory category) {
            this.category = category;
            return this;
        }

        public SeatBuilder status(SeatStatus status) {
            this.status = status;
            return this;
        }

        public SeatBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Seat build() {
            return new Seat(id, show, seatNumber, rowNumber, seatColumn, category, status, createdAt);
        }
    }
}
