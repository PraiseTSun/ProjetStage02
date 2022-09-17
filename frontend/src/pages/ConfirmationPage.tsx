import React from 'react';
import { useState, useEffect } from 'react'
import { Row, Col } from 'react-bootstrap';
import { useLocation, useParams } from 'react-router-dom';
import BeatLoader from 'react-spinners/BeatLoader';

const USER_PARAM = "userType=";

const ConfirmationPage = (): JSX.Element => {
    const [loading, setLoading] = useState(true);
    const [message, setMessage] = useState("");

    const query: String = useLocation().search;
    const userType: String = query.substring(query.indexOf(USER_PARAM) + USER_PARAM.length, query.length);

    const headers = {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
    };
    fetch("http://localhost:8080/confirmEmail/" + userType + "/" + useParams().id, headers)
        .then(response => {
            if (response.ok) {
                setMessage("Succès! Vous pouvez fermez cette page");
            }
            else {
                alert("Lien invalide ou expiré")
                window.location.href = "/"
            }
            setLoading(false);
        });

    return (
        <Row className="vh-100">
            <Col className="m-auto">
                <h1 className="text-white text-center display-1">{message}</h1>
                <BeatLoader className="text-center" color="#ffffff" loading={loading} size={100} />
            </Col>
        </Row>
    );
}

export default ConfirmationPage;