import React, {useState} from "react";
import {Col, Form, Row} from "react-bootstrap";

const ValidationGetionnaire = () => {
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");


    const onSubmit = (event: React.SyntheticEvent): void => {
        event.preventDefault();

        setFirstName("");
        setLastName("");
        setEmail("");
        setPassword("")
    }

    return (
        <Form onSubmit={onSubmit}>
            <Col className="px-4 pb-2 pt-1">
                <Row>
                    <Col>
                        <Form.Group>
                            <Form.Label className="fw-bold h5">Prénom</Form.Label>
                            <Form.Control type="email" required value={firstName} onChange={field => setFirstName(field.target.value)}></Form.Control>
                        </Form.Group>
                    </Col>
                    <Col>
                        <Form.Group>
                            <Form.Label className="fw-bold h5">Nom de famille</Form.Label>
                            <Form.Control type="password" required value={lastName} onChange={field => setLastName(field.target.value)}></Form.Control>
                        </Form.Group>
                    </Col>
                </Row>
                <Form.Group>
                    <Form.Label className="fw-bold mt-2 h5">Email</Form.Label>
                    <Form.Control type="password" required value={email} onChange={field => setEmail(field.target.value)}></Form.Control>
                </Form.Group>
                <Form.Group>
                    <Form.Label className="fw-bold mt-2 h5">Mot de passe</Form.Label>
                    <Form.Control type="password" required value={password} onChange={field => setPassword(field.target.value)}></Form.Control>
                </Form.Group>
                {/*{isInvalidLoggin ? <h5 className="text-danger fw-bold text-center mt-2">Courriel ou mot de passe invalide!</h5> : <></>}*/}
                <Row className="mt-3">
                    <Col className="text-center">
                        <Form.Control type="submit" value="Connecter" className="btn btn-success mx-auto w-75"></Form.Control>
                    </Col>
                </Row>
            </Col>
        </Form>
    );
}

export default ValidationGetionnaire;