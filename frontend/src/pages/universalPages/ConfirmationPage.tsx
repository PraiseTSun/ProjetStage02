import React, {useEffect, useState} from 'react';
import {Col, Row} from 'react-bootstrap';
import {useLocation, useParams} from 'react-router-dom';
import BeatLoader from 'react-spinners/BeatLoader';
import {putConfirmEmail} from "../../services/universalServices/UniversalFetchService";

const USER_PARAM = "userType=";

const ConfirmationPage = (): JSX.Element => {
    const [loading, setLoading] = useState(true);
    const [message, setMessage] = useState("");
    const query: string = useLocation().search;
    const pathId: string = useParams().id!;
    const userType: string = query.substring(query.indexOf(USER_PARAM) + USER_PARAM.length, query.length);

    useEffect(() => {
        const confirmUserEmail = async () => {
            const response = await putConfirmEmail(userType, pathId);
            if (response.ok) {
                setMessage("Succès! Vous pouvez fermez cette page");
            } else {
                alert("Lien invalide ou expiré");
                window.location.href = "/";
            }
            setLoading(false);
        }

        confirmUserEmail();
    });

    return (
        <Row className="min-vh-100">
            <Col className="m-auto">
                <h1 className="text-white text-center display-1">{message}</h1>
                <BeatLoader className="text-center" color="#ffffff" loading={loading} size={100}/>
            </Col>
        </Row>
    );
}

export default ConfirmationPage;