import React from "react";
import { useState, useEffect } from "react";
import { Form, Row, Col, ToggleButton, ToggleButtonGroup } from "react-bootstrap";

const LoginForm = (props: { setUser: Function }): JSX.Element => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [userType, setUserType] = useState("student")
    const [isInvalidLoggin, setIsInvalidLoggin] = useState(false);

    const onSubmit = (event: React.SyntheticEvent): void => {
        event.preventDefault();

        const headers = {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ "email": email, "password": password })
        };
        fetch("http://localhost:8080/" + userType, headers)
            .then(response => {
                if (response.ok) return response.json()
                else setIsInvalidLoggin(true)
            })
            .then(data => {
                if (data) props.setUser(data.firstName)
            });

        setEmail("");
        setPassword("");
        setUserType("student");
    }

    return (
        <Form onSubmit={onSubmit}>
            <ToggleButtonGroup className="d-flex" name="userType" type="radio" value={userType} onChange={field => setUserType(field)}>
                <ToggleButton className="w-100" id="1" variant="info" value="student">Étudiant</ToggleButton>
                <ToggleButton className="w-100" id="2" variant="info" value="company">Entreprise</ToggleButton>
                <ToggleButton className="w-100" id="3" variant="info" value="gestionnaire">Gestionnaire</ToggleButton>
            </ToggleButtonGroup>
            <Row>
                <Col className="px-4 pb-2 pt-1">
                    <Form.Group>
                        <Form.Label className="fw-bold h5">Adresse courriel</Form.Label>
                        <Form.Control type="email" required value={email} onChange={field => setEmail(field.target.value)}></Form.Control>
                    </Form.Group>
                    <Form.Group>
                        <Form.Label className="fw-bold mt-2 h5">Mot de passe</Form.Label>
                        <Form.Control type="password" required value={password} onChange={field => setPassword(field.target.value)}></Form.Control>
                    </Form.Group>
                    {isInvalidLoggin ? <h5 className="text-danger fw-bold text-center mt-2">Courriel ou mot de passe invalide!</h5> : <></>}
                    <Row className="mt-3">
                        <Col className="text-center">
                            <Form.Control type="submit" value="Connecter" className="btn btn-success mx-auto w-75"></Form.Control>
                        </Col>
                    </Row>
                </Col>
            </Row>
        </Form>
    );
}

export default LoginForm;