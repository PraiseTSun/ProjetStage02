import React from 'react';
import { useState } from 'react';
import { Container } from 'react-bootstrap';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import FormulaireSoumission from './components/FormulaireSoumission';
import LoginPage from './pages/LoginPage';
import Dashboard from './pages/Dashboard';
import IUser from './models/IUser';

function App() {
    const emptyUser:IUser = {   
      firstName:"",
      lastName:"",
      userType:""
  }
  const [user, setUser] = useState(emptyUser)

  if (user == emptyUser) {
    return <LoginPage setUser={setUser} />;
  }

  return (
    <Container className="vh-100">
      <BrowserRouter>
        <Routes>
          <Route path="/" element= {<Dashboard user={user}/>}
           />
        </Routes>
      </BrowserRouter>
    </Container>
  );
}

export default App;
