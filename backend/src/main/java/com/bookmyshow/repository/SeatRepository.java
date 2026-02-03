package com.bookmyshow.repository;

import com.bookmyshow.entity.Seat;
import com.bookmyshow.entity.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByShowId(Long showId);
    List<Seat> findByShowIdAndStatus(Long showId, SeatStatus status);
    Optional<Seat> findByShowIdAndSeatNumber(Long showId, String seatNumber);
    List<Seat> findByShowIdAndStatusIn(Long showId, List<SeatStatus> statuses);
    List<Seat> findByShowIdAndSeatNumberIn(Long showId, List<String> seatNumbers);
    long countByShowId(Long showId);
    List<Seat> findByShowIdAndLockedByUserId(Long showId, Long lockedByUserId);
}
