import React, {useState} from "react";
import {Button, Col, Form, Row, ToggleButton, ToggleButtonGroup} from "react-bootstrap";
import {BeatLoader} from "react-spinners";
import {LOCAL_STORAGE_KEY} from '../../App';
import IUser from '../../models/IUser';
import {postUserTypeLogin, putUserType} from "../../services/universalServices/UniversalFetchService";

const LoginForm = (props: { setUser: Function }): JSX.Element => {
    const [waiting, setWaiting] = useState(false);
    const [validated, setValidated] = useState(false);
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [userType, setUserType] = useState("student")
    const [isInvalidLoggin, setIsInvalidLoggin] = useState(false);

    const onSubmit = async (event: React.SyntheticEvent) => {
        const form: any = event.currentTarget;
        event.preventDefault();

        if (form.checkValidity()) {
            setWaiting(true)
            const tokenRes = await postUserTypeLogin(userType, email, password)
            if (tokenRes.ok) {
                const tokenData = await tokenRes.json()
                const res = await putUserType(userType, tokenData.token)
                if (res.ok) {
                    const data = await res.json()
                    const user: IUser = {
                        id: data.id,
                        firstName: data.firstName,
                        lastName: data.lastName,
                        userType: userType,
                        token: tokenData.token,
                        cvToValidate: data.cvToValidate,
                        cv: data.cv,
                        companyName: data.companyName
                    }
                    props.setUser(user)
                    localStorage.setItem(LOCAL_STORAGE_KEY, JSON.stringify(user))
                } else setIsInvalidLoggin(true)
            } else setIsInvalidLoggin(true)
        }
        setWaiting(false)
        setValidated(true);
    }

    if (waiting) {
        return <BeatLoader className="text-center" color="#0275d8" size={100}/>
    }

    return (
        <Form onSubmit={onSubmit} validated={validated} noValidate>
            <ToggleButtonGroup className="d-flex" name="userType" type="radio" value={userType} onChange={field => {
                setUserType(field)
                setValidated(false)
            }}>
                <ToggleButton className="w-100" id="1" variant="primary" value="student">Étudiant</ToggleButton>
                <ToggleButton className="w-100" id="2" variant="primary" value="company">Entreprise</ToggleButton>
                <ToggleButton className="w-100" id="3" variant="primary"
                              value="gestionnaire">Gestionnaire</ToggleButton>
            </ToggleButtonGroup>
            <Row>
                <Col className="px-4 pb-2 pt-1">
                    <Form.Group>
                        <Form.Label className="fw-bold h5">Adresse courriel</Form.Label>
                        <Form.Control data-testid="courreielLoginForm" type="email" required value={email}
                                      onChange={field => setEmail(field.target.value)}></Form.Control>
                        <Form.Control.Feedback type="invalid">Courriel invalide</Form.Control.Feedback>
                    </Form.Group>
                    <Form.Group>
                        <Form.Label data-testid="labelmotDePasseLoginForm" className="fw-bold mt-2 h5">Mot de
                            passe</Form.Label>
                        <Form.Control data-testid="motDePasseLoginForm" type="password" required value={password}
                                      minLength={8} onChange={field => setPassword(field.target.value)}></Form.Control>
                        <Form.Control.Feedback type="invalid">Mot de passe plus petit que 8
                            caractères</Form.Control.Feedback>
                    </Form.Group>
                    {isInvalidLoggin ?
                        <h5 className="text-danger fw-bold text-center mt-2">Aucun compte n'existe avec ce courriel et
                            mot de passe</h5> : <></>}
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