import React, {useCallback, useEffect, useState} from "react";
import {Button, Col, Container, Row, Table} from "react-bootstrap";
import IUser from "../../models/IUser";
import {generateAlert} from "../../services/universalServices/UniversalUtilService";
import {
    putcompanyContracts,
    putCompanySignatureContract,
    putGetEvaluatedStudentsContrats
} from "../../services/companyServices/CompanyFetchService";
import SignaturePad from "react-signature-canvas";
import PageHeader from "../../components/universalComponents/PageHeader";
import StudentEvaluationForm from "../../components/companyComponents/StudentEvaluationForm";
import {universalFetch} from "../../services/universalServices/UniversalFetchService";

const CompanyContractsPage = ({connectedUser}: { connectedUser: IUser }): JSX.Element => {
    const [contratsNonSigner, setContratsNonSigner] = useState<any[]>([])
    const [isSigner, setIsSigner] = useState(false)
    const [contratId, setContratId] = useState(0)
    const [evaluatedStudentContracts, setEvaluatedStudentContracts] = useState<string[]>([])
    const [currentlySelectedContract, setCurrentlySelectedContract] = useState<string>("");
    const [showEvaluationForm, setShowEvaluationForm] = useState(false)
    const nextYear: number = new Date().getFullYear() + 1
    let sigPad: SignaturePad | null

    const fetchEvaluatedStudentContracts = useCallback(async () => {
        universalFetch(async () =>
                await putGetEvaluatedStudentsContrats(connectedUser.token, connectedUser.id),
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
                alert("Félicitations vous avez signé le contrat")
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
                setShowEvaluationForm={setShowEvaluationForm}
                setEvaluatedStudentContracts={fetchEvaluatedStudentContracts}
                evaluatedStudentContracts={evaluatedStudentContracts}/>
        );
    }

    if (isSigner) {
        return (
            <Container className="vh-100">
                <Row className="bg-dark p-2">
                    <Col sm={1}><Button variant="danger" onClick={() => {
                        setIsSigner(false)
                    }}>Fermer</Button></Col>
                    <Col sm={10}></Col>
                    <Col sm={1}><Button variant="success" onClick={() => {
                        if (sigPad!.isEmpty()) {
                            alert("Vous devez signer!")
                        } else {
                            signer(sigPad!.toDataURL())
                        }
                    }}>Signer</Button></Col>
                </Row>
                <Row>
                    <Col sm={4} className="mx-auto mt-3">
                        <SignaturePad canvasProps={{width: 500, height: 200, className: 'border border-5 bg-light'}}
                                      ref={(ref) => {
                                          sigPad = ref
                                      }}/>
                        <Button onClick={() => {
                            sigPad!.clear()
                        }}>Recommencer</Button>
                    </Col>
                </Row>
            </Container>
        );
    }

    return (
        <Container className="d-flex justify-content-center">
            <Col>
                <PageHeader title={"Mes contrats"}></PageHeader>
                <Row className="mt-5">
                    <Col className="bg-light p-0">
                        <Table className="text-center table table-bordered" hover>
                            <thead className="bg-primary">
                            <tr className="text-white">
                                <th>Description</th>
                                <th>Evaluation</th>
                                <th>Ententes</th>
                            </tr>
                            </thead>
                            <tbody className="bg-light">
                            {contratsNonSigner.map((contrat, index) => {
                                return (
                                    <tr key={index}>
                                        <td>{contrat.description}</td>
                                        <td>
                                            {
                                                (evaluatedStudentContracts.includes(contrat.contractId) && "Déjà évalué")
                                                || (!evaluatedStudentContracts.includes(contrat.contractId) &&
                                                contrat.companySignature.length > 0
                                                    ? <Button variant="warning" onClick={() => {
                                                        setCurrentlySelectedContract(contrat.contractId)
                                                        setShowEvaluationForm(true)
                                                    }}>Évaluer</Button>
                                                    : "Vous devez signé avant")
                                            }
                                        </td>
                                        <td>
                                            {
                                                contrat.companySignature.length > 0
                                                    ? <p>Déjà signé</p>
                                                    : <Button className="btn btn-warning"
                                                              onClick={async () =>
                                                                  await getEntente(contrat.contractId)
                                                              }>
                                                        Signer
                                                    </Button>
                                            }
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

export default CompanyContractsPage