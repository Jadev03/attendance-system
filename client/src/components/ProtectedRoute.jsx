import React, { useState, useEffect } from 'react';
import { Navigate } from 'react-router-dom';
import axios from 'axios';

const ProtectedRoute = ({ children }) => {
  const [isTokenValid, setIsTokenValid] = useState(null);

  useEffect(() => {
    const validateToken = async () => {
      const token = localStorage.getItem('token');
      if (!token) {
        setIsTokenValid(false);
        return;
      }

      try {
        const response = await axios.post(
          'http://localhost:8080/validate',
          { token });

        if (response.status===200) {
          setIsTokenValid(true);
        } else {
         localStorage.removeItem('token');
          setIsTokenValid(false);
        }
      } catch (error) {
        console.error('Error validating token:', error);
        setIsTokenValid(false);
      }
    };

    validateToken();
  }, []);

  if (isTokenValid === null) {
    // Render a loader or nothing while the token validation is in progress
    return <div>Loading...</div>;
  }

  return isTokenValid ? children : <Navigate to="/" replace />;
};

export default ProtectedRoute;
