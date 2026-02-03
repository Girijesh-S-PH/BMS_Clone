import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { adminService } from '../services/api';
import '../styles/Admin.css';

function AdminDashboard() {
  const navigate = useNavigate();
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const userRole = localStorage.getItem('userRole');
    if ((userRole || '').toUpperCase() !== 'ADMIN') {
      navigate('/');
      return;
    }

    loadDashboardStats();
  }, [navigate]);

  const loadDashboardStats = async () => {
    try {
      const response = await adminService.getDashboardStats();
      if (response.success) {
        setStats(response.data);
      }
    } catch (error) {
      console.error('Failed to load dashboard stats:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = () => {
    localStorage.clear();
    navigate('/login');
  };

  if (loading) {
    return <div className="admin-loading">Loading dashboard...</div>;
  }

  return (
    <div className="admin-container">
      <nav className="admin-nav">
        <div className="admin-nav-brand">
          <h2>BookMyShow Admin</h2>
        </div>
        <div className="admin-nav-links">
          <Link to="/admin/dashboard" className="nav-link active">Dashboard</Link>
          <Link to="/admin/events" className="nav-link">Events</Link>
          <Link to="/admin/venues" className="nav-link">Venues</Link>
          <Link to="/admin/shows" className="nav-link">Shows</Link>
          <button onClick={handleLogout} className="logout-btn">Logout</button>
        </div>
      </nav>

      <div className="admin-content">
        <h1>Dashboard</h1>

        <div className="stats-grid">
          <div className="stat-card">
            <h3>Total Events</h3>
            <p className="stat-number">{stats?.totalEvents || 0}</p>
          </div>
          <div className="stat-card">
            <h3>Total Venues</h3>
            <p className="stat-number">{stats?.totalVenues || 0}</p>
          </div>
        </div>

        <div className="quick-actions">
          <h2>Quick Actions</h2>
          <div className="action-buttons">
            <Link to="/admin/events" className="action-btn">
              <span>📽️</span>
              <div>
                <h3>Manage Events</h3>
                <p>Add, edit, or remove events</p>
              </div>
            </Link>
            <Link to="/admin/venues" className="action-btn">
              <span>🏛️</span>
              <div>
                <h3>Manage Venues</h3>
                <p>Add, edit, or remove venues</p>
              </div>
            </Link>
            <Link to="/admin/shows" className="action-btn">
              <span>🎬</span>
              <div>
                <h3>Manage Shows</h3>
                <p>Create and schedule shows</p>
              </div>
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
}

export default AdminDashboard;
