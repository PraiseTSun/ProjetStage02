import React, {useEffect, useState} from "react";
import {Button, Col, Container, Row, Table} from "react-bootstrap";
import {Link} from "react-router-dom";
import IUser from "../../models/IUser";
import {putcompanyContracts, putUnvalidatedOffers} from "../../services/gestionnaireServices/GestionnaireFetchService";
import {generateAlert} from "../../services/universalServices/UniversalUtilService";

const SignerEntenteDeStage = ({user}: { user: IUser }): JSX.Element => {
    const [contratsNonSigner, setContratsNonSigner] = useState<any[]>([])

    useEffect(()=>{
        const fetchCompanyContracts = async () => {
            try {
                const response = await putcompanyContracts(user.id, user.token)
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
       fetchCompanyContracts()
    },[])

    async function getEntente(ententeId: number): Promise<void> {

    }

    return (
        <Container className="d-flex justify-content-center">
            <Col>
                <Row>
                    <Col sm={2}>
                        <Link to="/" className="btn btn-primary mt-1">Home</Link>
                    </Col>
                    <Col sm={8} className="">
                    </Col>
                    <Col sm={2}></Col>
                </Row>
                <Row className="mt-5">
                    <Col className="bg-light p-0">
                        <Table className="text-center table table-bordered" hover>
                            <thead className="bg-primary">
                            <tr>
                                <th>Nom Étudiant</th>
                                <th>Position</th>
                                <th>Date</th>
                                <th>Ententes</th>
                            </tr>
                            </thead>
                            <tbody className="bg-light">
                            {contratsNonSigner.map((contrat, index) => {
                                return (
                                    <tr key={index}>
                                        <td>{user.firstName} {user.lastName}</td>
                                        <td>{}</td>
                                        <td>{}</td>
                                        <td>
                                            <Button className="btn btn-warning"
                                                    onClick={async () => await getEntente(contrat.id)}>Signer</Button>
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