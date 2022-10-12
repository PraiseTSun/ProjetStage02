import React, {useState} from "react";
import {Button, Col, Form, Row} from "react-bootstrap";
import {BeatLoader} from "react-spinners";
import IUser from "../models/IUser";

const ValidationGestionnaire = ({user}:{user:IUser}) => {
    const [waiting, setWaiting] = useState(false);
    const [validated, setValidated] = useState(false);
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

    const validEmail = new RegExp(
        '^[a-zA-Z0-9._:$!%-]+@[a-zA-Z0-9.-]+.[a-zA-Z]$'
    );

    const onSubmit = async (event: React.SyntheticEvent) => {
        const form: any = event.currentTarget;
        event.preventDefault();
        if (form.checkValidity()) {
            setWaiting(true)
            if (password !== confirmPassword) {
                alert("La confirmation du mot de passe n'est pas pareille.")
                setWaiting(false)
                return;
            }

            if (!validEmail.test(email)) {
                alert("La syntax du courriel est mauvaise.");
                setWaiting(false)
                return;
            }


            const res = await fetch(`http://localhost:8080/createGestionnaire`,
                {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        "firstName": firstName,
                        "lastName": lastName,
                        "email": email,
                        "password": password,
                        "token":user.token
                    })
                });

            if (!res.ok) {
                const data = await res.json();
                alert(data.error);
            }else {
                alert("Le courriel de confirmation à été envoyé.");
            }
            setWaiting(false)
            setValidated(false)
            setFirstName("")
            setLastName("")
            setEmail("")
            setPassword("")
            setConfirmPassword("")
        }
        setValidated(true)
    }

    if (waiting) {
        return (
            <div className="d-flex justify-content-center py-5 bg-light">
                <BeatLoader className="text-center" color="#292b2c" size={100} />
            </div>
                );
    }

    return (
        <Form onSubmit={onSubmit} validated={validated} noValidate>
            <Col className="bg-light px-4 pb-2 pt-1">
                <Row>
                    <Col>
                        <Form.Group>
                            <Form.Label className="fw-bold h5">Prénom</Form.Label>
                            <Form.Control type="text" minLength={2} required value={firstName}  
                                          onChange={field => setFirstName(field.target.value)}></Form.Control>

                            <Form.Control.Feedback type="invalid">Champ requis</Form.Control.Feedback>
                        </Form.Group>
                    </Col>
                    <Col>
                        <Form.Group>
                            <Form.Label className="fw-bold h5">Nom de famille</Form.Label>
                            <Form.Control minLength={2} type="text" required value={lastName}
                                          onChange={field => setLastName(field.target.value)}></Form.Control>

                            <Form.Control.Feedback  type="invalid">Champ requis</Form.Control.Feedback>
                        </Form.Group>
                    </Col>
                </Row>
                <Row>
                    <Form.Group>
                        <Form.Label className="fw-bold mt-2 h5">Courriel</Form.Label>
                        <Form.Control type="email" required value={email}
                                      onChange={field => setEmail(field.target.value)}></Form.Control>

                        <Form.Control.Feedback type="invalid">Courriel invalide</Form.Control.Feedback>
                    </Form.Group>
                </Row>
                <Row>
                    <Col>
                        <Form.Group>
                            <Form.Label className="fw-bold mt-2 h5">Mot de passe</Form.Label>
                            <Form.Control type="password" required minLength={8} value={password} 
                            onChange={field => setPassword(field.target.value)}></Form.Control>

                            <Form.Control.Feedback type="invalid">Longueur minimum de 8 requise</Form.Control.Feedback>
                        </Form.Group>
                    </Col>
                    <Col>
                        <Form.Group>
                            <Form.Label className="fw-bold mt-2 h5">Confirmer le mot de passe</Form.Label>
                            <Form.Control type="password" minLength={8} required value={confirmPassword} 
                            onChange={field => setConfirmPassword(field.target.value)}></Form.Control>

                        </Form.Group>
                    </Col>
                </Row>
                <Row className="mt-3">
                    <Button type="submit" className="btn btn-success mx-auto">Enregistrer</Button>

                </Row>
            </Col>
        </Form>
    );
}

export default ValidationGestionnaire;