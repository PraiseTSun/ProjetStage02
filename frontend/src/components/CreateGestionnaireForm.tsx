import React, {useState} from "react";
import {Col, Form, Row} from "react-bootstrap";

const ValidationGestionnaire = () => {
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

    const validEmail = new RegExp(
        '^[a-zA-Z0-9._:$!%-]+@[a-zA-Z0-9.-]+.[a-zA-Z]$'
    );

    const onSubmit = async (event: React.SyntheticEvent) => {
        event.preventDefault();

        if (password !== confirmPassword) {
            alert("La confirmation du mot de passe n'est pas pareille.")
            return;
        }

        if (!validEmail.test(email)) {
            alert("La syntax du courriel est mauvaise.");
            return;
        }


        const res = await fetch(`http://localhost:8080/createGestionnaire`,
            {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    "firstName" : firstName,
                    "lastName" : lastName,
                    "email" : email,
                    "password" : password
                })
            });

        if (res.status == 409) {
            const data = await res.json();
            alert(data.error);
        }

        if (res.status == 201) {
            alert("Utilisateur a été créé.");
            setFirstName("");
            setLastName("");
            setEmail("");
            setPassword("");
            setConfirmPassword("")
        }
    }

    return (
        <Form onSubmit={onSubmit}>
            <Col className="bg-light px-4 pb-2 pt-1">
                <Row>
                    <Col>
                        <Form.Group>
                            <Form.Label className="fw-bold h5">Prénom</Form.Label>
                            <Form.Control type="text" required value={firstName} onChange={field => setFirstName(field.target.value)}></Form.Control>
                        </Form.Group>
                    </Col>
                    <Col>
                        <Form.Group>
                            <Form.Label className="fw-bold h5">Nom de famille</Form.Label>
                            <Form.Control type="text" required value={lastName} onChange={field => setLastName(field.target.value)}></Form.Control>
                        </Form.Group>
                    </Col>
                </Row>
                <Row>
                    <Form.Group>
                        <Form.Label className="fw-bold mt-2 h5">Courriel</Form.Label>
                        <Form.Control type="email" required value={email} onChange={field => setEmail(field.target.value)}></Form.Control>
                    </Form.Group>
                </Row>
                <Row>
                    <Col>
                        <Form.Group>
                            <Form.Label className="fw-bold mt-2 h5">Mot de passe</Form.Label>
                            <Form.Control type="password" required value={password} onChange={field => setPassword(field.target.value)}></Form.Control>
                        </Form.Group>
                    </Col>
                    <Col>
                        <Form.Group>
                            <Form.Label className="fw-bold mt-2 h5">Confirmer le mot de passe</Form.Label>
                            <Form.Control type="password" required value={confirmPassword} onChange={field => setConfirmPassword(field.target.value)}></Form.Control>
                        </Form.Group>
                    </Col>
                </Row>
                <Row className="mt-3">
                    <Col className="text-center">
                        <Form.Control type="submit" value="Enregistrer" className="btn btn-success mx-auto"></Form.Control>
                    </Col>
                </Row>
            </Col>
        </Form>
    );
}

export default ValidationGestionnaire;