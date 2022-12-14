import React, {useCallback, useEffect, useState} from "react";
import {Button, Col, Container, Row, Table} from "react-bootstrap";
import IUser from "../../models/IUser";
import {generateAlert} from "../../services/universalServices/UniversalUtilService";
import {
    putcompanyContracts,
    putCompanySignatureContract,
    putGetEvaluatedStudentsContracts
} from "../../services/companyServices/CompanyFetchService";
import PageHeader from "../../components/universalComponents/PageHeader";
import StudentEvaluationForm from "../../components/companyComponents/StudentEvaluationForm";
import {universalFetch} from "../../services/universalServices/UniversalFetchService";
import SignaturePopup from "../../components/universalComponents/SignaturePopup";

const CompanyContractsPage = ({connectedUser}: { connectedUser: IUser }): JSX.Element => {
    const [contratsNonSigner, setContratsNonSigner] = useState<any[]>([])
    const [isSigner, setIsSigner] = useState(false)
    const [contratId, setContratId] = useState(0)
    const [evaluatedStudentContracts, setEvaluatedStudentContracts] = useState<string[]>([])
    const [currentlySelectedContract, setCurrentlySelectedContract] = useState<string>("");
    const [showEvaluationForm, setShowEvaluationForm] = useState(false)
    const nextYear: number = new Date().getFullYear() + 1

    const fetchEvaluatedStudentContracts = useCallback(async () => {
        universalFetch(async () =>
                await putGetEvaluatedStudentsContracts(connectedUser.token, connectedUser.id),
            async (response: Response) => {
                const data = await response.json();
                setEvaluatedStudentContracts(data)
            });
    }, [connectedUser]);

    useEffect(() => {
        const fetchCompanyContracts = async () => {
            try {
                const response = await putcompanyContracts(connectedUser.id, connectedUser.token, "Hiver", nextYear)
                if (response.ok) {
                    const data = await response.json();
                    setContratsNonSigner(data.contracts)
                } else {
                    generateAlert()
                }
            } catch {
                generateAlert()
            }
        }
        fetchCompanyContracts()
        fetchEvaluatedStudentContracts()
    }, [fetchEvaluatedStudentContracts, connectedUser, nextYear])


    async function getEntente(contratId: number): Promise<void> {
        setIsSigner(true)
        setContratId(contratId)
    }

    async function signer(signature: string): Promise<void> {
        setIsSigner(false)
        try {
            const response = await putCompanySignatureContract(connectedUser.token, connectedUser.id, contratId, signature)
            if (response.ok) {
                contratsNonSigner.forEach(contrat => {
                    if (contrat.contractId === contratId) {
                        contrat.companySignature = signature;
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

    if (showEvaluationForm) {
        return (
            <StudentEvaluationForm
                contractId={currentlySelectedContract}
                connectedUser={connectedUser}
                setShowEvaluationForm={setShowEvaluationForm}/>
        );
    }

    return (
        <Container className="min-vh-100">
            {isSigner && <SignaturePopup setSigning={setIsSigner} onSignature={signer}/>}
            <PageHeader title={"Mes contrats"}></PageHeader>
            <Row>
                <Col className="bg-light p-0" style={{minHeight: 400}}>
                    <Table className="text-center" hover>
                        <thead className="text-white bg-primary">
                        <tr>
                            <th>Description</th>
                            <th>Evaluation</th>
                            <th>Ententes</th>
                        </tr>
                        </thead>
                        <tbody>
                        {contratsNonSigner.length === 0
                            ? <tr>
                                <td colSpan={3}>
                                    <p className="h1">Aucun contrat</p>
                                </td>
                            </tr>
                            : contratsNonSigner.map((contrat, index) => {
                                return (
                                    <tr key={index}>
                                        <td>{contrat.description}</td>
                                        <td>
                                            {
                                                (evaluatedStudentContracts.includes(contrat.contractId) &&
                                                    <p className="text-success">Vous avez d??j?? ??valu??</p>)
                                                || (!evaluatedStudentContracts.includes(contrat.contractId) &&
                                                contrat.gestionnaireSignature.length > 0
                                                    ? <Button variant="warning" onClick={() => {
                                                        setCurrentlySelectedContract(contrat.contractId)
                                                        setShowEvaluationForm(true)
                                                    }}>??valuer</Button>
                                                    : "Vous devez attendre que tout les partis signent")
                                            }
                                        </td>
                                        <td>
                                            {contrat.companySignature.length > 0 ?
                                                <p className="text-success">Vous avez sign??</p> :
                                                <p className="text-danger">Vous n'avez pas sign??!</p>}
                                            <Button variant="success" disabled={contrat.companySignature.length > 0}
                                                    onClick={async () => {
                                                        await getEntente(contrat.contractId)
                                                    }}>Signer</Button>
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

export default CompanyContractsPage