import React from "react";
import { Accordion, Row } from "react-bootstrap";
import FormulaireSoumission from "./FormulaireSoumission";

const CompanyDashboard = (): JSX.Element => {

    return (
        <Row className="d-flex justify-content-center">
            <Accordion className="col-7">
                <Accordion.Item  className="bg-primary" eventKey="0">
                    <Accordion.Header>Soumettre une offre de stage</Accordion.Header>
                    <Accordion.Body>
                        <FormulaireSoumission />
                    </Accordion.Body>
                </Accordion.Item>
            </Accordion>
        </Row>
    )
}

export default CompanyDashboard;