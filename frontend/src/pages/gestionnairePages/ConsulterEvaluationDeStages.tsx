import IUser from "../../models/IUser";
import React, {useEffect, useState} from "react";
import {Button, Col, Container, Row, Table} from "react-bootstrap";
import {
    putEvaluationPdf,
    putGetContractsPourMillieuStage
} from "../../services/gestionnaireServices/GestionnaireFetchService";
import {generateAlert} from "../../services/universalServices/UniversalUtilService";
import IContract from "../../models/IContract";
import PdfComponent from "../../components/universalComponents/PdfComponent";
import PageHeader from "../../components/universalComponents/PageHeader";

const ConsulterEvaluationDeStages = ({connectedUser}:
                                         { connectedUser: IUser }): JSX.Element => {
    const [contrats, setContrats] = useState<any>([])
    const [evaluation, setEvaluation] = useState<Uint8Array>(new Uint8Array([]));
    const [showEvaluation, setShowEvaluation] = useState<boolean>(false);

    useEffect(() => {
        const fetchContrat = async () => {
            try {
                const response = await putGetContractsPourMillieuStage(connectedUser.token);
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
    }, [connectedUser])

    const getEvaluation = async (contratId: number): Promise<void> => {
        try {
            const response = await putEvaluationPdf(contratId, connectedUser.token)
            if (response.ok) {
                const data = await response.json();
                var toUint8Array = require('base64-to-uint8array')
                setEvaluation(toUint8Array(data.pdf))
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
            <PdfComponent pdf={evaluation} setShowPdf={setShowEvaluation}/>
        );
    }

    return (
        <Container className="min-vh-100">
            <PageHeader title={"Évaluations des stages"}/>
            <Row>
                <Col className="bg-light p-0" style={{minHeight: 400}}>
                    <Table className="text-center" hover>
                        <thead className="bg-primary text-white">
                        <tr>
                            <th>Description du contrat</th>
                            <th>Évaluation (pdf)</th>
                        </tr>
                        </thead>
                        <tbody className="bg-light text-dark">
                        {contrats.length === 0
                            ? <tr>
                                <td colSpan={2}>
                                    <p className="h1">Aucune évaluation</p>
                                </td>
                            </tr>
                            : contrats.map((contrat: IContract, index: number) => {
                                return (
                                    <tr key={index}>
                                        <td>{contrat.description}</td>
                                        <td><Button className="btn btn-warning"
                                                    onClick={async () => await getEvaluation(Number(contrat.contractId))}>pdf</Button>
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
export default ConsulterEvaluationDeStages;