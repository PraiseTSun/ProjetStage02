import React from 'react';
import { Col, Container, Row } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import LoginForm from './components/LoginForm';

function App() {
  return (
    <Container>
      <Row className="vh-100">
        <Col m-auto className="m-auto col-4 text-center">
          <h1 className="text-warning fw-bold text-center">OSE KILLER</h1>
          <LoginForm />
          <Link to="/register" className="link-warning">Nouveau utilisateur? Inscrivez vous ici.</Link>
        </Col>
      </Row>
    </Container>
  );
}

export default App;
