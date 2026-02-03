import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { eventService } from '../services/api';
import PosterImage from '../components/PosterImage';
import '../styles/Home.css';

const CITIES = ['Mumbai', 'Delhi', 'Bangalore', 'Hyderabad', 'Chennai'];
const CATEGORIES = [
  { key: 'all', label: 'All' },
  { key: 'MOVIE', label: 'Movies' },
  { key: 'CONCERT', label: 'Concerts' },
  { key: 'PLAY', label: 'Plays' },
  { key: 'SPORTS', label: 'Sports' },
];

function Home() {
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [city, setCity] = useState(() => localStorage.getItem('lastSelectedCity') || 'Mumbai');
  const [category, setCategory] = useState('all');
  const [search, setSearch] = useState('');
  const [sort, setSort] = useState('');
  const [languageFilter, setLanguageFilter] = useState('all');
  const [genreFilter, setGenreFilter] = useState('all');
  const [showFilters, setShowFilters] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    localStorage.setItem('lastSelectedCity', city);
  }, [city]);

  useEffect(() => {
    fetchEvents();
  }, [sort]);

  const fetchEvents = async () => {
    try {
      setLoading(true);
      const data = await eventService.getEvents(null, null, sort || null);
      if (data.success) setEvents(data.data || []);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const filterByCategory = (list) => {
    if (category === 'all') return list;
    return list.filter((e) => e.eventType === category);
  };

  const filterBySearch = (list) => {
    if (!search.trim()) return list;
    const q = search.toLowerCase();
    return list.filter((e) => e.title.toLowerCase().includes(q));
  };

  const filterByLanguage = (list) => {
    if (languageFilter === 'all') return list;
    return list.filter((e) => e.language && e.language.toLowerCase().includes(languageFilter.toLowerCase()));
  };

  const filterByGenre = (list) => {
    if (genreFilter === 'all') return list;
    return list.filter((e) => e.genre && e.genre.toLowerCase().includes(genreFilter.toLowerCase()));
  };

  const applyAllFilters = (list) => {
    return filterByGenre(filterByLanguage(filterBySearch(filterByCategory(list))));
  };

  const availableLanguages = ['all', ...new Set(events.map(e => e.language).filter(Boolean))];
  const availableGenres = ['all', ...new Set(events.flatMap(e =>
    e.genre ? e.genre.split(',').map(g => g.trim()) : []
  ))];

  const nowShowing = applyAllFilters(events.filter((e) => e.status === 'NOW_SHOWING'));
  const comingSoon = applyAllFilters(events.filter((e) => e.status === 'COMING_SOON'));
  const trending = applyAllFilters([...events].sort((a, b) => (b.rating || 0) - (a.rating || 0)).slice(0, 10));

  const handleEventClick = (id) => navigate(`/events/${id}`);
  const handleLogout = () => {
    localStorage.removeItem('userId');
    localStorage.removeItem('user');
    localStorage.removeItem('userRole');
    navigate('/login');
  };

  const EventCard = ({ event }) => (
    <div className="event-card" onClick={() => handleEventClick(event.id)}>
      <div className="event-poster">
        <PosterImage
          src={event.posterUrl}
          alt={event.title}
          placeholderLetter={event.title ? event.title.charAt(0) : '?'}
          variant="card"
        />
      </div>
      <div className="event-info">
        <h3>{event.title}</h3>
        <p className="genre">{event.genre}</p>
        <p className="language">{event.language}</p>
        {event.rating != null && (
          <div className="rating">
            <span className="star">⭐</span> {event.rating}/10
          </div>
        )}
        <span className={`status ${(event.status || '').toLowerCase().replace('_', '')}`}>
          {(event.status || '').replace('_', ' ')}
        </span>
      </div>
    </div>
  );

  const Carousel = ({ title, list }) => (
    <section className="carousel-section">
      <h3 className="carousel-title">{title}</h3>
      <div className="carousel">
        {list.map((event) => (
          <EventCard key={event.id} event={event} />
        ))}
      </div>
    </section>
  );

  return (
    <div className="home">
      <nav className="navbar">
        <div className="nav-content">
          <h1>BookMyShow</h1>
          <div className="nav-right">
            <select
              className="city-select"
              value={city}
              onChange={(e) => setCity(e.target.value)}
              aria-label="Select city"
            >
              {CITIES.map((c) => (
                <option key={c} value={c}>{c}</option>
              ))}
            </select>
            <button type="button" className="nav-btn" onClick={() => navigate('/bookings')}>
              My Bookings
            </button>
            <button type="button" className="nav-btn logout" onClick={handleLogout}>
              Logout
            </button>
          </div>
        </div>
      </nav>

      <div className="container">
        <div className="header">
          <h2>Events in {city}</h2>
          <input
            type="text"
            className="search-input"
            placeholder="Search events..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
          <div className="sort-row">
            <label>Sort by:</label>
            <select className="sort-select" value={sort} onChange={(e) => setSort(e.target.value)}>
              <option value="">Default</option>
              <option value="rating">Rating</option>
              <option value="releaseDate">Release Date</option>
            </select>
          </div>
          <div className="category-tabs">
            {CATEGORIES.map(({ key, label }) => (
              <button
                key={key}
                type="button"
                className={`filter-btn ${category === key ? 'active' : ''}`}
                onClick={() => setCategory(key)}
              >
                {label}
              </button>
            ))}
          </div>

          {/* Advanced Filters */}
          <div className="advanced-filters">
            <button
              type="button"
              className="toggle-filters-btn"
              onClick={() => setShowFilters(!showFilters)}
            >
              {showFilters ? '▼' : '▶'} Filters
            </button>
            {showFilters && (
              <div className="filters-panel">
                <div className="filter-group">
                  <label>Language:</label>
                  <select
                    className="filter-select"
                    value={languageFilter}
                    onChange={(e) => setLanguageFilter(e.target.value)}
                  >
                    {availableLanguages.map((lang) => (
                      <option key={lang} value={lang}>
                        {lang === 'all' ? 'All Languages' : lang}
                      </option>
                    ))}
                  </select>
                </div>
                <div className="filter-group">
                  <label>Genre:</label>
                  <select
                    className="filter-select"
                    value={genreFilter}
                    onChange={(e) => setGenreFilter(e.target.value)}
                  >
                    {availableGenres.map((genre) => (
                      <option key={genre} value={genre}>
                        {genre === 'all' ? 'All Genres' : genre}
                      </option>
                    ))}
                  </select>
                </div>
                <button
                  type="button"
                  className="clear-filters-btn"
                  onClick={() => {
                    setLanguageFilter('all');
                    setGenreFilter('all');
                    setCategory('all');
                    setSearch('');
                  }}
                >
                  Clear All Filters
                </button>
              </div>
            )}
          </div>
        </div>

        {loading ? (
          <div className="loading">Loading events...</div>
        ) : (
          <>
            {nowShowing.length > 0 && <Carousel title="Now Showing" list={nowShowing} />}
            {comingSoon.length > 0 && <Carousel title="Coming Soon" list={comingSoon} />}
            {trending.length > 0 && <Carousel title="Trending" list={trending} />}
            {!nowShowing.length && !comingSoon.length && !trending.length && (
              <p className="no-events">No events match your filters.</p>
            )}
          </>
        )}
      </div>
    </div>
  );
}

export default Home;
