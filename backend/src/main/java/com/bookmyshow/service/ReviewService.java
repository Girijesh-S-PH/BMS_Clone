package com.bookmyshow.service;

import com.bookmyshow.dto.ReviewDTO;
import com.bookmyshow.entity.Event;
import com.bookmyshow.entity.Review;
import com.bookmyshow.entity.User;
import com.bookmyshow.exception.ResourceNotFoundException;
import com.bookmyshow.repository.EventRepository;
import com.bookmyshow.repository.ReviewRepository;
import com.bookmyshow.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public ReviewService(ReviewRepository reviewRepository,
                         EventRepository eventRepository,
                         UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public List<ReviewDTO> getReviewsByEventId(Long eventId) {
        return reviewRepository.findByEventIdOrderByCreatedAtDesc(eventId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReviewDTO addReview(Long eventId, Long userId, Integer rating, String comment) {
        if (rating == null || rating < 1 || rating > 10) {
            throw new IllegalArgumentException("Rating must be between 1 and 10");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Review review = Review.builder()
                .event(event)
                .user(user)
                .rating(rating)
                .comment(comment)
                .build();

        Review saved = reviewRepository.save(review);
        return toDTO(saved);
    }

    private ReviewDTO toDTO(Review r) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(r.getId());
        dto.setEventId(r.getEvent().getId());
        dto.setUserName(r.getUser() != null ? r.getUser().getFullName() : "User");
        dto.setRating(r.getRating());
        dto.setComment(r.getComment());
        dto.setCreatedAt(r.getCreatedAt());
        return dto;
    }
}
