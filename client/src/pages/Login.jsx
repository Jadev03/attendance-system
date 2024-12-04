import React, { useState, useRef } from 'react';
import { TextField, Button, Container, Box, Typography } from '@mui/material';
import Avatar from '@mui/material/Avatar';
import Stack from '@mui/material/Stack';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const usernameRef = useRef(null);
  const passwordRef = useRef(null);
  const navigate = useNavigate();

  const handleLogin = async () => {
    setError('');

    // Username validation
    const usernameRegex = /^[A-Za-z]+$/;
    if (!username) {
      usernameRef.current.focus();
      setError('Username is required');
      return;
    }
    if (!usernameRegex.test(username)) {
      usernameRef.current.focus();
      setError('Username must contain only alphabets');
      return;
    }

    // Password validation
    const passwordRegex = /^(?=.*[A-Z])(?=.*[!@#$%^&*])(?=.*[0-9])(?=.*[a-z]).{8,}$/;
    if (!password) {
      passwordRef.current.focus();
      setError('Password is required');
      return;
    }
    if (!passwordRegex.test(password)) {
      passwordRef.current.focus();
      setError(
        'Password must be at least 8 characters long, include one uppercase letter, one special character, and one number'
      );
      return;
    }

    try {
      const response = await axios.post('http://localhost:8080/login', {
        username,
        password,
      });

      // Backend response handling
      if (response.status === 200) {
        const { message, token } = response.data; // Destructure backend response

        localStorage.setItem('token', token); // Store JWT in localStorage
        console.log(message); // Optionally log the success message
        navigate('/main'); // Navigate to the main page
      } else {
        setError('Username or password is incorrect');
      }
    } catch (error) {
      setError('Error logging in. Please try again later.');
      console.error('Login error:', error);
    }
  };

  return (
    <Container
      maxWidth="sm"
      sx={{
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        paddingTop: '100px',
      }}
    >
      <Box
        display="flex"
        flexDirection="column"
        alignItems="center"
        justifyContent="center"
        width="80%"
        height="70vh"
        sx={{
          border: '1px solid #ccc',
          padding: '20px',
          borderRadius: '8px',
          boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
        }}
      >
        <h2 className="text-3xl font-sansmat font-semibold">ABC School</h2>
        <h2 className="text-xl font-sansmat font-semibold">Attendance</h2>
        <Stack>
          <Avatar
            alt="logo"
            src="./images.png"
            sx={{ width: 100, height: 100, border: '1px solid #ccc' }}
          />
        </Stack>

        <TextField
          inputRef={usernameRef}
          type="text"
          label="Username"
          variant="outlined"
          fullWidth
          margin="normal"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />

        <TextField
          inputRef={passwordRef}
          label="Password"
          type="password"
          variant="outlined"
          fullWidth
          margin="normal"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />

        <Button
          variant="contained"
          color="primary"
          onClick={handleLogin}
          fullWidth
          style={{ marginTop: '10px' }}
        >
          Login
        </Button>

        {error && (
          <Typography
            variant="body2"
            color="error"
            align="center"
            sx={{ marginTop: '10px' }}
          >
            {error}
          </Typography>
        )}
      </Box>
    </Container>
  );
};

export default Login;
