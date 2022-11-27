import React, {useState} from "react";
import {Col, Container, ListGroup, Tab} from "react-bootstrap";
import FormulaireInscriptionEtudiant from "./FormulaireInscriptionEtudiant";
import FormulaireInscriptionEntreprise from "./FormulaireInscriptionEntreprise";

const InscriptionForm = (): JSX.Element => {
    const [signupSent, setSignupSent] = useState<boolean>(false);

    if (signupSent) {
        return (
            <Col className="p-2 text-center">
                <h1 className="text-success fw-bold">Courriel de confirmation envoyé. En attente d'approbation</h1>
            </Col>
        );
    }

    return (
        <Container className="px-0">
            <Tab.Container defaultActiveKey="#etudiant">
                <ListGroup horizontal className="">
                    <ListGroup.Item action href="#etudiant" variant="secondary">
                        Etudiant
                    </ListGroup.Item>
                    <ListGroup.Item data-testid="entrepriseInscriptionForm" action href="#entreprise"
                                    variant="secondary">
                        Entreprise
                    </ListGroup.Item>
                </ListGroup>

                <Tab.Content className="mt-3 p-2">
                    <Tab.Pane eventKey="#etudiant">
                        <FormulaireInscriptionEtudiant onInscrire={setSignupSent}/>
                    </Tab.Pane>
                    <Tab.Pane eventKey="#entreprise">
                        <FormulaireInscriptionEntreprise onInscrire={setSignupSent}/>
                    </Tab.Pane>
                </Tab.Content>
            </Tab.Container>
        </Container>
    );
}

export default InscriptionForm;