import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { adminService } from '../services/api';
import '../styles/Admin.css';

function AdminVenues() {
  const navigate = useNavigate();
  const [venues, setVenues] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [editingVenue, setEditingVenue] = useState(null);
  const [formData, setFormData] = useState({
    name: '',
    city: '',
    address: '',
    totalRows: '',
    seatsPerRow: '',
    amenities: '',
  });

  useEffect(() => {
    const userRole = localStorage.getItem('userRole');
    if ((userRole || '').toUpperCase() !== 'ADMIN') {
      navigate('/');
      return;
    }
    loadVenues();
  }, [navigate]);

  const loadVenues = async () => {
    try {
      const response = await adminService.getAllVenues();
      if (response.success) {
        setVenues(response.data);
      }
    } catch (error) {
      console.error('Failed to load venues:', error);
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
      const venueData = {
        ...formData,
        totalRows: parseInt(formData.totalRows),
        seatsPerRow: parseInt(formData.seatsPerRow),
      };

      let response;
      if (editingVenue) {
        response = await adminService.updateVenue(editingVenue.id, venueData);
      } else {
        response = await adminService.createVenue(venueData);
      }

      if (response.success) {
        alert(response.message);
        setShowForm(false);
        setEditingVenue(null);
        resetForm();
        loadVenues();
      } else {
        alert('Error: ' + (response.message || 'Failed to save venue'));
      }
    } catch (error) {
      console.error('Failed to save venue:', error);
      alert('Failed to save venue');
    }
  };

  const handleEdit = (venue) => {
    setEditingVenue(venue);
    setFormData({
      name: venue.name,
      city: venue.city,
      address: venue.address,
      totalRows: venue.totalRows,
      seatsPerRow: venue.seatsPerRow,
      amenities: venue.amenities || '',
    });
    setShowForm(true);
  };

  const handleDelete = async (venueId) => {
    if (!window.confirm('Are you sure you want to delete this venue?')) return;

    try {
      const response = await adminService.deleteVenue(venueId);
      if (response.success) {
        alert('Venue deleted successfully');
        loadVenues();
      } else {
        alert('Failed to delete venue');
      }
    } catch (error) {
      console.error('Failed to delete venue:', error);
      alert('Failed to delete venue');
    }
  };

  const resetForm = () => {
    setFormData({
      name: '',
      city: '',
      address: '',
      totalRows: '',
      seatsPerRow: '',
      amenities: '',
    });
  };

  const handleCancel = () => {
    setShowForm(false);
    setEditingVenue(null);
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
          <Link to="/admin/events" className="nav-link">Events</Link>
          <Link to="/admin/venues" className="nav-link active">Venues</Link>
          <Link to="/admin/shows" className="nav-link">Shows</Link>
          <button onClick={handleLogout} className="logout-btn">Logout</button>
        </div>
      </nav>

      <div className="admin-content">
        <div className="page-header">
          <h1>Manage Venues</h1>
          <button onClick={() => setShowForm(!showForm)} className="btn-primary">
            {showForm ? 'Cancel' : '+ Add New Venue'}
          </button>
        </div>

        {showForm && (
          <div className="form-card">
            <h2>{editingVenue ? 'Edit Venue' : 'Create New Venue'}</h2>
            <form onSubmit={handleSubmit}>
              <div className="form-grid">
                <div className="form-group">
                  <label>Venue Name *</label>
                  <input
                    type="text"
                    name="name"
                    value={formData.name}
                    onChange={handleInputChange}
                    placeholder="PVR Phoenix Mall"
                    required
                  />
                </div>

                <div className="form-group">
                  <label>City *</label>
                  <input
                    type="text"
                    name="city"
                    value={formData.city}
                    onChange={handleInputChange}
                    placeholder="Mumbai"
                    required
                  />
                </div>

                <div className="form-group full-width">
                  <label>Address *</label>
                  <input
                    type="text"
                    name="address"
                    value={formData.address}
                    onChange={handleInputChange}
                    placeholder="Phoenix Market City, Lower Parel, Mumbai"
                    required
                  />
                </div>

                <div className="form-group">
                  <label>Total Rows *</label>
                  <input
                    type="number"
                    name="totalRows"
                    value={formData.totalRows}
                    onChange={handleInputChange}
                    min="1"
                    max="20"
                    placeholder="15"
                    required
                  />
                </div>

                <div className="form-group">
                  <label>Seats Per Row *</label>
                  <input
                    type="number"
                    name="seatsPerRow"
                    value={formData.seatsPerRow}
                    onChange={handleInputChange}
                    min="1"
                    max="30"
                    placeholder="20"
                    required
                  />
                </div>

                <div className="form-group full-width">
                  <label>Amenities</label>
                  <input
                    type="text"
                    name="amenities"
                    value={formData.amenities}
                    onChange={handleInputChange}
                    placeholder="Dolby Atmos, IMAX, Food Court, Parking"
                  />
                </div>
              </div>

              <div className="form-actions">
                <button type="submit" className="btn-primary">
                  {editingVenue ? 'Update Venue' : 'Create Venue'}
                </button>
                <button type="button" onClick={handleCancel} className="btn-secondary">
                  Cancel
                </button>
              </div>
            </form>
          </div>
        )}

        {loading ? (
          <div className="loading">Loading venues...</div>
        ) : (
          <div className="table-container">
            <table className="data-table">
              <thead>
                <tr>
                  <th>Name</th>
                  <th>City</th>
                  <th>Address</th>
                  <th>Rows</th>
                  <th>Seats/Row</th>
                  <th>Total Capacity</th>
                  <th>Amenities</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {venues.length === 0 ? (
                  <tr>
                    <td colSpan="8" style={{ textAlign: 'center' }}>No venues found</td>
                  </tr>
                ) : (
                  venues.map(venue => (
                    <tr key={venue.id}>
                      <td><strong>{venue.name}</strong></td>
                      <td>{venue.city}</td>
                      <td>{venue.address}</td>
                      <td>{venue.totalRows}</td>
                      <td>{venue.seatsPerRow}</td>
                      <td>{venue.totalRows * venue.seatsPerRow}</td>
                      <td>{venue.amenities || '-'}</td>
                      <td className="action-buttons">
                        <button onClick={() => handleEdit(venue)} className="btn-edit">Edit</button>
                        <button onClick={() => handleDelete(venue.id)} className="btn-delete">Delete</button>
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

export default AdminVenues;
