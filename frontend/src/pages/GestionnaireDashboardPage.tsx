import React from "react";
import { Row, Accordion, Button } from "react-bootstrap";
import { Link } from "react-router-dom";
import { emptyUser } from "../App";
import IUser from "../models/IUser";

const GestionnaireDashboard = ({ user, setUser }: { user: IUser, setUser: Function }): JSX.Element => {

    return (
        <>
            <Button className="btn btn-danger my-2" onClick={() => setUser(emptyUser)}>
                Déconnexion
            </Button>
            <h1 className="p-5 text-center ">Bienvenue {user.firstName} {user.lastName}</h1>
            <Row className="d-flex justify-content-center">
                <Accordion className="col-7">
                    <Accordion.Item className="bg-secondary" eventKey="0">
                        <Accordion.Header>Valider les utilisateurs</Accordion.Header>
                        <Accordion.Body>
                            <Link to="/userValidation" className="btn btn-primary">Validation des utilisateurs</Link>
                        </Accordion.Body>
                    </Accordion.Item>
                </Accordion>
            </Row>
        </>
    );
}

export default GestionnaireDashboard;