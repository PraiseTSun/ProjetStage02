import React, { useState } from "react";
import { Col, Row } from "react-bootstrap";
import InscriptionForm from "../components/InscriptionForm";
import LoginForm from "../components/LoginForm";

const LoginPage = (props: { setUser: Function }): JSX.Element => {
    const [isLogginPage, setIsLogginPage] = useState(true)

    return (
        <Row className="vh-100">
            <Col className="m-auto col-lg-4 col-md-6">
                {isLogginPage ? <h1 className="text-warning fw-bold text-center display-3">OSE KILLER</h1> : <></>}
                <Row>
                    <Col className="bg-white rounded p-0">
                        {isLogginPage ? <LoginForm setUser={props.setUser} /> : <InscriptionForm setIsLogginPage={setIsLogginPage} />}
                        <Row>
                            <Col className="text-center mb-2">
                                <a href="#" className="link-warning" onClick={() => setIsLogginPage(!isLogginPage)}>
                                    {isLogginPage ? "Nouveau utilisateur? Inscrivez vous ici." : "Retour à la page de connexion"}
                                </a>
                            </Col>
                        </Row>
                    </Col>
                </Row>
            </Col>
        </Row>
    );
}

export default LoginPage;