import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { adminService, eventService } from '../services/api';
import PosterImage from '../components/PosterImage';
import '../styles/Admin.css';

function AdminEvents() {
  const navigate = useNavigate();
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [editingEvent, setEditingEvent] = useState(null);
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    posterUrl: '',
    genre: '',
    language: '',
    durationMinutes: '',
    rating: '',
    trailerUrl: '',
    cast: '',
    crew: '',
    status: 'NOW_SHOWING',
    eventType: 'MOVIE',
    releaseDate: new Date().toISOString().split('T')[0] + 'T00:00:00',
  });

  useEffect(() => {
    const userRole = localStorage.getItem('userRole');
    if ((userRole || '').toUpperCase() !== 'ADMIN') {
      navigate('/');
      return;
    }
    loadEvents();
  }, [navigate]);

  const loadEvents = async () => {
    try {
      const response = await eventService.getEvents();
      if (response.success) {
        setEvents(response.data);
      }
    } catch (error) {
      console.error('Failed to load events:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const eventData = {
        ...formData,
        durationMinutes: parseInt(formData.durationMinutes),
        rating: parseFloat(formData.rating),
      };

      let response;
      if (editingEvent) {
        response = await adminService.updateEvent(editingEvent.id, eventData);
      } else {
        response = await adminService.createEvent(eventData);
      }

      if (response.success) {
        alert(response.message);
        setShowForm(false);
        setEditingEvent(null);
        resetForm();
        loadEvents();
      } else {
        alert('Error: ' + (response.message || 'Failed to save event'));
      }
    } catch (error) {
      console.error('Failed to save event:', error);
      alert('Failed to save event');
    }
  };

  const handleEdit = (event) => {
    setEditingEvent(event);
    setFormData({
      title: event.title,
      description: event.description,
      posterUrl: event.posterUrl,
      genre: event.genre,
      language: event.language,
      durationMinutes: event.durationMinutes,
      rating: event.rating,
      trailerUrl: event.trailerUrl || '',
      cast: event.cast || '',
      crew: event.crew || '',
      status: event.status,
      eventType: event.eventType,
      releaseDate: event.releaseDate,
    });
    setShowForm(true);
  };

  const handleDelete = async (eventId) => {
    if (!window.confirm('Are you sure you want to delete this event?')) return;

    try {
      const response = await adminService.deleteEvent(eventId);
      if (response.success) {
        alert('Event deleted successfully');
        loadEvents();
      } else {
        alert('Failed to delete event');
      }
    } catch (error) {
      console.error('Failed to delete event:', error);
      alert('Failed to delete event');
    }
  };

  const resetForm = () => {
    setFormData({
      title: '',
      description: '',
      posterUrl: '',
      genre: '',
      language: '',
      durationMinutes: '',
      rating: '',
      trailerUrl: '',
      cast: '',
      crew: '',
      status: 'NOW_SHOWING',
      eventType: 'MOVIE',
      releaseDate: new Date().toISOString().split('T')[0] + 'T00:00:00',
    });
  };

  const handleCancel = () => {
    setShowForm(false);
    setEditingEvent(null);
    resetForm();
  };

  const handleLogout = () => {
    localStorage.clear();
    navigate('/login');
  };

  return (
    <div className="admin-container">
      <nav className="admin-nav">
        <div className="admin-nav-brand">
          <h2>BookMyShow Admin</h2>
        </div>
        <div className="admin-nav-links">
          <Link to="/admin/dashboard" className="nav-link">Dashboard</Link>
          <Link to="/admin/events" className="nav-link active">Events</Link>
          <Link to="/admin/venues" className="nav-link">Venues</Link>
          <Link to="/admin/shows" className="nav-link">Shows</Link>
          <button onClick={handleLogout} className="logout-btn">Logout</button>
        </div>
      </nav>

      <div className="admin-content">
        <div className="page-header">
          <h1>Manage Events</h1>
          <button onClick={() => setShowForm(!showForm)} className="btn-primary">
            {showForm ? 'Cancel' : '+ Add New Event'}
          </button>
        </div>

        {showForm && (
          <div className="form-card">
            <h2>{editingEvent ? 'Edit Event' : 'Create New Event'}</h2>
            <form onSubmit={handleSubmit}>
              <div className="form-grid">
                <div className="form-group">
                  <label>Title *</label>
                  <input
                    type="text"
                    name="title"
                    value={formData.title}
                    onChange={handleInputChange}
                    required
                  />
                </div>

                <div className="form-group">
                  <label>Event Type *</label>
                  <select
                    name="eventType"
                    value={formData.eventType}
                    onChange={handleInputChange}
                    required
                  >
                    <option value="MOVIE">Movie</option>
                    <option value="CONCERT">Concert</option>
                    <option value="PLAY">Play</option>
                    <option value="SPORTS">Sports</option>
                  </select>
                </div>

                <div className="form-group full-width">
                  <label>Description *</label>
                  <textarea
                    name="description"
                    value={formData.description}
                    onChange={handleInputChange}
                    rows="3"
                    required
                  />
                </div>

                <div className="form-group">
                  <label>Poster URL *</label>
                  <input
                    type="url"
                    name="posterUrl"
                    value={formData.posterUrl}
                    onChange={handleInputChange}
                    placeholder="https://image.tmdb.org/t/p/w500/xxxxx.jpg"
                    required
                  />
                  <span className="form-hint">Use a direct image link (e.g. TMDB: image.tmdb.org/t/p/w500/...). Some sites block hotlinking.</span>
                </div>

                <div className="form-group">
                  <label>Trailer URL</label>
                  <input
                    type="url"
                    name="trailerUrl"
                    value={formData.trailerUrl}
                    onChange={handleInputChange}
                    placeholder="https://youtube.com/..."
                  />
                </div>

                <div className="form-group">
                  <label>Genre *</label>
                  <input
                    type="text"
                    name="genre"
                    value={formData.genre}
                    onChange={handleInputChange}
                    placeholder="Action, Drama, Thriller"
                    required
                  />
                </div>

                <div className="form-group">
                  <label>Language *</label>
                  <input
                    type="text"
                    name="language"
                    value={formData.language}
                    onChange={handleInputChange}
                    placeholder="English, Hindi, Tamil"
                    required
                  />
                </div>

                <div className="form-group">
                  <label>Duration (minutes) *</label>
                  <input
                    type="number"
                    name="durationMinutes"
                    value={formData.durationMinutes}
                    onChange={handleInputChange}
                    min="1"
                    required
                  />
                </div>

                <div className="form-group">
                  <label>Rating *</label>
                  <input
                    type="number"
                    name="rating"
                    value={formData.rating}
                    onChange={handleInputChange}
                    step="0.1"
                    min="0"
                    max="10"
                    required
                  />
                </div>

                <div className="form-group">
                  <label>Status *</label>
                  <select
                    name="status"
                    value={formData.status}
                    onChange={handleInputChange}
                    required
                  >
                    <option value="NOW_SHOWING">Now Showing</option>
                    <option value="COMING_SOON">Coming Soon</option>
                    <option value="ENDED">Ended</option>
                  </select>
                </div>

                <div className="form-group">
                  <label>Release Date *</label>
                  <input
                    type="datetime-local"
                    name="releaseDate"
                    value={formData.releaseDate}
                    onChange={handleInputChange}
                    required
                  />
                </div>

                <div className="form-group full-width">
                  <label>Cast</label>
                  <input
                    type="text"
                    name="cast"
                    value={formData.cast}
                    onChange={handleInputChange}
                    placeholder="Actor 1, Actor 2, Actor 3"
                  />
                </div>

                <div className="form-group full-width">
                  <label>Crew</label>
                  <input
                    type="text"
                    name="crew"
                    value={formData.crew}
                    onChange={handleInputChange}
                    placeholder="Director: Name, Music: Name"
                  />
                </div>
              </div>

              <div className="form-actions">
                <button type="submit" className="btn-primary">
                  {editingEvent ? 'Update Event' : 'Create Event'}
                </button>
                <button type="button" onClick={handleCancel} className="btn-secondary">
                  Cancel
                </button>
              </div>
            </form>
          </div>
        )}

        {loading ? (
          <div className="loading">Loading events...</div>
        ) : (
          <div className="table-container">
            <table className="data-table">
              <thead>
                <tr>
                  <th>Poster</th>
                  <th>Title</th>
                  <th>Type</th>
                  <th>Genre</th>
                  <th>Language</th>
                  <th>Duration</th>
                  <th>Rating</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {events.length === 0 ? (
                  <tr>
                    <td colSpan="9" style={{ textAlign: 'center' }}>No events found</td>
                  </tr>
                ) : (
                  events.map(event => (
                    <tr key={event.id}>
                      <td>
                        <div className="table-poster-wrap">
                          <PosterImage
                            src={event.posterUrl}
                            alt={event.title}
                            placeholderLetter={event.title ? event.title.charAt(0) : '?'}
                            variant="table"
                            className="table-poster"
                          />
                        </div>
                      </td>
                      <td>{event.title}</td>
                      <td>{event.eventType}</td>
                      <td>{event.genre}</td>
                      <td>{event.language}</td>
                      <td>{event.durationMinutes} min</td>
                      <td>{event.rating}/10</td>
                      <td>
                        <span className={`badge badge-${event.status.toLowerCase()}`}>
                          {event.status.replace('_', ' ')}
                        </span>
                      </td>
                      <td className="action-buttons">
                        <button onClick={() => handleEdit(event)} className="btn-edit">Edit</button>
                        <button onClick={() => handleDelete(event.id)} className="btn-delete">Delete</button>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}

export default AdminEvents;
