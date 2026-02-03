package com.bookmyshow.repository;

import com.bookmyshow.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {
    List<Venue> findByCity(String city);
    List<Venue> findByCityAndNameContainingIgnoreCase(String city, String name);
}
