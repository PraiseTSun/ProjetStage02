import React from 'react';
import { useState } from 'react';
import { Container } from 'react-bootstrap';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import UserValidation from './pages/UserValidation';

function App() {
  const [user, setUser] = useState("")

  return <UserValidation/>
  if (!user) {
    return <LoginPage setUser={setUser} />;
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
