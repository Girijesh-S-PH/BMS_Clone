package com.bookmyshow.repository;

import com.bookmyshow.entity.Booking;
import com.bookmyshow.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser_Id(Long userId);
    List<Booking> findByUser_IdAndStatus(Long userId, BookingStatus status);
    List<Booking> findByEventId(Long eventId);
    List<Booking> findByVenueId(Long venueId);
}
