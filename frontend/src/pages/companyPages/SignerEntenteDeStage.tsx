import React, {useEffect, useState} from "react";
import {Button, Col, Container, Row, Table} from "react-bootstrap";
import {Link} from "react-router-dom";
import IUser from "../../models/IUser";
import {generateAlert} from "../../services/universalServices/UniversalUtilService";
import {Viewer} from "@react-pdf-viewer/core";
import {putcompanyContracts, putCompanySignatureContract} from "../../services/companyServices/CompanyFetchService";

const SignerEntenteDeStage = ({user}: { user: IUser }): JSX.Element => {
    const [contratsNonSigner, setContratsNonSigner] = useState<any[]>([])
    const [isSigner, setIsSigner] = useState(false)
    const [contratId, setContratId] = useState(0)
    const nextYear: number = new Date().getFullYear() + 1
    useEffect(() => {
        const fetchCompanyContracts = async () => {
            try {
                const response = await putcompanyContracts(user.id, user.token, "Hiver", nextYear)
                if (response.ok) {
                    const data = await response.json();
                    console.log(data)
                    setContratsNonSigner(data)
                } else {
                    generateAlert()
                }
            } catch {
                generateAlert()
            }
        }
        fetchCompanyContracts()
    }, [])

    async function getEntente(contratId: number): Promise<void> {
        setIsSigner(true)
        setContratId(contratId)
    }

    async function signer(): Promise<void> {
        setIsSigner(false)
        try {
            const response = await putCompanySignatureContract(user.token, user.id, contratId)
            if (response.ok) {
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
            <Container className="min-vh-100 bg-white p-0">
                <div className="bg-dark p-2">
                    <Button className="Btn btn-primary" onClick={async () => await signer()}>
                        Fermer
                    </Button>
                </div>
                <div>
                    <Viewer fileUrl={""}/>
                </div>
            </Container>
        );
    }

    return (
        <Container className="d-flex justify-content-center">
            <Col>
                <Row>
                    <Col sm={2}>
                        <Link to="/" className="btn btn-primary mt-1">Home</Link>
                    </Col>
                    <Col sm={8} className="text-center pt-2">
                        <h1 className="fw-bold text-white display-3 pb-2">Signer Entente De Stage</h1>
                    </Col>
                    <Col sm={2}></Col>
                </Row>
                <Row className="mt-5">
                    <Col className="bg-light p-0">
                        <Table className="text-center table table-bordered" hover>
                            <thead className="bg-primary">
                            <tr className="text-white">
                                <th>First Name</th>
                                <th>Last Name</th>
                                <th>Session</th>
                                <th>Description</th>
                                <th>Company Signature</th>
                                <th>Company Signature Date</th>
                                <th>Ententes</th>
                            </tr>
                            </thead>
                            <tbody className="bg-light">
                            {contratsNonSigner.map((contrat, index) => {
                                return (
                                    <tr key={index}>
                                        <td>{user.firstName}</td>
                                        <td>{user.lastName}</td>
                                        <td>{contrat.session}</td>
                                        <td>{contrat.description}</td>
                                        <td>{contrat.companySignature}</td>
                                        <td>{contrat.companySignatureDate}</td>
                                        <td>
                                            <Button className="btn btn-warning"
                                                    onClick={async () => await getEntente(contrat.contractId)}>Signer</Button>
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

export default SignerEntenteDeStage