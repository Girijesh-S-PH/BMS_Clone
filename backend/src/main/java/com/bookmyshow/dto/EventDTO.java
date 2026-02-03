package com.bookmyshow.dto;

import java.time.LocalDateTime;

public class EventDTO {
    private Long id;
    private String title;
    private String description;
    private String posterUrl;
    private String genre;
    private String language;
    private Integer durationMinutes;
    private Double rating;
    private Integer reviewCount;
    private String trailerUrl;
    private String cast;
    private String crew;
    private String status;
    private String eventType;
    private LocalDateTime releaseDate;

    public EventDTO() {}

    public EventDTO(Long id, String title, String description, String posterUrl, String genre, String language,
                   Integer durationMinutes, Double rating, Integer reviewCount, String trailerUrl, String cast,
                   String crew, String status, String eventType, LocalDateTime releaseDate) {
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
        this.cast = cast;
        this.crew = crew;
        this.status = status;
        this.eventType = eventType;
        this.releaseDate = releaseDate;
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

    public String getCast() { return cast; }
    public void setCast(String cast) { this.cast = cast; }

    public String getCrew() { return crew; }
    public void setCrew(String crew) { this.crew = crew; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public LocalDateTime getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDateTime releaseDate) { this.releaseDate = releaseDate; }

    // Builder pattern
    public static EventDTOBuilder builder() {
        return new EventDTOBuilder();
    }

    public static class EventDTOBuilder {
        private Long id;
        private String title;
        private String description;
        private String posterUrl;
        private String genre;
        private String language;
        private Integer durationMinutes;
        private Double rating;
        private Integer reviewCount;
        private String trailerUrl;
        private String cast;
        private String crew;
        private String status;
        private String eventType;
        private LocalDateTime releaseDate;

        public EventDTOBuilder id(Long id) { this.id = id; return this; }
        public EventDTOBuilder title(String title) { this.title = title; return this; }
        public EventDTOBuilder description(String description) { this.description = description; return this; }
        public EventDTOBuilder posterUrl(String posterUrl) { this.posterUrl = posterUrl; return this; }
        public EventDTOBuilder genre(String genre) { this.genre = genre; return this; }
        public EventDTOBuilder language(String language) { this.language = language; return this; }
        public EventDTOBuilder durationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; return this; }
        public EventDTOBuilder rating(Double rating) { this.rating = rating; return this; }
        public EventDTOBuilder reviewCount(Integer reviewCount) { this.reviewCount = reviewCount; return this; }
        public EventDTOBuilder trailerUrl(String trailerUrl) { this.trailerUrl = trailerUrl; return this; }
        public EventDTOBuilder cast(String cast) { this.cast = cast; return this; }
        public EventDTOBuilder crew(String crew) { this.crew = crew; return this; }
        public EventDTOBuilder status(String status) { this.status = status; return this; }
        public EventDTOBuilder eventType(String eventType) { this.eventType = eventType; return this; }
        public EventDTOBuilder releaseDate(LocalDateTime releaseDate) { this.releaseDate = releaseDate; return this; }

        public EventDTO build() {
            return new EventDTO(id, title, description, posterUrl, genre, language, durationMinutes, rating, reviewCount, trailerUrl, cast, crew, status, eventType, releaseDate);
        }
    }
}
