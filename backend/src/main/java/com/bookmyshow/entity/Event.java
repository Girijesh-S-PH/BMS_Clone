package com.bookmyshow.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(length = 500)
    private String posterUrl;

    @Column(nullable = false)
    private String genre;

    @Column(nullable = false)
    private String language;

    @Column(nullable = false)
    private Integer durationMinutes;

    @Column(nullable = false)
    private Double rating = 0.0;

    @Column(nullable = false)
    private Integer reviewCount = 0;

    @Column(length = 500)
    private String trailerUrl;

    @Column(length = 500)
    private String castList;

    @Column(length = 500)
    private String crew;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50)
    private EventStatus status = EventStatus.COMING_SOON;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", length = 50)
    private EventType eventType = EventType.MOVIE;

    @Column(nullable = false)
    private LocalDateTime releaseDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public enum EventStatus {
        COMING_SOON, NOW_SHOWING, ARCHIVED
    }

    public enum EventType {
        MOVIE, CONCERT, PLAY, SPORTS
    }

    public Event() {}

    public Event(Long id, String title, String description, String posterUrl, String genre, String language,
                 Integer durationMinutes, Double rating, Integer reviewCount, String trailerUrl, String cast,
                 String crew, EventStatus status, EventType eventType, LocalDateTime releaseDate,
                 LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.posterUrl = posterUrl;
        this.genre = genre;
        this.language = language;
        this.durationMinutes = durationMinutes;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.trailerUrl = trailerUrl;
        this.castList = cast;
        this.crew = crew;
        this.status = status;
        this.eventType = eventType;
        this.releaseDate = releaseDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }

    public Integer getReviewCount() { return reviewCount; }
    public void setReviewCount(Integer reviewCount) { this.reviewCount = reviewCount; }

    public String getTrailerUrl() { return trailerUrl; }
    public void setTrailerUrl(String trailerUrl) { this.trailerUrl = trailerUrl; }

    public String getCastList() { return castList; }
    public void setCastList(String castList) { this.castList = castList; }

    public String getCrew() { return crew; }
    public void setCrew(String crew) { this.crew = crew; }

    public EventStatus getStatus() { return status; }
    public void setStatus(EventStatus status) { this.status = status; }

    public EventType getEventType() { return eventType; }
    public void setEventType(EventType eventType) { this.eventType = eventType; }

    public LocalDateTime getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDateTime releaseDate) { this.releaseDate = releaseDate; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Builder pattern
    public static EventBuilder builder() {
        return new EventBuilder();
    }

    public static class EventBuilder {
        private Long id;
        private String title;
        private String description;
        private String posterUrl;
        private String genre;
        private String language;
        private Integer durationMinutes;
        private Double rating = 0.0;
        private Integer reviewCount = 0;
        private String trailerUrl;
        private String castList;
        private String crew;
        private EventStatus status = EventStatus.COMING_SOON;
        private EventType eventType = EventType.MOVIE;
        private LocalDateTime releaseDate;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public EventBuilder id(Long id) { this.id = id; return this; }
        public EventBuilder title(String title) { this.title = title; return this; }
        public EventBuilder description(String description) { this.description = description; return this; }
        public EventBuilder posterUrl(String posterUrl) { this.posterUrl = posterUrl; return this; }
        public EventBuilder genre(String genre) { this.genre = genre; return this; }
        public EventBuilder language(String language) { this.language = language; return this; }
        public EventBuilder durationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; return this; }
        public EventBuilder rating(Double rating) { this.rating = rating; return this; }
        public EventBuilder reviewCount(Integer reviewCount) { this.reviewCount = reviewCount; return this; }
        public EventBuilder trailerUrl(String trailerUrl) { this.trailerUrl = trailerUrl; return this; }
        public EventBuilder castList(String castList) { this.castList = castList; return this; }
        public EventBuilder crew(String crew) { this.crew = crew; return this; }
        public EventBuilder status(EventStatus status) { this.status = status; return this; }
        public EventBuilder eventType(EventType eventType) { this.eventType = eventType; return this; }
        public EventBuilder releaseDate(LocalDateTime releaseDate) { this.releaseDate = releaseDate; return this; }
        public EventBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public EventBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Event build() {
            Event event = new Event();
            event.id = this.id;
            event.title = this.title;
            event.description = this.description;
            event.posterUrl = this.posterUrl;
            event.genre = this.genre;
            event.language = this.language;
            event.durationMinutes = this.durationMinutes;
            event.rating = this.rating;
            event.reviewCount = this.reviewCount;
            event.trailerUrl = this.trailerUrl;
            event.castList = this.castList;
            event.crew = this.crew;
            event.status = this.status;
            event.eventType = this.eventType;
            event.releaseDate = this.releaseDate;
            event.createdAt = this.createdAt != null ? this.createdAt : LocalDateTime.now();
            event.updatedAt = this.updatedAt != null ? this.updatedAt : LocalDateTime.now();
            return event;
        }
    }
}
