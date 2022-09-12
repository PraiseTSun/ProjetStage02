import React from 'react';
import { Col, Container, Row } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import LoginForm from './components/LoginForm';

function App() {
  return (
    <Container>
      <Row className="vh-100">
        <Col className="m-auto col-lg-4 col-md-6">
          <h1 className="text-warning fw-bold text-center display-3">OSE KILLER</h1>
          <LoginForm />
          <Row>
            <Col className="text-center">
              <Link to="/register" className="link-warning">Nouveau utilisateur? Inscrivez vous ici.</Link>
            </Col>
          </Row>
        </Col>
      </Row>
    </Container>
  );
}

export default App;
