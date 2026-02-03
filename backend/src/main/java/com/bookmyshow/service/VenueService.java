package com.bookmyshow.service;

import com.bookmyshow.dto.VenueDTO;
import com.bookmyshow.entity.Venue;
import com.bookmyshow.entity.Show;
import com.bookmyshow.entity.Seat;
import com.bookmyshow.entity.Booking;
import com.bookmyshow.exception.ResourceNotFoundException;
import com.bookmyshow.repository.VenueRepository;
import com.bookmyshow.repository.ShowRepository;
import com.bookmyshow.repository.SeatRepository;
import com.bookmyshow.repository.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VenueService {

    private final VenueRepository venueRepository;
    private final ShowRepository showRepository;
    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;

    public VenueService(VenueRepository venueRepository,
                        ShowRepository showRepository,
                        SeatRepository seatRepository,
                        BookingRepository bookingRepository) {
        this.venueRepository = venueRepository;
        this.showRepository = showRepository;
        this.seatRepository = seatRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<VenueDTO> getAllVenues() {
        return venueRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<VenueDTO> getVenuesByCity(String city) {
        return venueRepository.findByCity(city).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public VenueDTO getVenueById(Long venueId) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found with id: " + venueId));
        return convertToDTO(venue);
    }

    public VenueDTO createVenue(VenueDTO venueDTO) {
        Venue venue = Venue.builder()
                .name(venueDTO.getName())
                .city(venueDTO.getCity())
                .address(venueDTO.getAddress())
                .totalRows(venueDTO.getTotalRows())
                .seatsPerRow(venueDTO.getSeatsPerRow())
                .amenities(venueDTO.getAmenities())
                .build();
        
        venue = venueRepository.save(venue);
        return convertToDTO(venue);
    }

    public VenueDTO updateVenue(Long venueId, VenueDTO venueDTO) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found with id: " + venueId));
        
        venue.setName(venueDTO.getName());
        venue.setCity(venueDTO.getCity());
        venue.setAddress(venueDTO.getAddress());
        venue.setTotalRows(venueDTO.getTotalRows());
        venue.setSeatsPerRow(venueDTO.getSeatsPerRow());
        venue.setAmenities(venueDTO.getAmenities());
        
        venue = venueRepository.save(venue);
        return convertToDTO(venue);
    }

    @Transactional
    public void deleteVenue(Long venueId) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found with id: " + venueId));

        // Delete all bookings linked to this venue
        for (Booking booking : bookingRepository.findByVenueId(venueId)) {
            bookingRepository.delete(booking);
        }

        // Delete all shows (and their seats) linked to this venue
        for (Show show : showRepository.findByVenueId(venueId)) {
            Long showId = show.getId();
            for (Seat seat : seatRepository.findByShowId(showId)) {
                seatRepository.delete(seat);
            }
            showRepository.delete(show);
        }

        venueRepository.delete(venue);
    }

    private VenueDTO convertToDTO(Venue venue) {
        return VenueDTO.builder()
                .id(venue.getId())
                .name(venue.getName())
                .city(venue.getCity())
                .address(venue.getAddress())
                .totalRows(venue.getTotalRows())
                .seatsPerRow(venue.getSeatsPerRow())
                .amenities(venue.getAmenities())
                .build();
    }
}
