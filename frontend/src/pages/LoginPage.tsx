import React from "react";
import { useState } from "react";
import { Col, Row } from "react-bootstrap";
import LoginForm from "../components/LoginForm";

const LoginPage = (props: { setUser: Function }): JSX.Element => {
    const [isLogginPage, setIsLogginPage] = useState(true)

    return (    
        <Row className="vh-100">
            <Col className="m-auto col-lg-4 col-md-6">
                <h1 className="text-warning fw-bold text-center display-3">OSE KILLER</h1>
                {isLogginPage ? <LoginForm setUser={props.setUser} /> : <h1>Mettre register form ici</h1>}
                <Row>
                    <Col className="text-center">
                        <a href="#" className="link-warning" onClick={() => setIsLogginPage(!isLogginPage)}>
                            {isLogginPage ? "Nouveau utilisateur? Inscrivez vous ici." : "Retour à la page de connexion"}
                        </a>
                    </Col>
                </Row>
            </Col>
        </Row>
    );
}

export default LoginPage;