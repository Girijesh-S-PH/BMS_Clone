package com.bookmyshow.service;

import com.bookmyshow.dto.BookingDTO;
import com.bookmyshow.entity.*;
import com.bookmyshow.exception.BadRequestException;
import com.bookmyshow.exception.ResourceNotFoundException;
import com.bookmyshow.repository.BookingRepository;
import com.bookmyshow.repository.SeatRepository;
import com.bookmyshow.repository.ShowRepository;
import com.bookmyshow.repository.UserRepository;
import com.bookmyshow.util.CaptchaValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;
    private final ShowRepository showRepository;
    private final UserRepository userRepository;
    private final CaptchaValidator captchaValidator;

    public BookingService(BookingRepository bookingRepository, SeatRepository seatRepository,
                         ShowRepository showRepository, UserRepository userRepository,
                         CaptchaValidator captchaValidator) {
        this.bookingRepository = bookingRepository;
        this.seatRepository = seatRepository;
        this.showRepository = showRepository;
        this.userRepository = userRepository;
        this.captchaValidator = captchaValidator;
    }

    @Transactional
    public BookingDTO createBooking(BookingDTO bookingDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Show show = showRepository.findById(bookingDTO.getShowId())
                .orElseThrow(() -> new ResourceNotFoundException("Show not found"));

        List<Long> seatIds = bookingDTO.getSeatIds();
        if (seatIds == null || seatIds.isEmpty()) {
            throw new BadRequestException("No seats selected");
        }

        if (seatIds.size() > 10) {
            throw new BadRequestException("Maximum 10 seats allowed per booking");
        }

        List<Seat> seats = seatRepository.findAllById(seatIds);
        if (seats.size() != seatIds.size()) {
            throw new BadRequestException("Some seats not found");
        }

        LocalDateTime now = LocalDateTime.now();

        for (Seat seat : seats) {
            if (seat.getStatus() == SeatStatus.BOOKED) {
                throw new BadRequestException("Seat " + seat.getSeatNumber() + " is not available");
            }

            if (seat.getStatus() == SeatStatus.BLOCKED) {
                LocalDateTime lockedUntil = seat.getLockedUntil();
                // If locked by someone else and not yet expired
                if (lockedUntil != null && lockedUntil.isAfter(now)
                        && (seat.getLockedByUserId() == null || !seat.getLockedByUserId().equals(userId))) {
                    throw new BadRequestException("Seat " + seat.getSeatNumber() + " is currently locked");
                }
            }
            if (!seat.getShow().getId().equals(show.getId())) {
                throw new BadRequestException("Seat does not belong to this show");
            }
        }

        for (Seat seat : seats) {
            seat.setStatus(SeatStatus.BOOKED);
            seat.setLockedUntil(null);
            seat.setLockedByUserId(null);
            seat.setUpdatedAt(now);
            seatRepository.save(seat);
        }

        show.setAvailableSeats(show.getAvailableSeats() - seats.size());
        showRepository.save(show);

        String seatNumbers = seats.stream()
                .map(Seat::getSeatNumber)
                .collect(Collectors.joining(", "));

        double totalAmount = seats.stream()
                .mapToDouble(seat -> {
                    switch (seat.getCategory()) {
                        case STANDARD:
                            return show.getPriceStandard();
                        case PREMIUM:
                            return show.getPricePremium();
                        case VIP:
                            return show.getPriceVip();
                        default:
                            return show.getPriceStandard();
                    }
                })
                .sum();

        totalAmount += 50.0; // Convenience fee

        String code = captchaValidator.generateBookingCode();
        Booking booking = Booking.builder()
                .user(user)
                .event(show.getEvent())
                .venue(show.getVenue())
                .show(show)
                .bookingCode(code)
                .totalAmount(totalAmount)
                .numberOfSeats(seats.size())
                .seatNumbers(seatNumbers)
                .status(BookingStatus.CONFIRMED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        booking = bookingRepository.save(booking);
        return convertToDTO(booking);
    }

    public List<BookingDTO> getUserBookings(Long userId) {
        List<Booking> bookings = bookingRepository.findByUser_Id(userId);
        return bookings.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public BookingDTO getBookingById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!booking.getUser().getId().equals(userId)) {
            throw new BadRequestException("Unauthorized access to booking");
        }

        return convertToDTO(booking);
    }

    @Transactional
    public void cancelBooking(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        if (!booking.getUser().getId().equals(userId)) {
            throw new BadRequestException("Unauthorized access to booking");
        }
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BadRequestException("Booking already cancelled");
        }
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setUpdatedAt(LocalDateTime.now());
        bookingRepository.save(booking);
        // Release seats so they can be booked again
        Show show = booking.getShow();
        if (show != null && booking.getSeatNumbers() != null) {
            List<String> numbers = Arrays.asList(booking.getSeatNumbers().split(",\\s*"));
            List<Seat> seats = seatRepository.findByShowIdAndSeatNumberIn(show.getId(), numbers);
            for (Seat s : seats) {
                s.setStatus(SeatStatus.AVAILABLE);
                s.setUpdatedAt(LocalDateTime.now());
                seatRepository.save(s);
            }
            show.setAvailableSeats(show.getAvailableSeats() + seats.size());
            showRepository.save(show);
        }
    }

    private BookingDTO convertToDTO(Booking booking) {
        Show show = booking.getShow();
        return BookingDTO.builder()
                .id(booking.getId())
                .userId(booking.getUser().getId())
                .showId(show != null ? show.getId() : null)
                .eventId(booking.getEvent().getId())
                .venueId(booking.getVenue().getId())
                .eventName(booking.getEvent().getTitle())
                .venueName(booking.getVenue().getName())
                .posterUrl(booking.getEvent().getPosterUrl())
                .showDateTime(show != null ? show.getShowDateTime() : null)
                .seatNumbers(booking.getSeatNumbers())
                .numberOfSeats(booking.getNumberOfSeats())
                .totalAmount(booking.getTotalAmount())
                .bookingCode(booking.getBookingCode())
                .status(booking.getStatus().toString())
                .createdAt(booking.getCreatedAt())
                .build();
    }
}
