import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { bookingService } from '../services/api';
import PosterImage from '../components/PosterImage';
import '../styles/MyBookings.css';

const TABS = [
  { key: 'upcoming', label: 'Upcoming' },
  { key: 'completed', label: 'Completed' },
  { key: 'cancelled', label: 'Cancelled' },
];

function MyBookings() {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [tab, setTab] = useState('upcoming');
  const [cancellingId, setCancellingId] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const load = async () => {
      const userId = localStorage.getItem('userId');
      if (!userId) {
        navigate('/login');
        return;
      }
      try {
        const data = await bookingService.getBookings();
        if (data.success) setBookings(data.data || []);
      } catch (e) {
        console.error(e);
      } finally {
        setLoading(false);
      }
    };
    load();
  }, [navigate]);

  const now = new Date();
  const upcoming = bookings.filter((b) => b.status === 'CONFIRMED' && b.showDateTime && new Date(b.showDateTime) >= now);
  const completed = bookings.filter((b) => b.status === 'CONFIRMED' && b.showDateTime && new Date(b.showDateTime) < now);
  const cancelled = bookings.filter((b) => b.status === 'CANCELLED');

  const list = tab === 'upcoming' ? upcoming : tab === 'completed' ? completed : cancelled;

  const formatDate = (dateTime) => {
    if (!dateTime) return 'N/A';
    return new Date(dateTime).toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' });
  };

  const formatTime = (dateTime) => {
    if (!dateTime) return '';
    return new Date(dateTime).toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit', hour12: true });
  };

  const handleCancel = async (bookingId) => {
    if (!window.confirm('Cancel this booking? This cannot be undone.')) return;
    setCancellingId(bookingId);
    try {
      const data = await bookingService.cancelBooking(bookingId);
      if (data.success) {
        setBookings((prev) =>
          prev.map((b) => (b.id === bookingId ? { ...b, status: 'CANCELLED' } : b))
        );
      } else {
        alert(data.message || 'Failed to cancel');
      }
    } catch (e) {
      alert('Failed to cancel booking');
    } finally {
      setCancellingId(null);
    }
  };

  if (loading) {
    return <div className="loading">Loading bookings...</div>;
  }

  return (
    <div className="my-bookings">
      <div className="header">
        <button type="button" className="back-btn" onClick={() => navigate('/')}>
          ← Back to Home
        </button>
        <h1>My Bookings</h1>
      </div>

      <div className="tabs">
        {TABS.map(({ key, label }) => (
          <button
            key={key}
            type="button"
            className={`tab-btn ${tab === key ? 'active' : ''}`}
            onClick={() => setTab(key)}
          >
            {label}
          </button>
        ))}
      </div>

      <div className="bookings-container">
        {list.length === 0 ? (
          <div className="no-bookings">
            <p>No {tab} bookings.</p>
            <button type="button" onClick={() => navigate('/')}>Browse Events</button>
          </div>
        ) : (
          <div className="bookings-list">
            {list.map((booking) => (
              <div key={booking.id} className="booking-card">
                <div className="booking-card-inner">
                  <div className="booking-poster">
                    <PosterImage
                      src={booking.posterUrl}
                      alt={booking.eventName}
                      placeholderLetter={booking.eventName ? booking.eventName.charAt(0) : '?'}
                      variant="booking"
                    />
                  </div>
                  <div className="booking-content">
                <div className="booking-header">
                  <h3>{booking.eventName}</h3>
                  <span className={`status-badge ${booking.status.toLowerCase()}`}>{booking.status}</span>
                </div>
                <div className="booking-body">
                  <div className="booking-detail">
                    <span className="label">Venue:</span>
                    <span className="value">{booking.venueName || `#${booking.venueId}`}</span>
                  </div>
                  {booking.showDateTime && (
                    <div className="booking-detail">
                      <span className="label">Date & Time:</span>
                      <span className="value">{formatDate(booking.showDateTime)} {formatTime(booking.showDateTime)}</span>
                    </div>
                  )}
                  {booking.bookingCode && (
                    <div className="booking-detail">
                      <span className="label">Booking Code:</span>
                      <span className="value code">{booking.bookingCode}</span>
                    </div>
                  )}
                  <div className="booking-detail">
                    <span className="label">Seats:</span>
                    <span className="value">{booking.seatNumbers}</span>
                  </div>
                  <div className="booking-detail">
                    <span className="label">Amount:</span>
                    <span className="value">₹{booking.totalAmount}</span>
                  </div>
                </div>
                {tab === 'upcoming' && booking.status === 'CONFIRMED' && (
                  <div className="booking-actions">
                    <button
                      type="button"
                      className="btn-view-ticket"
                      onClick={() => navigate('/booking-confirmation', { state: { booking } })}
                    >
                      View Ticket
                    </button>
                    <button
                      type="button"
                      className="btn-cancel"
                      onClick={() => handleCancel(booking.id)}
                      disabled={cancellingId === booking.id}
                    >
                      {cancellingId === booking.id ? 'Cancelling...' : 'Cancel Booking'}
                    </button>
                  </div>
                )}
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

export default MyBookings;
