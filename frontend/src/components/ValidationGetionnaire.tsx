import React, {useState} from "react";
import {Col, Form, Row} from "react-bootstrap";

const ValidationGetionnaire = () => {
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");


    const onSubmit = (event: React.SyntheticEvent): void => {
        event.preventDefault();

        setFirstName("");
        setLastName("");
        setEmail("");
        setPassword("");
        setConfirmPassword("")
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
                        <Form.Label className="fw-bold mt-2 h5">Email</Form.Label>
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
                            <Form.Label className="fw-bold mt-2 h5">Confirme mot de passe</Form.Label>
                            <Form.Control type="password" required value={confirmPassword} onChange={field => setConfirmPassword(field.target.value)}></Form.Control>
                        </Form.Group>
                    </Col>
                </Row>
                {/*{isInvalidLoggin ? <h5 className="text-danger fw-bold text-center mt-2">Courriel ou mot de passe invalide!</h5> : <></>}*/}
                <Row className="mt-3">
                    <Col className="text-center">
                        <Form.Control type="submit" value="Connecter" className="btn btn-success mx-auto"></Form.Control>
                    </Col>
                </Row>
            </Col>
        </Form>
    );
}

export default ValidationGetionnaire;