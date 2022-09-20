import React from 'react';
import { useState } from 'react';
import { Container } from 'react-bootstrap';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import ConfirmationPage from './pages/ConfirmationPage';
import LoginPage from './pages/LoginPage';
import UserValidation from './pages/UserValidation';

function App() {
  const [user, setUser] = useState("")

  if (!user) {
    return (
      <Container className="vh-100">
        <BrowserRouter>
          <Routes>
            <Route path="/" element={<LoginPage setUser={setUser} />} />
            <Route path="/confirmEmail/:id" element={<ConfirmationPage />} />
          </Routes>
        </BrowserRouter>
      </Container>
    );
  }

  return (
    <Container className="vh-100">
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<h1>Dashboard</h1>} />
        </Routes>
      </BrowserRouter>
    </Container>
  );
}

export default App;
