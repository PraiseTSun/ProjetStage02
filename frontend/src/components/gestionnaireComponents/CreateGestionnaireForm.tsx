import React, {useState} from "react";
import {Button, Col, Form, Row} from "react-bootstrap";
import {BeatLoader} from "react-spinners";
import IUser from "../../models/IUser";
import {postCreateGestionnaire} from "../../services/gestionnaireServices/GestionnaireFetchService";

const CreateGestionnaireForm = ({connectedUser}: { connectedUser: IUser }) => {
    const [waiting, setWaiting] = useState(false);
    const [validated, setValidated] = useState(false);
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [passwordsMatch, setPasswordsMatch] = useState<boolean>(true);
    const [emailValid, setEmailValid] = useState<boolean>(true);
    const [accountCreated, setAccountCreated] = useState<boolean>(false);
    const [accountCreationError, setAccountCreationError] = useState<string>("");

    const validEmail = new RegExp(
        '^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$'
    );

    const onSubmit = async (event: React.SyntheticEvent) => {
        const form: any = event.currentTarget;
        event.preventDefault();
        
        setValidated(true)
        if (form.checkValidity()) {
            if (password !== confirmPassword) {
                setPasswordsMatch(false)
                return;
            } else {
                setPasswordsMatch(true)
            }

            console.log(validEmail.test(email))
            console.log(email)

            if (!validEmail.test(email)) {
                setEmailValid(false)
                return;
            } else {
                setEmailValid(true)
            }

            setWaiting(true)
            const res = await postCreateGestionnaire(firstName, lastName, email, password, connectedUser.token)

            if (!res.ok) {
                const data = await res.json();
                setAccountCreationError(data.error)
            } else {
                setEmailValid(true)
                setPasswordsMatch(true)
                setAccountCreated(true)
                setFirstName("")
                setLastName("")
                setEmail("")
                setPassword("")
                setConfirmPassword("")
            }
            setWaiting(false)
            setValidated(false)
        }
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

                            <Form.Control.Feedback type="invalid">Champ requis</Form.Control.Feedback>
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
                {
                    accountCreated &&
                    <p className="text-success fw-bold text-center h3">Compte créé</p>}
                {
                    accountCreationError !== "" &&
                    <p className="text-danger fw-bold text-center h3">{accountCreationError}</p>}
                {!passwordsMatch &&
                    <p className="text-danger fw-bold text-center h3">Les mots de passe ne sont pas pareils</p>}
                {!emailValid &&
                    <p className="text-danger fw-bold text-center h3">Le courriel est invalide</p>}
                {
                    waiting
                        ? <BeatLoader color="#0275d8" className="text-center" size={25}/>
                        : <Row className="mt-3">
                            <Button type="submit" className="btn btn-success mx-auto">Enregistrer</Button>
                        </Row>
                }
            </Col>
        </Form>
    );
}

export default CreateGestionnaireForm;