import {useState} from "react";
import { Container, ListGroup, Tab} from "react-bootstrap";
import FormulaireEtudiant from "./FormulaireEtudiant";
import FormulaireEntreprise from "./FormulaireEntreprise";
const PageInscription = ({ onInscrire } : { onInscrire : Function}) => {

    return(
    <Container className={"mx-auto mt-5"} style={{width: "500px"}}>
        <Tab.Container defaultActiveKey="#etudiant">

                    <ListGroup horizontal className="">
                        <ListGroup.Item action href="#etudiant" variant="success" >
                            Etudiant
                        </ListGroup.Item>
                        <ListGroup.Item action href="#entreprise" variant="success">
                            Entreprise
                        </ListGroup.Item>
                    </ListGroup>

                    <Tab.Content className="mt-3 p-2">
                        <Tab.Pane eventKey="#etudiant" >
                            <FormulaireEtudiant onInscrire={onInscrire} />
                        </Tab.Pane>
                        <Tab.Pane eventKey="#entreprise">
                            <FormulaireEntreprise onInscrire={onInscrire}/>
                        </Tab.Pane>
                    </Tab.Content>
        </Tab.Container>
    </Container>

    )
}
export default PageInscription