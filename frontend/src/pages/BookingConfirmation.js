import React from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import '../styles/BookingConfirmation.css';

function BookingConfirmation() {
  const location = useLocation();
  const navigate = useNavigate();
  const { booking } = location.state || {};

  if (!booking) {
    return (
      <div className="confirmation-error">
        <h2>No booking information found</h2>
        <button onClick={() => navigate('/')}>Go to Home</button>
      </div>
    );
  }

  const formatDateTime = (dateTime) => {
    if (!dateTime) return 'N/A';
    const date = new Date(dateTime);
    return date.toLocaleString('en-US', {
      weekday: 'long',
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
      hour12: true
    });
  };

  return (
    <div className="booking-confirmation">
      <div className="confirmation-card">
        <div className="success-icon">✓</div>
        <h1>Booking Confirmed!</h1>
        <p className="success-message">Your tickets have been booked successfully</p>

        {booking.bookingCode && (
          <div className="booking-code">
            <label>Booking Code</label>
            <div className="code">{booking.bookingCode}</div>
          </div>
        )}

        <div className="booking-details">
          <h2>Booking Details</h2>
          <div className="detail-row">
            <span className="label">Event:</span>
            <span className="value">{booking.eventName || 'N/A'}</span>
          </div>
          <div className="detail-row">
            <span className="label">Venue:</span>
            <span className="value">{booking.venueName || `Venue #${booking.venueId}`}</span>
          </div>
          {booking.showDateTime && (
            <div className="detail-row">
              <span className="label">Show Time:</span>
              <span className="value">{formatDateTime(booking.showDateTime)}</span>
            </div>
          )}
          <div className="detail-row">
            <span className="label">Seats:</span>
            <span className="value">{booking.seatNumbers}</span>
          </div>
          <div className="detail-row">
            <span className="label">Number of Seats:</span>
            <span className="value">{booking.numberOfSeats}</span>
          </div>
          <div className="detail-row total">
            <span className="label">Total Amount:</span>
            <span className="value">₹{booking.totalAmount}</span>
          </div>
        </div>

        <div className="qr-placeholder">
          <div className="qr-box">QR Code</div>
          <p className="qr-text">Show this at the venue</p>
        </div>

        <div className="action-buttons no-print">
          <button type="button" className="btn-download" onClick={() => window.print()}>
            Download Ticket
          </button>
          <button type="button" className="btn-primary" onClick={() => navigate('/')}>
            Book More Tickets
          </button>
          <button type="button" className="btn-secondary" onClick={() => navigate('/bookings')}>
            View My Bookings
          </button>
        </div>
      </div>
    </div>
  );
}

export default BookingConfirmation;
