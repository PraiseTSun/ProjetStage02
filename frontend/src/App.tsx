import React from 'react';
import { Row, Col, Container } from 'react-bootstrap';
import LoginPage from './components/LoginForm';

function App() {
  return (
    <Container>
      <Row className="vh-100">
        <Col m-auto className="m-auto col-4">
          <h1 className="text-warning fw-bold text-center">OSE KILLER</h1>
          <LoginPage />
        </Col>
      </Row>
    </Container>
  );
}

export default App;
