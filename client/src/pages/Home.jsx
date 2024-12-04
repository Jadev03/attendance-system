import React from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const Head = () => {
  const navigate = useNavigate();

  const handleLogout = async () => {
    const token = localStorage.getItem('token');
    if (!token) {
      navigate('/'); 
      return;
    }

    try {
      const response = await axios.post(
        'http://localhost:8080/logout',
        {"token":token},
        
      );

      if (response.status === 200) {
              localStorage.removeItem('token');
              alert('Logout successful');
              navigate('/'); // Redirect to login page
            } else if (response.status === 401) {
              alert('Invalid or expired token');
              localStorage.removeItem('token');
              navigate('/'); // Redirect to login page
            } else {
              alert('Failed to log out. Please try again.');
            }
    } catch (error) {
      console.error('Logout error:', error.message);
      alert('Failed to log out. Please try again.'); 
    }
  };

  return (
    <div>


    <div className="flex justify-between items-center border border-white p-4">
    
      <header className="text-3xl">ABC SCHOOL-Grade 10</header>
      <button
        className="bg-blue-500 h-10 w-20 text-white"
        onClick={handleLogout}
      >
        Logout
      </button>
    </div>
    <div>
      WELCOME
    </div>
    </div>
  );
};

export default Head;
