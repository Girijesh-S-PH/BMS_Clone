import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { bookingService } from '../services/api';
import '../styles/Payment.css';

const LOCK_MINUTES = 10;

function PaymentGateway() {
  const navigate = useNavigate();
  const location = useLocation();
  const { bookingData } = location.state || {};
  const [processing, setProcessing] = useState(false);
  const [paymentMethod, setPaymentMethod] = useState('card');
  const [secondsLeft, setSecondsLeft] = useState(LOCK_MINUTES * 60);
  const [timerExpired, setTimerExpired] = useState(false);

  useEffect(() => {
    if (!bookingData) {
      navigate('/');
    }
  }, [bookingData, navigate]);

  useEffect(() => {
    if (!bookingData || timerExpired) return;
    const timer = setInterval(() => {
      setSecondsLeft(prev => {
        if (prev <= 1) {
          setTimerExpired(true);
          return 0;
        }
        return prev - 1;
      });
    }, 1000);
    return () => clearInterval(timer);
  }, [bookingData, timerExpired]);

  const formatTimer = (s) => {
    const m = Math.floor(s / 60);
    const sec = s % 60;
    return `${m}:${sec < 10 ? '0' : ''}${sec}`;
  };

  const handlePayment = async () => {
    if (timerExpired || secondsLeft <= 0) {
      alert('Time expired. Please go back and reselect your seats.');
      return;
    }

    setProcessing(true);

    // Simulate payment processing delay
    await new Promise(resolve => setTimeout(resolve, 2000));

    try {
      // Create the actual booking after "payment" succeeds
      const data = await bookingService.createBooking(
        bookingData.showId,
        bookingData.seatIds
      );

      if (data.success) {
        navigate('/booking-confirmation', { state: { booking: data.data } });
      } else {
        alert('Payment failed: ' + (data.message || 'Please try again'));
        setProcessing(false);
      }
    } catch (error) {
      console.error('Payment error:', error);
      alert('Payment failed. Please try again.');
      setProcessing(false);
    }
  };

  if (!bookingData) {
    return (
      <div className="payment-gateway">
        <div className="payment-container payment-missing">
          <h1>No Booking Data</h1>
          <p className="payment-subtitle">Your session may have expired or you arrived here without selecting seats.</p>
          <button type="button" className="btn-pay" onClick={() => navigate('/')}>
            Back to Home
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="payment-gateway">
      <div className="payment-container">
        <h1>Payment Gateway</h1>
        <p className="payment-subtitle">Complete your booking payment</p>

        <div className="payment-summary">
          <h2>Order Summary</h2>
          <div className="summary-row">
            <span>Event:</span>
            <span>{bookingData.eventName}</span>
          </div>
          <div className="summary-row">
            <span>Venue:</span>
            <span>{bookingData.venueName}</span>
          </div>
          <div className="summary-row">
            <span>Seats:</span>
            <span>{bookingData.seatNumbers}</span>
          </div>
          <div className="summary-row total">
            <span>Total Amount:</span>
            <span>₹{bookingData.totalAmount}</span>
          </div>
        </div>

        <div className="payment-timer-row">
          <span>Time left to complete payment:</span>
          <span className={`payment-timer ${secondsLeft <= 60 ? 'warning' : ''}`}>
            {formatTimer(secondsLeft)}
          </span>
        </div>

        <div className="payment-methods">
          <h2>Select Payment Method</h2>
          <div className="payment-options">
            <label className={`payment-option ${paymentMethod === 'card' ? 'selected' : ''}`}>
              <input
                type="radio"
                name="payment"
                value="card"
                checked={paymentMethod === 'card'}
                onChange={(e) => setPaymentMethod(e.target.value)}
              />
              <div className="option-content">
                <span className="option-icon">💳</span>
                <span>Credit/Debit Card</span>
              </div>
            </label>
            <label className={`payment-option ${paymentMethod === 'upi' ? 'selected' : ''}`}>
              <input
                type="radio"
                name="payment"
                value="upi"
                checked={paymentMethod === 'upi'}
                onChange={(e) => setPaymentMethod(e.target.value)}
              />
              <div className="option-content">
                <span className="option-icon">📱</span>
                <span>UPI</span>
              </div>
            </label>
            <label className={`payment-option ${paymentMethod === 'wallet' ? 'selected' : ''}`}>
              <input
                type="radio"
                name="payment"
                value="wallet"
                checked={paymentMethod === 'wallet'}
                onChange={(e) => setPaymentMethod(e.target.value)}
              />
              <div className="option-content">
                <span className="option-icon">👛</span>
                <span>Wallet</span>
              </div>
            </label>
            <label className={`payment-option ${paymentMethod === 'netbanking' ? 'selected' : ''}`}>
              <input
                type="radio"
                name="payment"
                value="netbanking"
                checked={paymentMethod === 'netbanking'}
                onChange={(e) => setPaymentMethod(e.target.value)}
              />
              <div className="option-content">
                <span className="option-icon">🏦</span>
                <span>Net Banking</span>
              </div>
            </label>
          </div>
        </div>

        <div className="payment-notice">
          <p>🔒 This is a demo payment gateway. No actual payment will be charged.</p>
        </div>

        <div className="payment-actions">
          <button
            type="button"
            className="btn-cancel"
            onClick={() => navigate(-1)}
            disabled={processing}
          >
            Cancel
          </button>
          <button
            type="button"
            className="btn-pay"
            onClick={handlePayment}
            disabled={processing || timerExpired}
          >
            {processing ? 'Processing...' : `Pay ₹${bookingData.totalAmount}`}
          </button>
        </div>
      </div>
    </div>
  );
}

export default PaymentGateway;
