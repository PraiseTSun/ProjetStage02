import React from "react";
import { useState } from "react";
import { Form, Row, Col, ToggleButton, ToggleButtonGroup, Button } from "react-bootstrap";

const LoginForm = (props: { setUser: Function }): JSX.Element => {
    const [validated, setValidated] = useState(false);
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [userType, setUserType] = useState("student")
    const [isInvalidLoggin, setIsInvalidLoggin] = useState(false);

    const onSubmit = (event: React.SyntheticEvent): void => {
        const form: any = event.currentTarget;
        event.preventDefault();

        if (form.checkValidity() === false) {
            event.stopPropagation();
        }
        else {
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
                    if (data) props.setUser({
                        firstName: data.firstName,
                        lastName: data.lastName,
                        userType: userType,
                    })
                });
        }

        setValidated(true);



    }

    return (
        <Form onSubmit={onSubmit} validated={validated} noValidate>
            <ToggleButtonGroup className="d-flex" name="userType" type="radio" value={userType} onChange={field => {
                setUserType(field)      
                setValidated(false)      
            }}>
                <ToggleButton className="w-100" id="1" variant="primary" value="student">Étudiant</ToggleButton>
                <ToggleButton className="w-100" id="2" variant="primary" value="company">Entreprise</ToggleButton>
                <ToggleButton className="w-100" id="3" variant="primary" value="gestionnaire">Gestionnaire</ToggleButton>
            </ToggleButtonGroup>
            <Row>
                <Col className="px-4 pb-2 pt-1">
                    <Form.Group>
                        <Form.Label className="fw-bold h5">Adresse courriel</Form.Label>
                        <Form.Control type="email" required value={email} onChange={field => setEmail(field.target.value)}></Form.Control>
                        <Form.Control.Feedback type="invalid">Courriel invalide</Form.Control.Feedback>
                    </Form.Group>
                    <Form.Group >
                        <Form.Label className="fw-bold mt-2 h5">Mot de passe</Form.Label>
                        <Form.Control type="password" required value={password} onChange={field => setPassword(field.target.value)}></Form.Control>
                        <Form.Control.Feedback type="invalid">Mot de passe invalide</Form.Control.Feedback>
                    </Form.Group>
                    {isInvalidLoggin ? <h5 className="text-danger fw-bold text-center mt-2">Aucun compte n'existe avec ce courriel et mot de passe</h5> : <></>}
                    <Row className="mt-3">
                        <Col className="text-center">
                            <Button type="submit" className="btn btn-success mx-auto w-75">Connecter</Button>
                        </Col>
                    </Row>
                </Col>
            </Row>
        </Form>
    );
}

export default LoginForm;