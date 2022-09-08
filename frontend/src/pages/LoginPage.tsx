import React from "react";
import { Form, Button, Row, Col } from "react-bootstrap";

function LoginPage() {
    return (
        <Form>
            <Form.Group>
                <Form.Label>Adresse courriel</Form.Label>
                <Form.Control type="email"></Form.Control>
            </Form.Group>
            <Form.Group>
                <Form.Label>Mot de passe</Form.Label>
                <Form.Control type="password"></Form.Control>
            </Form.Group>
            <Row className="mt-3">
                <Col>
                    <Button className="btn-success" type="submit">
                        Connecter
                    </Button>
                </Col>
            </Row>
        </Form>
    );
}

export default LoginPage;