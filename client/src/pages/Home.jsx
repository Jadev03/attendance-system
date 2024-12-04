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

      if (response.data.status === 'success') {
        localStorage.removeItem('token'); 
        navigate('/'); 
      } else {
        console.error('Failed to log out:', response.data.message);
      }
    } catch (error) {
      console.error('Logout error:', error.message);
      alert('Failed to log out. Please try again.'); 
    }
  };

  return (
    <div className="flex justify-between items-center border border-white p-4">
      <header className="text-3xl">ABC SCHOOL</header>
      <button
        className="bg-blue-500 h-10 w-20 text-white"
        onClick={handleLogout}
      >
        Logout
      </button>
    </div>
  );
};

export default Head;
