import React, { useState, useEffect, useMemo } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { eventService } from '../services/api';
import PosterImage from '../components/PosterImage';
import '../styles/EventDetails.css';

function EventDetails() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [event, setEvent] = useState(null);
  const [shows, setShows] = useState([]);
  const [reviews, setReviews] = useState([]);
  const [showMoreReviews, setShowMoreReviews] = useState(false);
  const [loading, setLoading] = useState(true);
  const [selectedDate, setSelectedDate] = useState(null);
  const [reviewRating, setReviewRating] = useState(10);
  const [reviewComment, setReviewComment] = useState('');
  const [submittingReview, setSubmittingReview] = useState(false);

  useEffect(() => {
    const load = async () => {
      try {
        const [eventRes, showsRes, reviewsRes] = await Promise.all([
          eventService.getEventDetails(id),
          eventService.getShowsByEvent(id),
          eventService.getReviews(id),
        ]);
        if (eventRes.success) setEvent(eventRes.data);
        if (showsRes.success) setShows(showsRes.data || []);
        if (reviewsRes.success) setReviews(reviewsRes.data || []);
      } catch (e) {
        console.error(e);
      } finally {
        setLoading(false);
      }
    };
    load();
  }, [id]);

  const next7Days = useMemo(() => {
    const d = new Date();
    return Array.from({ length: 7 }, (_, i) => {
      const x = new Date(d);
      x.setDate(d.getDate() + i);
      return x;
    });
  }, []);

  const defaultDate = next7Days[0];
  const activeDate = selectedDate || (defaultDate && defaultDate.toISOString().slice(0, 10));

  const showsOnDate = useMemo(() => {
    if (!activeDate) return shows;
    const selectedCity = localStorage.getItem('lastSelectedCity') || 'Mumbai';
    return shows.filter((s) => {
      const d = new Date(s.showDateTime);
      const matchesDate = d.toISOString().slice(0, 10) === activeDate;
      const matchesCity = !s.venueCity || s.venueCity === selectedCity;
      return matchesDate && matchesCity;
    });
  }, [shows, activeDate]);

  const groupedByVenue = useMemo(() => {
    const map = {};
    showsOnDate.forEach((s) => {
      const key = s.venueId;
      if (!map[key]) map[key] = { venueId: key, venueName: s.venueName || `Venue #${s.venueId}`, shows: [] };
      map[key].shows.push(s);
    });
    return Object.values(map);
  }, [showsOnDate]);

  const formatTime = (dateTime) => {
    return new Date(dateTime).toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit', hour12: true });
  };

  const formatDateLabel = (d) => {
    return d.toLocaleDateString('en-US', { weekday: 'short', month: 'short', day: 'numeric' });
  };

  const getShowStatusClass = (status) => {
    if (!status) return '';
    const s = status.toLowerCase();
    if (s === 'housefull') return 'housefull';
    if (s === 'fast_filling') return 'fast_filling';
    if (s === 'available') return 'available';
    return '';
  };

  const durationStr = event?.durationMinutes
    ? `${Math.floor(event.durationMinutes / 60)}h ${event.durationMinutes % 60}m`
    : '';

  const trailerEmbedUrl = (url) => {
    if (!url) return null;
    const m = url.match(/(?:youtube\.com\/watch\?v=|youtu\.be\/)([a-zA-Z0-9_-]+)/);
    return m ? `https://www.youtube.com/embed/${m[1]}` : null;
  };

  const handleSubmitReview = async (e) => {
    e.preventDefault();
    if (!reviewRating || reviewRating < 1 || reviewRating > 10) {
      alert('Please select a rating between 1 and 10.');
      return;
    }
    if (!reviewComment.trim()) {
      alert('Please enter a short comment for your review.');
      return;
    }
    try {
      setSubmittingReview(true);
      const res = await eventService.addReview(id, Number(reviewRating), reviewComment.trim());
      if (res.success && res.data) {
        setReviews((prev) => [res.data, ...prev]);
        setReviewComment('');
        setReviewRating(10);
        setShowMoreReviews(false);
      } else {
        alert(res.message || 'Failed to submit review.');
      }
    } catch (err) {
      console.error('Failed to submit review', err);
      alert('Failed to submit review. Please try again.');
    } finally {
      setSubmittingReview(false);
    }
  };

  if (loading || !event) {
    return <div className="loading">Loading...</div>;
  }

  return (
    <div className="event-details">
      <button type="button" className="back-btn" onClick={() => navigate('/')}>← Back to Home</button>

      <div className="hero-section">
        <div className="hero-content">
          <div className="hero-poster-wrap">
            <PosterImage
              src={event.posterUrl}
              alt={event.title}
              placeholderLetter={event.title ? event.title.charAt(0) : '?'}
              variant="hero"
              className="hero-poster"
            />
          </div>
          <div className="hero-info">
            <h1>{event.title}</h1>
            <div className="meta">
              {event.rating != null && <span className="rating">⭐ {event.rating}/10</span>}
              {durationStr && <span>{durationStr}</span>}
              {event.genre && <span>{event.genre}</span>}
              {event.language && <span>{event.language}</span>}
              {event.releaseDate && (
                <span>Release: {new Date(event.releaseDate).toLocaleDateString()}</span>
              )}
            </div>
            {event.description && <p className="description">{event.description}</p>}
            {event.cast && (
              <div className="cast"><strong>Cast:</strong> {event.cast}</div>
            )}
            {event.crew && (
              <div className="crew"><strong>Crew:</strong> {event.crew}</div>
            )}
          </div>
        </div>
      </div>

      {event.trailerUrl && trailerEmbedUrl(event.trailerUrl) && (
        <div className="trailer-section">
          <h2>Trailer</h2>
          <div className="trailer-wrap">
            <iframe
              title="Trailer"
              src={trailerEmbedUrl(event.trailerUrl)}
              frameBorder="0"
              allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
              allowFullScreen
            />
          </div>
        </div>
      )}

      <div className="reviews-section">
        <h2>User Reviews</h2>

        {reviews.length > 0 ? (
          <>
            <div className="reviews-list">
              {(showMoreReviews ? reviews : reviews.slice(0, 3)).map((r) => (
                <div key={r.id} className="review-card">
                  <div className="review-rating">⭐ {r.rating}/10</div>
                  <p className="review-comment">{r.comment}</p>
                  <span className="review-user">— {r.userName}</span>
                </div>
              ))}
            </div>
            {reviews.length > 3 && !showMoreReviews && (
              <button type="button" className="load-more-reviews" onClick={() => setShowMoreReviews(true)}>
                Load More
              </button>
            )}
          </>
        ) : (
          <p className="no-reviews-text">No reviews yet. Be the first to review this movie.</p>
        )}

        <form className="review-form" onSubmit={handleSubmitReview}>
          <div className="review-form-row">
            <label>
              Your Rating:
              <select
                value={reviewRating}
                onChange={(e) => setReviewRating(Number(e.target.value))}
              >
                {Array.from({ length: 10 }, (_, i) => 10 - i).map((val) => (
                  <option key={val} value={val}>{val}/10</option>
                ))}
              </select>
            </label>
          </div>
          <div className="review-form-row">
            <label>
              Your Review:
              <textarea
                value={reviewComment}
                onChange={(e) => setReviewComment(e.target.value)}
                rows={3}
                placeholder="Share your experience (min 1 character)..."
              />
            </label>
          </div>
          <button type="submit" className="submit-review-btn" disabled={submittingReview}>
            {submittingReview ? 'Submitting...' : 'Submit Review'}
          </button>
        </form>
      </div>

      <div className="shows-section">
        <h2>Book Tickets</h2>
        <div className="date-picker">
          {next7Days.map((d) => {
            const dateStr = d.toISOString().slice(0, 10);
            const isActive = dateStr === activeDate;
            return (
              <button
                key={dateStr}
                type="button"
                className={`date-btn ${isActive ? 'active' : ''}`}
                onClick={() => setSelectedDate(dateStr)}
              >
                <span className="date-day">{d.getDate()}</span>
                <span className="date-label">{formatDateLabel(d)}</span>
              </button>
            );
          })}
        </div>

        {groupedByVenue.length === 0 ? (
          <p className="no-shows">No shows on this date.</p>
        ) : (
          groupedByVenue.map((group) => (
            <div key={group.venueId || group.venueName} className="venue-group">
              <h3 className="venue-name">{group.venueName}</h3>
              <div className="show-times">
                {group.shows.map((show) => (
                  <button
                    key={show.id}
                    type="button"
                    className={`show-btn ${getShowStatusClass(show.status)}`}
                    onClick={() => navigate(`/shows/${show.id}/seats`)}
                    disabled={show.status === 'HOUSEFULL'}
                  >
                    <div className="show-time">{formatTime(show.showDateTime)}</div>
                    <div className="show-price">₹{show.priceStandard} - ₹{show.priceVip}</div>
                    <div className="show-seats">{show.availableSeats} seats</div>
                  </button>
                ))}
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
}

export default EventDetails;
