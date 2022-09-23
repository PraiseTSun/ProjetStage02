import React from "react";
import { Accordion, Button, Row } from "react-bootstrap";
import { emptyUser } from "../App";
import FormulaireSoumission from "../components/FormulaireSoumission";
import IUser from "../models/IUser";

const CompanyDashboard = ({ user, setUser }: { user: IUser, setUser: Function }): JSX.Element => {

    return (<>
        <Button className="btn btn-danger my-2" onClick={() => setUser(emptyUser)}>
            Déconnexion
        </Button>
        <h1 className="p-5 text-center ">Bienvenue {user.firstName} {user.lastName}</h1>
        <Row className="d-flex justify-content-center">
            <Accordion className="col-7">
                <Accordion.Item className="bg-primary" eventKey="0">
                    <Accordion.Header>Soumettre une offre de stage</Accordion.Header>
                    <Accordion.Body>
                        <FormulaireSoumission />
                    </Accordion.Body>
                </Accordion.Item>
            </Accordion>
        </Row>
    </>
    );
}

export default CompanyDashboard;