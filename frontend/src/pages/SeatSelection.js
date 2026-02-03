import React, { useState, useEffect, useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { showService } from '../services/api';
import '../styles/SeatSelection.css';

const LOCK_MINUTES = 10;

function SeatSelection() {
  const { showId } = useParams();
  const navigate = useNavigate();
  const [show, setShow] = useState(null);
  const [seats, setSeats] = useState([]);
  const [selectedSeats, setSelectedSeats] = useState([]);
  const [loading, setLoading] = useState(true);
  const [secondsLeft, setSecondsLeft] = useState(LOCK_MINUTES * 60);
  const [timerExpired, setTimerExpired] = useState(false);
  const timerStarted = useRef(false);

  useEffect(() => {
    const load = async () => {
      try {
        const [showRes, seatsRes] = await Promise.all([
          showService.getShowById(showId),
          showService.getSeats(showId),
        ]);
        if (showRes.success) setShow(showRes.data);
        if (seatsRes.success) setSeats(seatsRes.data || []);
      } catch (e) {
        console.error(e);
      } finally {
        setLoading(false);
      }
    };
    load();
  }, [showId]);

  useEffect(() => {
    if (selectedSeats.length === 0) {
      timerStarted.current = false;
      return;
    }
    if (!timerStarted.current) {
      timerStarted.current = true;
      setSecondsLeft(LOCK_MINUTES * 60);
    }
  }, [selectedSeats.length]);

  useEffect(() => {
    if (selectedSeats.length === 0 || secondsLeft <= 0) return;
    const t = setInterval(() => {
      setSecondsLeft((prev) => {
        if (prev <= 1) {
          setTimerExpired(true);
          return 0;
        }
        return prev - 1;
      });
    }, 1000);
    return () => clearInterval(t);
  }, [selectedSeats.length, secondsLeft]);

  useEffect(() => {
    if (timerExpired) {
      const releaseAndExit = async () => {
        try {
          if (selectedSeats.length > 0) {
            await showService.releaseSeats(showId, selectedSeats.map((s) => s.id));
          }
        } catch (err) {
          console.error('Failed to release seats after timer expiry', err);
        } finally {
          const msg = 'Time expired. Seats released. Please select again.';
          alert(msg);
          navigate(-1);
        }
      };
      releaseAndExit();
    }
  }, [timerExpired, navigate, selectedSeats, showId]);

  useEffect(() => {
    return () => {
      if (selectedSeats.length > 0) {
        showService
          .releaseSeats(showId, selectedSeats.map((s) => s.id))
          .catch((err) => console.error('Failed to release seats on unmount', err));
      }
    };
  }, [selectedSeats, showId]);

  const handleSeatClick = async (seat) => {
    const userId = localStorage.getItem('userId');
    if (!userId) {
      alert('Please login before selecting seats');
      navigate('/login');
      return;
    }

    if (seat.status !== 'AVAILABLE') return;

    let newSelectedSeats;
    if (selectedSeats.find((s) => s.id === seat.id)) {
      newSelectedSeats = selectedSeats.filter((s) => s.id !== seat.id);
    } else {
      if (selectedSeats.length >= 10) {
        alert('Maximum 10 seats can be selected');
        return;
      }
      newSelectedSeats = [...selectedSeats, seat];
    }

    try {
      const res = await showService.lockSeats(showId, newSelectedSeats.map((s) => s.id));
      if (res.success) {
        const updatedSeats = res.data || [];
        setSeats(updatedSeats);
        const currentUserId = Number(userId);
        const lockedForUser = updatedSeats.filter(
          (s) => s.lockedByUserId === currentUserId && s.status === 'BLOCKED'
        );
        setSelectedSeats(lockedForUser);
      }
    } catch (err) {
      console.error('Failed to lock seats', err);
    }
  };

  const calculateTotal = () => {
    if (!show) return 0;

    let total = selectedSeats.reduce((sum, seat) => {
      let price = 0;
      switch (seat.category) {
        case 'STANDARD':
          price = show.priceStandard;
          break;
        case 'PREMIUM':
          price = show.pricePremium;
          break;
        case 'VIP':
          price = show.priceVip;
          break;
        default:
          price = show.priceStandard;
      }
      return sum + price;
    }, 0);

    return total + 50; 
  };

  const handleProceedToPay = () => {
    if (selectedSeats.length === 0) {
      alert('Please select at least one seat');
      return;
    }
    if (!localStorage.getItem('userId')) {
      navigate('/login');
      return;
    }

    const bookingData = {
      showId: parseInt(showId, 10),
      seatIds: selectedSeats.map((s) => s.id),
      eventName: show.eventName || 'Event',
      venueName: show.venueName || 'Venue',
      seatNumbers: selectedSeats.map((s) => s.seatNumber).join(', '),
      totalAmount: calculateTotal(),
    };

    navigate('/payment', { state: { bookingData } });
  };

  const formatTimer = (s) => {
    const m = Math.floor(s / 60);
    const sec = s % 60;
    return `${m}:${sec < 10 ? '0' : ''}${sec}`;
  };

  const getPriceBreakdown = () => {
    if (!show) return [];
    const byCat = {};
    selectedSeats.forEach((seat) => {
      let price = show.priceStandard;
      if (seat.category === 'PREMIUM') price = show.pricePremium;
      else if (seat.category === 'VIP') price = show.priceVip;
      const key = seat.category;
      if (!byCat[key]) byCat[key] = { count: 0, price, label: key };
      byCat[key].count += 1;
    });
    return Object.values(byCat);
  };

  if (loading || !show) {
    return <div className="loading">Loading seats...</div>;
  }

  const seatsByRow = seats.reduce((acc, seat) => {
    const row = seat.rowNumber;
    if (!acc[row]) {
      acc[row] = [];
    }
    acc[row].push(seat);
    return acc;
  }, {});

  return (
    <div className="seat-selection">
      <button className="back-btn" onClick={() => navigate(-1)}>← Back</button>

      <div className="screen">
        <div className="screen-label">SCREEN THIS WAY</div>
      </div>

      <div className="seat-layout">
        {Object.keys(seatsByRow).sort((a, b) => a - b).map(rowNum => (
          <div key={rowNum} className="seat-row">
            <div className="row-label">Row {String.fromCharCode(64 + parseInt(rowNum))}</div>
            <div className="seats">
              {seatsByRow[rowNum]
                .sort((a, b) => a.columnNumber - b.columnNumber)
                .map(seat => (
                  <div
                    key={seat.id}
                    className={`seat ${seat.category.toLowerCase()} ${seat.status.toLowerCase()} ${
                      selectedSeats.find(s => s.id === seat.id) ? 'selected' : ''
                    }`}
                    onClick={() => handleSeatClick(seat)}
                  >
                    {seat.seatNumber}
                  </div>
                ))}
            </div>
          </div>
        ))}
      </div>

      <div className="legend">
        <div className="legend-item">
          <div className="seat-sample available"></div>
          <span>Available</span>
        </div>
        <div className="legend-item">
          <div className="seat-sample selected"></div>
          <span>Selected</span>
        </div>
        <div className="legend-item">
          <div className="seat-sample booked"></div>
          <span>Booked</span>
        </div>
        <div className="legend-item">
          <div className="seat-sample standard"></div>
          <span>Standard - ₹{show.priceStandard}</span>
        </div>
        <div className="legend-item">
          <div className="seat-sample premium"></div>
          <span>Premium - ₹{show.pricePremium}</span>
        </div>
        <div className="legend-item">
          <div className="seat-sample vip"></div>
          <span>VIP - ₹{show.priceVip}</span>
        </div>
      </div>

      {selectedSeats.length > 0 && (
        <div className="booking-summary">
          <div className="summary-content">
            <div className="timer-row">
              <span>Time left to complete:</span>
              <span className={`timer ${secondsLeft <= 60 ? 'warning' : ''}`}>{formatTimer(secondsLeft)}</span>
            </div>
            <div className="selected-info">
              <h3>Selected Seats</h3>
              <p>{selectedSeats.map((s) => s.seatNumber).join(', ')}</p>
              <p className="seat-count">{selectedSeats.length} seat(s) selected (max 10)</p>
            </div>
            <div className="price-info">
              {getPriceBreakdown().map((item) => (
                <div key={item.label} className="price-row">
                  <span>{item.count}x {item.label} @ ₹{item.price}</span>
                  <span>₹{item.count * item.price}</span>
                </div>
              ))}
              <div className="price-row">
                <span>Convenience Fee:</span>
                <span>₹50</span>
              </div>
              <div className="price-row total">
                <span>Total:</span>
                <span>₹{calculateTotal()}</span>
              </div>
            </div>
            <button type="button" className="proceed-btn" onClick={handleProceedToPay}>
              Proceed to Pay
            </button>
          </div>
        </div>
      )}
    </div>
  );
}

export default SeatSelection;
