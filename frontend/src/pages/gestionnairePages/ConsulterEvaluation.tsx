import IUser from "../../models/IUser";
import React, {useEffect, useState} from "react";
import {Button, Col, Container, Row, Table, ToggleButton, ToggleButtonGroup} from "react-bootstrap";
import {Link, useParams} from "react-router-dom";
import IContract from "../../models/IContract";
import {
    putEvaluationPdf,
    putGetContractsParOffreId
} from "../../services/gestionnaireServices/GestionnaireFetchService";
import {generateAlert} from "../../services/universalServices/UniversalUtilService";
import {Viewer} from "@react-pdf-viewer/core";

const ConsulterEvaluation = ({connectedUser}:
                                 { connectedUser: IUser }): JSX.Element => {
    const [contrats, setContrats] = useState<IContract[]>([])
    const [evaluation, setEvaluation] = useState<Uint8Array>(new Uint8Array([]));
    const [showEvaluation, setShowEvaluation] = useState<boolean>(false);

    const {offreId} = useParams()

    useEffect(()=>{
        const fetchContrat = async () => {
            try {
                const response = await putGetContractsParOffreId(offreId, connectedUser.token);
                if (response.ok) {
                    const data = await response.json();
                    setContrats(data);
                } else {
                    generateAlert()
                }
            } catch {
                generateAlert()
            }
        }
        fetchContrat()
    },[connectedUser])
    async function getEvaluation(contratId: string): Promise<void> {
        try {
            const response = await putEvaluationPdf(contratId, connectedUser.token)
            if (response.ok) {
                const data = await response.json();
                setEvaluation(data)
                setShowEvaluation(true);
            } else {
                generateAlert()
            }
        } catch (exception) {
            generateAlert()
        }
    }

    if (showEvaluation) {
        return (
            <Container>
                <Container className="min-vh-100 bg-white p-0">
                    <div className="bg-dark p-2">

                        <Button className="Btn btn-primary" onClick={() => setShowEvaluation(false)}>
                            Fermer
                        </Button>

                    </div>
                    <div>
                        <Viewer fileUrl={evaluation}/>
                    </div>
                </Container>
            </Container>
        );
    }

    return (
        <Container className="min-vh-100">
            <Row>
                <Col sm={2}>
                    <Link to="/" className="btn btn-primary my-3">Home</Link>
                </Col>
                <Col sm={8} className="text-center pt-2">
                    <h1 className="fw-bold text-white display-3 pb-2">Évaluations</h1>
                </Col>
                <Col sm={2}></Col>
            </Row>
            <Row>
                <Col className="bg-light p-0" style={{height: 400}}>
                    <Table className="text-center" hover>
                        <thead className="bg-primary">
                        <tr>
                            <th>Description du contrat</th>
                            <th>Évaluation (pdf)</th>
                        </tr>
                        </thead>
                        <tbody className="bg-light">
                        {contrats.map((contrat, index) => {
                            return (
                                <tr key={index}>
                                    <td>{contrat.description}</td>
                                    <td><Button className="btn btn-warning"
                                                onClick={async () => await getEvaluation(contrat.contractId)}>pdf</Button>
                                    </td>
                                </tr>
                            );
                        })}
                        </tbody>
                    </Table>
                </Col>
            </Row>
        </Container>
    )
}
export default ConsulterEvaluation