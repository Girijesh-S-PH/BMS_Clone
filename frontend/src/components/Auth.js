import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { authService } from '../services/api';
import './Auth.css';

export default function Auth() {
  const navigate = useNavigate();
  const [isLogin, setIsLogin] = useState(true);
  const [formData, setFormData] = useState({
    phoneNumber: '',
    password: '',
    fullName: '',
    email: '',
  });
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setMessage('');

    try {
      let result;
      if (isLogin) {
        result = await authService.login(formData.phoneNumber, formData.password);
      } else {
        result = await authService.signup({
          phoneNumber: formData.phoneNumber,
          password: formData.password,
          fullName: formData.fullName,
          email: formData.email,
          captchaToken: 'test-token',
        });
      }

      if (result.success) {
        if (result.data && result.data.userId) {
          localStorage.setItem('userId', String(result.data.userId));
          localStorage.setItem('user', JSON.stringify(result.data));
          localStorage.setItem('userRole', (result.data.role || '').toUpperCase());

          const role = (result.data.role || '').toUpperCase();
          if (role === 'ADMIN') {
            navigate('/admin/dashboard');
          } else {
            navigate('/');
          }
        } else {
          setMessage(`Success! ${result.message}`);
        }
      } else {
        setMessage(`Error: ${result.message}`);
      }
    } catch (error) {
      const msg = error.message === 'Failed to fetch'
        ? 'Cannot reach server. Start the backend: cd backend && java -jar target/bms-backend-1.0.0.jar'
        : `Error: ${error.message}`;
      setMessage(`Error: ${msg}`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-box">
        <h1>{isLogin ? 'Login' : 'Signup'}</h1>
        {message && (
          <div className={`message ${message.includes('Error') ? 'error' : 'success'}`}>
            {message}
          </div>
        )}
        <form onSubmit={handleSubmit}>
          <input
            type="text"
            name="phoneNumber"
            placeholder="Phone Number"
            value={formData.phoneNumber}
            onChange={handleChange}
            required
          />
          <input
            type="password"
            name="password"
            placeholder="Password"
            value={formData.password}
            onChange={handleChange}
            required
          />
          {!isLogin && (
            <>
              <input
                type="text"
                name="fullName"
                placeholder="Full Name"
                value={formData.fullName}
                onChange={handleChange}
                required
              />
              <input
                type="email"
                name="email"
                placeholder="Email"
                value={formData.email}
                onChange={handleChange}
                required
              />
            </>
          )}
          <button type="submit" disabled={loading}>
            {loading ? 'Loading...' : isLogin ? 'Login' : 'Signup'}
          </button>
        </form>
        <p>
          {isLogin ? "Don't have an account? " : 'Already have an account? '}
          <button
            type="button"
            className="toggle-btn"
            onClick={() => setIsLogin(!isLogin)}
          >
            {isLogin ? 'Signup' : 'Login'}
          </button>
        </p>
      </div>
    </div>
  );
}
