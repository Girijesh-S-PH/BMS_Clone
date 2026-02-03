import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { adminService, eventService } from '../services/api';
import '../styles/Admin.css';

function AdminShows() {
  const navigate = useNavigate();
  const [events, setEvents] = useState([]);
  const [venues, setVenues] = useState([]);
  const [shows, setShows] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState({
    eventId: '',
    venueId: '',
    showDateTime: '',
    priceStandard: '',
    pricePremium: '',
    priceVip: '',
  });

  useEffect(() => {
    const userRole = localStorage.getItem('userRole');
    if ((userRole || '').toUpperCase() !== 'ADMIN') {
      navigate('/');
      return;
    }
    loadData();
  }, [navigate]);

  const loadData = async () => {
    try {
      const [eventsRes, venuesRes, showsRes] = await Promise.all([
        eventService.getEvents(),
        adminService.getAllVenues(),
        adminService.getAllShows(),
      ]);

      if (eventsRes.success) {
        setEvents(eventsRes.data);
      }
      if (venuesRes.success) {
        setVenues(venuesRes.data);
      }
      if (showsRes.success) {
        setShows(showsRes.data || []);
      }
    } catch (error) {
      console.error('Failed to load data:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteShow = async (showId) => {
    if (!window.confirm('Are you sure you want to delete this show? All associated seats and bookings will be affected.')) return;
    try {
      const response = await adminService.deleteShow(showId);
      if (response.success) {
        alert('Show deleted successfully');
        loadData();
      } else {
        alert('Failed to delete show: ' + (response.message || 'Unknown error'));
      }
    } catch (error) {
      console.error('Failed to delete show:', error);
      alert('Failed to delete show');
    }
  };

  const formatShowDateTime = (dateTime) => {
    if (!dateTime) return '—';
    const d = new Date(dateTime);
    return d.toLocaleString('en-IN', { dateStyle: 'medium', timeStyle: 'short' });
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const showData = {
        eventId: parseInt(formData.eventId),
        venueId: parseInt(formData.venueId),
        showDateTime: formData.showDateTime,
        priceStandard: parseFloat(formData.priceStandard),
        pricePremium: parseFloat(formData.pricePremium),
        priceVip: parseFloat(formData.priceVip),
      };

      const response = await adminService.createShow(showData);

      if (response.success) {
        alert(response.message + '\n\nShow ID: ' + response.data.id);
        setShowForm(false);
        resetForm();
        loadData();
      } else {
        alert('Error: ' + (response.message || 'Failed to create show'));
      }
    } catch (error) {
      console.error('Failed to create show:', error);
      alert('Failed to create show');
    }
  };

  const resetForm = () => {
    setFormData({
      eventId: '',
      venueId: '',
      showDateTime: '',
      priceStandard: '',
      pricePremium: '',
      priceVip: '',
    });
  };

  const handleCancel = () => {
    setShowForm(false);
    resetForm();
  };

  const handleLogout = () => {
    localStorage.clear();
    navigate('/login');
  };

  const getDefaultDateTime = () => {
    const now = new Date();
    now.setMinutes(0);
    now.setSeconds(0);
    return now.toISOString().slice(0, 16);
  };

  return (
    <div className="admin-container">
      <nav className="admin-nav">
        <div className="admin-nav-brand">
          <h2>BookMyShow Admin</h2>
        </div>
        <div className="admin-nav-links">
          <Link to="/admin/dashboard" className="nav-link">Dashboard</Link>
          <Link to="/admin/events" className="nav-link">Events</Link>
          <Link to="/admin/venues" className="nav-link">Venues</Link>
          <Link to="/admin/shows" className="nav-link active">Shows</Link>
          <button onClick={handleLogout} className="logout-btn">Logout</button>
        </div>
      </nav>

      <div className="admin-content">
        <div className="page-header">
          <h1>Manage Shows</h1>
          <button
            onClick={() => setShowForm(!showForm)}
            className="btn-primary"
            disabled={events.length === 0 || venues.length === 0}
          >
            {showForm ? 'Cancel' : '+ Add New Show'}
          </button>
        </div>

        {(events.length === 0 || venues.length === 0) && (
          <div className="alert alert-warning">
            {events.length === 0 && <p>Please add events first before creating shows.</p>}
            {venues.length === 0 && <p>Please add venues first before creating shows.</p>}
          </div>
        )}

        {showForm && (
          <div className="form-card">
            <h2>Create New Show</h2>
            <form onSubmit={handleSubmit}>
              <div className="form-grid">
                <div className="form-group">
                  <label>Event *</label>
                  <select
                    name="eventId"
                    value={formData.eventId}
                    onChange={handleInputChange}
                    required
                  >
                    <option value="">Select Event</option>
                    {events.map(event => (
                      <option key={event.id} value={event.id}>
                        {event.title} ({event.eventType})
                      </option>
                    ))}
                  </select>
                </div>

                <div className="form-group">
                  <label>Venue *</label>
                  <select
                    name="venueId"
                    value={formData.venueId}
                    onChange={handleInputChange}
                    required
                  >
                    <option value="">Select Venue</option>
                    {venues.map(venue => (
                      <option key={venue.id} value={venue.id}>
                        {venue.name} - {venue.city}
                      </option>
                    ))}
                  </select>
                </div>

                <div className="form-group">
                  <label>Show Date & Time *</label>
                  <input
                    type="datetime-local"
                    name="showDateTime"
                    value={formData.showDateTime}
                    onChange={handleInputChange}
                    min={getDefaultDateTime()}
                    required
                  />
                </div>

                <div className="form-group">
                  <label>Standard Seat Price (₹) *</label>
                  <input
                    type="number"
                    name="priceStandard"
                    value={formData.priceStandard}
                    onChange={handleInputChange}
                    step="0.01"
                    min="0"
                    placeholder="150.00"
                    required
                  />
                </div>

                <div className="form-group">
                  <label>Premium Seat Price (₹) *</label>
                  <input
                    type="number"
                    name="pricePremium"
                    value={formData.pricePremium}
                    onChange={handleInputChange}
                    step="0.01"
                    min="0"
                    placeholder="250.00"
                    required
                  />
                </div>

                <div className="form-group">
                  <label>VIP Seat Price (₹) *</label>
                  <input
                    type="number"
                    name="priceVip"
                    value={formData.priceVip}
                    onChange={handleInputChange}
                    step="0.01"
                    min="0"
                    placeholder="350.00"
                    required
                  />
                </div>
              </div>

              <div className="form-actions">
                <button type="submit" className="btn-primary">
                  Create Show
                </button>
                <button type="button" onClick={handleCancel} className="btn-secondary">
                  Cancel
                </button>
              </div>
            </form>
          </div>
        )}

        {loading ? (
          <div className="loading">Loading data...</div>
        ) : (
          <>
            <div className="info-panel">
              <h3>Quick Stats</h3>
              <div className="stats-row">
                <div className="stat-item">
                  <span className="stat-label">Total Events:</span>
                  <span className="stat-value">{events.length}</span>
                </div>
                <div className="stat-item">
                  <span className="stat-label">Total Venues:</span>
                  <span className="stat-value">{venues.length}</span>
                </div>
                <div className="stat-item">
                  <span className="stat-label">Total Shows:</span>
                  <span className="stat-value">{shows.length}</span>
                </div>
              </div>
            </div>

            {shows.length > 0 && (
              <div className="table-container" style={{ marginTop: '1.5rem' }}>
                <h3 style={{ marginBottom: '1rem', color: '#1a1a1a' }}>All Shows</h3>
                <table className="data-table">
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>Event</th>
                      <th>Venue</th>
                      <th>City</th>
                      <th>Date & Time</th>
                      <th>Seats</th>
                      <th>Status</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {shows.map((show) => (
                      <tr key={show.id}>
                        <td>{show.id}</td>
                        <td>{show.eventTitle || `Event #${show.eventId}`}</td>
                        <td>{show.venueName || `Venue #${show.venueId}`}</td>
                        <td>{show.venueCity || '—'}</td>
                        <td>{formatShowDateTime(show.showDateTime)}</td>
                        <td>{show.availableSeats != null ? `${show.availableSeats}/${show.totalSeats}` : '—'}</td>
                        <td>
                          <span className={`badge badge-${(show.status || '').toLowerCase().replace(' ', '_')}`}>
                            {(show.status || '—').replace('_', ' ')}
                          </span>
                        </td>
                        <td className="action-buttons">
                          <button
                            type="button"
                            className="btn-delete"
                            onClick={() => handleDeleteShow(show.id)}
                          >
                            Delete
                          </button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </>
        )}
      </div>
    </div>
  );
}

export default AdminShows;
