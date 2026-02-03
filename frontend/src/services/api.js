const API_BASE = process.env.REACT_APP_API_BASE || 'http://localhost:8080/api';

export const authService = {
  signup: async (data) => {
    const response = await fetch(`${API_BASE}/auth/signup`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    });
    return response.json();
  },

  login: async (phoneNumber, password) => {
    const response = await fetch(`${API_BASE}/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        phoneNumber,
        password,
        captchaToken: 'test-token',
      }),
    });
    return response.json();
  },
};

const getAuthHeaders = () => {
  const headers = { 'Content-Type': 'application/json' };
  const userId = localStorage.getItem('userId');
  if (userId) {
    headers['X-User-Id'] = userId;
  }
  return headers;
};

export const eventService = {
  getEvents: async (status = null, type = null, sort = null) => {
    let url = `${API_BASE}/events`;
    const params = [];
    if (status) params.push(`status=${status}`);
    if (type) params.push(`type=${type}`);
    if (sort) params.push(`sort=${sort}`);
    if (params.length) url += '?' + params.join('&');
    const response = await fetch(url, { headers: getAuthHeaders() });
    return response.json();
  },

  getEventDetails: async (eventId) => {
    const response = await fetch(`${API_BASE}/events/${eventId}`, { headers: getAuthHeaders() });
    return response.json();
  },

  getReviews: async (eventId) => {
    const response = await fetch(`${API_BASE}/events/${eventId}/reviews`, { headers: getAuthHeaders() });
    return response.json();
  },

  addReview: async (eventId, rating, comment) => {
    const response = await fetch(`${API_BASE}/events/${eventId}/reviews`, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify({ rating, comment }),
    });
    return response.json();
  },

  getShowsByEvent: async (eventId) => {
    const response = await fetch(`${API_BASE}/shows/event/${eventId}`, { headers: getAuthHeaders() });
    return response.json();
  },
};

export const bookingService = {
  getBookingById: async (bookingId) => {
    const response = await fetch(`${API_BASE}/bookings/${bookingId}`, { headers: getAuthHeaders() });
    return response.json();
  },

  getBookings: async () => {
    const response = await fetch(`${API_BASE}/bookings`, { headers: getAuthHeaders() });
    return response.json();
  },

  createBooking: async (showId, seatIds) => {
    const response = await fetch(`${API_BASE}/bookings`, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify({ showId, seatIds }),
    });
    return response.json();
  },

  cancelBooking: async (bookingId) => {
    const response = await fetch(`${API_BASE}/bookings/${bookingId}`, {
      method: 'DELETE',
      headers: getAuthHeaders(),
    });
    return response.json();
  },
};

export const showService = {
  getShowById: async (showId) => {
    const response = await fetch(`${API_BASE}/shows/${showId}`, { headers: getAuthHeaders() });
    return response.json();
  },

  getSeats: async (showId) => {
    const response = await fetch(`${API_BASE}/shows/${showId}/seats`, { headers: getAuthHeaders() });
    return response.json();
  },

  lockSeats: async (showId, seatIds) => {
    const response = await fetch(`${API_BASE}/shows/${showId}/lock-seats`, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify(seatIds),
    });
    return response.json();
  },

  releaseSeats: async (showId, seatIds) => {
    const response = await fetch(`${API_BASE}/shows/${showId}/release-seats`, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify(seatIds),
    });
    return response.json();
  },
};

export const adminService = {
  // Event Management
  createEvent: async (eventData) => {
    const response = await fetch(`${API_BASE}/admin/events`, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify(eventData),
    });
    return response.json();
  },

  updateEvent: async (eventId, eventData) => {
    const response = await fetch(`${API_BASE}/admin/events/${eventId}`, {
      method: 'PUT',
      headers: getAuthHeaders(),
      body: JSON.stringify(eventData),
    });
    return response.json();
  },

  deleteEvent: async (eventId) => {
    const response = await fetch(`${API_BASE}/admin/events/${eventId}`, {
      method: 'DELETE',
      headers: getAuthHeaders(),
    });
    return response.json();
  },

  // Venue Management
  getAllVenues: async () => {
    const response = await fetch(`${API_BASE}/venues`, { headers: getAuthHeaders() });
    return response.json();
  },

  createVenue: async (venueData) => {
    const response = await fetch(`${API_BASE}/admin/venues`, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify(venueData),
    });
    return response.json();
  },

  updateVenue: async (venueId, venueData) => {
    const response = await fetch(`${API_BASE}/admin/venues/${venueId}`, {
      method: 'PUT',
      headers: getAuthHeaders(),
      body: JSON.stringify(venueData),
    });
    return response.json();
  },

  deleteVenue: async (venueId) => {
    const response = await fetch(`${API_BASE}/admin/venues/${venueId}`, {
      method: 'DELETE',
      headers: getAuthHeaders(),
    });
    return response.json();
  },

  // Show Management
  getAllShows: async () => {
    const response = await fetch(`${API_BASE}/admin/shows`, { headers: getAuthHeaders() });
    return response.json();
  },

  createShow: async (showData) => {
    const response = await fetch(`${API_BASE}/admin/shows`, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify(showData),
    });
    return response.json();
  },

  updateShowAvailability: async (showId) => {
    const response = await fetch(`${API_BASE}/admin/shows/${showId}/update-availability`, {
      headers: getAuthHeaders(),
    });
    return response.json();
  },

  deleteShow: async (showId) => {
    const response = await fetch(`${API_BASE}/admin/shows/${showId}`, {
      method: 'DELETE',
      headers: getAuthHeaders(),
    });
    return response.json();
  },

  // Dashboard Stats
  getDashboardStats: async () => {
    const response = await fetch(`${API_BASE}/admin/stats/dashboard`, { headers: getAuthHeaders() });
    return response.json();
  },
};
