import React from "react";
import { useState, useEffect } from "react";
import { Form, Row, Col } from "react-bootstrap";

const LoginForm = (props: { setUser: Function }): JSX.Element => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [isInvalidLoggin, setIsInvalidLoggin] = useState("");

    const onSubmit = (event: React.SyntheticEvent): void => {
        event.preventDefault
        //TODO
    }

    return (
        <Form onSubmit={onSubmit}>
            <Form.Group>
                <Form.Label className="text-white fw-bold h5">Adresse courriel</Form.Label>
                <Form.Control type="email" required value={email} onChange={field => setEmail(field.target.value)}></Form.Control>
            </Form.Group>
            <Form.Group>
                <Form.Label className="text-white fw-bold mt-2 h5">Mot de passe</Form.Label>
                <Form.Control type="password" required value={password} onChange={field => setPassword(field.target.value)}></Form.Control>
            </Form.Group>
            {isInvalidLoggin ? <h5 className="text-danger fw-bold text-center mt-2">Courriel ou mot de passe invalide!</h5> : <></>}
            <Row className="mt-3">
                <Col className="text-center">
                    <Form.Control type="submit" value="Connecter" className="btn btn-success mx-auto w-75"></Form.Control>
                </Col>
            </Row>
        </Form>
    );
}

export default LoginForm;