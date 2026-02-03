package com.bookmyshow.service;

import com.bookmyshow.dto.ShowDTO;
import com.bookmyshow.entity.*;
import com.bookmyshow.exception.ResourceNotFoundException;
import com.bookmyshow.repository.EventRepository;
import com.bookmyshow.repository.SeatRepository;
import com.bookmyshow.repository.ShowRepository;
import com.bookmyshow.repository.VenueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShowService {

    private final ShowRepository showRepository;
    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;
    private final SeatRepository seatRepository;

    public ShowService(ShowRepository showRepository, EventRepository eventRepository, VenueRepository venueRepository, SeatRepository seatRepository) {
        this.showRepository = showRepository;
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
        this.seatRepository = seatRepository;
    }

    @Transactional
    public ShowDTO createShow(ShowDTO showDTO) {
        Event event = eventRepository.findById(showDTO.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        Venue venue = venueRepository.findById(showDTO.getVenueId())
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found"));

        // Calculate total seats from venue dimensions
        int totalSeats = venue.getTotalRows() * venue.getSeatsPerRow();

        Show show = Show.builder()
                .event(event)
                .venue(venue)
                .showDateTime(showDTO.getShowDateTime())
                .priceStandard(showDTO.getPriceStandard())
                .pricePremium(showDTO.getPricePremium())
                .priceVip(showDTO.getPriceVip())
                .totalSeats(totalSeats)
                .availableSeats(totalSeats)
                .status(Show.ShowStatus.AVAILABLE)
                .build();

        Show savedShow = showRepository.save(show);
        
        // Create seats for the show based on venue layout
        System.out.println("[ShowService] Creating seats for show ID: " + savedShow.getId() + 
                          ", Venue: " + venue.getName() + 
                          ", Rows: " + venue.getTotalRows() + 
                          ", SeatsPerRow: " + venue.getSeatsPerRow());
        
        int seatsCreated = createSeatsForShow(savedShow, venue);
        
        System.out.println("[ShowService] Created " + seatsCreated + " seats for show ID: " + savedShow.getId());
        
        return convertToDTO(savedShow);
    }

    public ShowDTO updateShow(Long id, ShowDTO showDTO) {
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found"));

        if (showDTO.getPriceStandard() != null) {
            show.setPriceStandard(showDTO.getPriceStandard());
        }
        if (showDTO.getPricePremium() != null) {
            show.setPricePremium(showDTO.getPricePremium());
        }
        if (showDTO.getPriceVip() != null) {
            show.setPriceVip(showDTO.getPriceVip());
        }

        Show updatedShow = showRepository.save(show);
        return convertToDTO(updatedShow);
    }

    public ShowDTO getShowById(Long id) {
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found"));
        return convertToDTO(show);
    }

    public List<ShowDTO> getAllShows() {
        return showRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ShowDTO> getShowsByEventId(Long eventId) {
        return showRepository.findByEventId(eventId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ShowDTO> getShowsByVenueId(Long venueId) {
        return showRepository.findByVenueId(venueId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteShow(Long id) {
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found"));
        // Delete associated seats first (foreign key constraint)
        seatRepository.findByShowId(id).forEach(seatRepository::delete);
        showRepository.delete(show);
    }

    @Transactional
    public String generateSeatsForExistingShow(Long showId) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found"));
        
        // Check if seats already exist
        long existingSeats = seatRepository.countByShowId(showId);
        if (existingSeats > 0) {
            return "Show already has " + existingSeats + " seats. Delete them first if you want to regenerate.";
        }
        
        Venue venue = show.getVenue();
        int seatsCreated = createSeatsForShow(show, venue);
        
        return "Successfully created " + seatsCreated + " seats for show ID: " + showId;
    }

    public ShowDTO updateShowAvailability(Long id) {
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found"));

        int availableSeats = show.getAvailableSeats();
        if (availableSeats > (show.getTotalSeats() * 0.75)) {
            show.setStatus(Show.ShowStatus.AVAILABLE);
        } else if (availableSeats > (show.getTotalSeats() * 0.25)) {
            show.setStatus(Show.ShowStatus.FAST_FILLING);
        } else if (availableSeats > 0) {
            show.setStatus(Show.ShowStatus.FAST_FILLING);
        } else {
            show.setStatus(Show.ShowStatus.HOUSEFULL);
        }

        Show updatedShow = showRepository.save(show);
        return convertToDTO(updatedShow);
    }

    private int createSeatsForShow(Show show, Venue venue) {
        int rows = venue.getTotalRows();
        int seatsPerRow = venue.getSeatsPerRow();
        String[] rowLabels = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T"};
        
        int seatsCreated = 0;

        for (int row = 0; row < rows && row < rowLabels.length; row++) {
            for (int col = 1; col <= seatsPerRow; col++) {
                String seatNumber = rowLabels[row] + col;

                // Determine seat category based on row position
                SeatCategory category;
                if (row < rows * 0.33) {
                    category = SeatCategory.STANDARD;
                } else if (row < rows * 0.67) {
                    category = SeatCategory.PREMIUM;
                } else {
                    category = SeatCategory.VIP;
                }

                Seat seat = Seat.builder()
                        .show(show)
                        .seatNumber(seatNumber)
                        .rowNumber(row + 1)
                        .seatColumn(col)
                        .category(category)
                        .status(SeatStatus.AVAILABLE)
                        .createdAt(LocalDateTime.now())
                        .build();

                seatRepository.save(seat);
                seatsCreated++;
            }
        }
        
        return seatsCreated;
    }

    private ShowDTO convertToDTO(Show show) {
        return ShowDTO.builder()
                .id(show.getId())
                .eventId(show.getEvent().getId())
                .eventTitle(show.getEvent().getTitle())
                .venueId(show.getVenue().getId())
                .venueName(show.getVenue().getName())
                .venueCity(show.getVenue().getCity())
                .showDateTime(show.getShowDateTime())
                .priceStandard(show.getPriceStandard())
                .pricePremium(show.getPricePremium())
                .priceVip(show.getPriceVip())
                .totalSeats(show.getTotalSeats())
                .availableSeats(show.getAvailableSeats())
                .status(show.getStatus().toString())
                .build();
    }
}
