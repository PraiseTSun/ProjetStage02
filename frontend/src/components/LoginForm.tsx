import React from "react";
import { Form, Button, Row, Col } from "react-bootstrap";

function LoginForm() {
    return (
        <Form>
            <Form.Group>
                <Form.Label className="text-white fw-bold h5">Adresse courriel</Form.Label>
                <Form.Control type="email" required></Form.Control>
            </Form.Group>
            <Form.Group>
                <Form.Label className="text-white fw-bold mt-2 h5">Mot de passe</Form.Label>
                <Form.Control type="password" required></Form.Control>
            </Form.Group>
            <Row className="mt-3">
                <Col className="text-center">
                    <Button className="btn-success mx-auto w-75" type="submit">
                        Connecter
                    </Button>
                </Col>
            </Row>
        </Form>
    );
}

export default LoginForm;