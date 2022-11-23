import React, {useState} from "react";
import {Col, Container, ListGroup, Tab} from "react-bootstrap";
import FormulaireInscriptionEtudiant from "./FormulaireInscriptionEtudiant";
import FormulaireInscriptionEntreprise from "./FormulaireInscriptionEntreprise";
import {postUserType} from "../../services/universalServices/UniversalFetchService";

const InscriptionForm = (): JSX.Element => {
    const [signupSent, setSignupSent] = useState<boolean>(false);

    const onInscrire = async (compte: object, type: string) => {
        const res = await postUserType(type, compte);

        if (!res.ok) {
            const data = await res.json();
            alert(data.error);
        } else {
            setSignupSent(true);
        }
    }

    if (signupSent) {
        return (
            <Col className="p-2 text-center">
                <h1 className="text-success fw-bold">Courriel de confirmation envoyé</h1>
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
                        <FormulaireInscriptionEtudiant onInscrire={onInscrire}/>
                    </Tab.Pane>
                    <Tab.Pane eventKey="#entreprise">
                        <FormulaireInscriptionEntreprise onInscrire={onInscrire}/>
                    </Tab.Pane>
                </Tab.Content>
            </Tab.Container>
        </Container>
    );
}

export default InscriptionForm;