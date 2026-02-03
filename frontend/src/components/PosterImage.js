import React, { useState } from 'react';
import './PosterImage.css';


function normalizePosterUrl(url) {
  if (!url || typeof url !== 'string') return '';
  const trimmed = url.trim();
  if (!trimmed) return '';
  if (trimmed.startsWith('http://') && typeof window !== 'undefined' && window.location?.protocol === 'https:') {
    return trimmed.replace(/^http:\/\//i, 'https://');
  }
  return trimmed;
}

export default function PosterImage({ src, alt, className = '', placeholderLetter = '?', variant = 'card' }) {
  const [error, setError] = useState(false);
  const normalizedSrc = normalizePosterUrl(src);
  const showImg = normalizedSrc && !error;

  return (
    <div className={`poster-image-wrapper poster-image--${variant} ${className}`}>
      {showImg ? (
        <img
          src={normalizedSrc}
          alt={alt || 'Poster'}
          className="poster-image-img"
          referrerPolicy="no-referrer"
          onError={() => setError(true)}
        />
      ) : (
        <div className="poster-image-placeholder">
          <span>{placeholderLetter}</span>
        </div>
      )}
    </div>
  );
}
