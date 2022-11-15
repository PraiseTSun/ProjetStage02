import React, {useEffect, useState} from "react";
import {Button, Col, Container, Row, Table} from "react-bootstrap";
import IUser from "../../models/IUser";
import {generateAlert} from "../../services/universalServices/UniversalUtilService";
import SignaturePad from "react-signature-canvas";
import PageHeader from "../../components/universalComponents/PageHeader";
import {putContracts, putSignatureContract} from "../../services/gestionnaireServices/GestionnaireFetchService";
import StageContract from "../../models/StageContract";

const SignerEntenteDeStageParGestionnaire = ({connectedUser}: { connectedUser: IUser }): JSX.Element => {
    const [contratsNonSigner, setContratsNonSigner] = useState<StageContract[]>([])
    const [isSigner, setIsSigner] = useState(false)
    const [contratId, setContratId] = useState(0)
    let sigPad: SignaturePad | null

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
                alert("Félicitations vous avez signé le contrat")
            } else {
                generateAlert()
            }
        } catch {
            generateAlert()
        }
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
        <Container className="d-flex justify-content-center vh-100">
            <Col>
                <PageHeader title={"Signer Entente De Stage"}/>
                <Row className="mt-5">
                    <Col className="bg-light p-0">
                        <Table className="text-center table table-bordered" hover>
                            <thead className="bg-primary">
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
                            {contratsNonSigner.map((contrat, index) => {
                                return (
                                    <tr key={index}>
                                        <td>{contrat.companyName}</td>
                                        <td>{contrat.studentFullName}</td>
                                        <td>{contrat.position}</td>
                                        <td>{contrat.session}</td>
                                        <td>{contrat.description}</td>
                                        <td>{contrat.companySignatureDate.split("T")[0]}</td>
                                        <td>{contrat.studentSignatureDate.split("T")[0]}</td>
                                        <td>{
                                            contrat.gestionnaireSignature.length > 0 ?
                                                <p>Déjà signé</p>
                                                :
                                                <Button className="btn btn-warning"
                                                        onClick={async () => await getEntente(contrat.contractId)}>Signer</Button>

                                        }</td>
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