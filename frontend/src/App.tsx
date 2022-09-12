import React from 'react';
import { Row, Col, Container } from 'react-bootstrap';
import LoginPage from './components/LoginForm';

function App() {
  return (
    <Container>
      <Row className="vh-100">
        <Col m-auto className="m-auto col-4 text-center">
          <h1 className="text-warning fw-bold text-center">OSE KILLER</h1>
          <LoginPage />
          <a href="/register" className="link-warning">Nouveau utilisateur? Inscrivez vous ici.</a>
        </Col>
      </Row>
    </Container>
  );
}

export default App;
