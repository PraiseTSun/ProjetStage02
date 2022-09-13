import React from 'react';
import { useState } from 'react';
import { Container } from 'react-bootstrap';
import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import LoginPage from './pages/LoginPage';

function App() {
  const [user, setUser] = useState("")

  if (!user || window.location.href != "/register") {
    return <LoginPage setUser={setUser} />;
  }

  return (
    <Container className="vh-100">
      <BrowserRouter>
        <Routes>
          <Route path="/dashboard" element={<h1>Dashboard</h1>} />
        </Routes>
      </BrowserRouter>
    </Container>
  );
}

export default App;
