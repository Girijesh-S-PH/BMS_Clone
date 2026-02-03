package com.bookmyshow.service;

import com.bookmyshow.dto.SeatDTO;
import com.bookmyshow.entity.Seat;
import com.bookmyshow.entity.SeatStatus;
import com.bookmyshow.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeatService {

    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public List<SeatDTO> getSeatsByShowId(Long showId) {
        releaseExpiredLocks(showId);
        return seatRepository.findByShowId(showId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SeatDTO> getAvailableSeatsByShowId(Long showId) {
        return seatRepository.findByShowIdAndStatus(showId, SeatStatus.AVAILABLE).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SeatDTO> lockSeats(Long showId, List<Long> seatIds, Long userId, int lockMinutes) {
        if (seatIds == null || seatIds.isEmpty()) {
            return getSeatsByShowId(showId);
        }

        releaseExpiredLocks(showId);

        // Release any existing locks for this user on this show
        if (userId != null) {
            List<Seat> previouslyLocked = seatRepository.findByShowIdAndLockedByUserId(showId, userId);
            LocalDateTime now = LocalDateTime.now();
            for (Seat seat : previouslyLocked) {
                if (seat.getLockedUntil() == null || seat.getLockedUntil().isAfter(now)) {
                    seat.setStatus(SeatStatus.AVAILABLE);
                    seat.setLockedUntil(null);
                    seat.setLockedByUserId(null);
                    seat.setUpdatedAt(now);
                    seatRepository.save(seat);
                }
            }
        }

        LocalDateTime lockUntil = LocalDateTime.now().plusMinutes(lockMinutes);
        List<Seat> seatsToLock = new ArrayList<>(seatRepository.findAllById(seatIds));

        for (Seat seat : seatsToLock) {
            if (!seat.getShow().getId().equals(showId)) {
                continue;
            }
            // If already booked, do not allow locking
            if (seat.getStatus() == SeatStatus.BOOKED) {
                continue;
            }

            // If blocked by someone else and not expired, skip
            if (seat.getStatus() == SeatStatus.BLOCKED) {
                LocalDateTime lockedUntil = seat.getLockedUntil();
                if (lockedUntil != null && lockedUntil.isAfter(LocalDateTime.now())
                        && (seat.getLockedByUserId() == null || !seat.getLockedByUserId().equals(userId))) {
                    continue;
                }
            }

            seat.setStatus(SeatStatus.BLOCKED);
            seat.setLockedUntil(lockUntil);
            seat.setLockedByUserId(userId);
            seat.setUpdatedAt(LocalDateTime.now());
            seatRepository.save(seat);
        }

        return getSeatsByShowId(showId);
    }

    public List<SeatDTO> releaseSeats(Long showId, List<Long> seatIds, Long userId) {
        if (seatIds == null || seatIds.isEmpty()) {
            return getSeatsByShowId(showId);
        }

        List<Seat> seatsToRelease = seatRepository.findAllById(seatIds);
        LocalDateTime now = LocalDateTime.now();
        for (Seat seat : seatsToRelease) {
            if (!seat.getShow().getId().equals(showId)) {
                continue;
            }
            if (seat.getStatus() == SeatStatus.BLOCKED &&
                    (userId == null || seat.getLockedByUserId() == null || seat.getLockedByUserId().equals(userId))) {
                seat.setStatus(SeatStatus.AVAILABLE);
                seat.setLockedUntil(null);
                seat.setLockedByUserId(null);
                seat.setUpdatedAt(now);
                seatRepository.save(seat);
            }
        }

        return getSeatsByShowId(showId);
    }

    public void releaseExpiredLocks(Long showId) {
        LocalDateTime now = LocalDateTime.now();
        List<Seat> blockedSeats = seatRepository.findByShowIdAndStatus(showId, SeatStatus.BLOCKED);
        for (Seat seat : blockedSeats) {
            if (seat.getLockedUntil() != null && seat.getLockedUntil().isBefore(now)) {
                seat.setStatus(SeatStatus.AVAILABLE);
                seat.setLockedUntil(null);
                seat.setLockedByUserId(null);
                seat.setUpdatedAt(now);
                seatRepository.save(seat);
            }
        }
    }

    private SeatDTO convertToDTO(Seat seat) {
        return SeatDTO.builder()
                .id(seat.getId())
                .seatNumber(seat.getSeatNumber())
                .rowNumber(seat.getRowNumber())
                .columnNumber(seat.getSeatColumn())
                .category(seat.getCategory().toString())
                .status(seat.getStatus().toString())
                .lockedByUserId(seat.getLockedByUserId())
                .build();
    }
}
