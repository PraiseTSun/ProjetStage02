import React, {useEffect, useState} from "react";
import {Button, Col, Container, Row, Table} from "react-bootstrap";
import IUser from "../../models/IUser";
import {generateAlert} from "../../services/universalServices/UniversalUtilService";
import {putcompanyContracts, putCompanySignatureContract} from "../../services/companyServices/CompanyFetchService";
import SignaturePad from "react-signature-canvas";
import PageHeader from "../../components/universalComponents/PageHeader";

const SignerEntenteDeStageParCompagnie = ({user}: { user: IUser }): JSX.Element => {
    const [contratsNonSigner, setContratsNonSigner] = useState<any[]>([])
    const [isSigner, setIsSigner] = useState(false)
    const [contratId, setContratId] = useState(0)
    const nextYear: number = new Date().getFullYear() + 1
    let sigPad: SignaturePad | null

    useEffect(() => {
        const fetchCompanyContracts = async () => {
            try {
                const response = await putcompanyContracts(user.id, user.token, "Hiver", nextYear)
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
    }, [user])

    async function getEntente(contratId: number): Promise<void> {
        setIsSigner(true)
        setContratId(contratId)
    }

    async function signer(signature: string): Promise<void> {
        setIsSigner(false)
        try {
            const response = await putCompanySignatureContract(user.token, user.id, contratId, signature)
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
                            console.log()
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
                <PageHeader title={"Signer Entente De Stage"}></PageHeader>
                <Row className="mt-5">
                    <Col className="bg-light p-0">
                        <Table className="text-center table table-bordered" hover>
                            <thead className="bg-primary">
                            <tr className="text-white">
                                <th>Description</th>
                                <th>Ententes</th>
                            </tr>
                            </thead>
                            <tbody className="bg-light">
                            {contratsNonSigner.map((contrat, index) => {
                                return (
                                    <tr key={index}>
                                        <td>{contrat.description}</td>
                                        <td>{
                                            contrat.companySignature.length > 0 ?
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

export default SignerEntenteDeStageParCompagnie