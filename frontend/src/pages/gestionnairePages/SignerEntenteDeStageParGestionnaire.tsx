import React, {useEffect, useState} from "react";
import {Button, Col, Container, Row, Table} from "react-bootstrap";
import IUser from "../../models/IUser";
import {generateAlert} from "../../services/universalServices/UniversalUtilService";
import PageHeader from "../../components/universalComponents/PageHeader";
import {putContracts, putSignatureContract} from "../../services/gestionnaireServices/GestionnaireFetchService";
import StageContract from "../../models/StageContract";
import SignaturePopup from "../../components/universalComponents/SignaturePopup";

const SignerEntenteDeStageParGestionnaire = ({connectedUser}: { connectedUser: IUser }): JSX.Element => {
    const [contratsNonSigner, setContratsNonSigner] = useState<StageContract[]>([])
    const [isSigner, setIsSigner] = useState(false)
    const [contratId, setContratId] = useState(0)

    useEffect(() => {
        const fetchContracts = async () => {
            try {
                const response = await putContracts(connectedUser.token)
                if (response.ok) {
                    const data = await response.json();
                    setContratsNonSigner(data)

                } else {
                    generateAlert()
                }
            } catch {
                generateAlert()
            }
        }
        fetchContracts()
    }, [connectedUser])

    async function getEntente(contratId: number): Promise<void> {
        setIsSigner(true)
        setContratId(contratId)
    }

    async function signer(signature: string): Promise<void> {
        setIsSigner(false)
        try {
            const response = await putSignatureContract(connectedUser.token, Number(connectedUser.id), contratId, signature)
            if (response.ok) {
                contratsNonSigner.forEach(contrat => {
                    if (contrat.contractId === contratId) {
                        contrat.gestionnaireSignature = signature;
                        return;
                    }
                })
                setContratsNonSigner([...contratsNonSigner])
            } else {
                generateAlert()
            }
        } catch {
            generateAlert()
        }
    }

    return (
        <Container className="min-vh-100">
            <PageHeader title={"Signer Entente De Stage"}/>
            {isSigner && <SignaturePopup setSigning={setIsSigner} onSignature={signer}/>}
            <Col>
                <Row className="mt-5">
                    <Col className="bg-light p-0" style={{minHeight: 400}}>
                        <Table className="text-center" hover responsive>
                            <thead className="bg-primary text-white">
                            <tr className="text-white">
                                <th>Nom de compagnie</th>
                                <th>Nom complet de l'étudiant</th>
                                <th>position</th>
                                <th>session</th>
                                <th>Description</th>
                                <th>Date de signature de l'entreprise</th>
                                <th>Date de signature de l'étudiant</th>
                                <th>Signature</th>
                            </tr>
                            </thead>
                            <tbody className="bg-light">
                            {contratsNonSigner.length === 0
                                ? <tr>
                                    <td colSpan={8}>
                                        <p className="h1">Aucun contrat</p>
                                    </td>
                                </tr>
                                : contratsNonSigner.map((contrat, index) => {
                                    return (
                                        <tr key={index}>
                                            <td>{contrat.companyName}</td>
                                            <td>{contrat.studentFullName}</td>
                                            <td>{contrat.position}</td>
                                            <td>{contrat.session}</td>
                                            <td>{contrat.description}</td>
                                            <td>{contrat.companySignatureDate.split("T")[0]}</td>
                                            <td>{contrat.studentSignatureDate.split("T")[0]}</td>
                                            <td>
                                                {contrat.gestionnaireSignature.length > 0 ?
                                                    <p className="text-success">Vous avez signé</p> :
                                                    <p className="text-danger">Vous n'avez pas signé!</p>}
                                                <Button variant="success"
                                                        disabled={contrat.gestionnaireSignature.length > 0}
                                                        onClick={async () => await getEntente(contrat.contractId)}>
                                                    Signer
                                                </Button>
                                            </td>
                                        </tr>
                                    );
                                })}
                            </tbody>
                        </Table>
                    </Col>
                </Row>
            </Col>
        </Container>
    )
}

export default SignerEntenteDeStageParGestionnaire;