import IUser from "../../models/IUser";
import {Button, Col, Container, Row, Table} from "react-bootstrap";
import PageHeader from "../../components/universalComponents/PageHeader";
import React, {useCallback, useEffect, useState} from "react";
import IAcceptation from "../../models/IAcceptation";
import {putUnvalidatedAcceptations} from "../../services/gestionnaireServices/GestionnaireFetchService";
import {generateAlert} from "../../services/universalServices/UniversalUtilService";

const UnvalidatedAcceptationsPage = ({connectedUser}: { connectedUser: IUser }): JSX.Element => {
    const [acceptations, setAcceptations] = useState<IAcceptation[]>([]);

    const fetchAcceptations = useCallback(async () => {
        try {
            const response: Response = await putUnvalidatedAcceptations(connectedUser.token);

            if (response.ok) {
                const data: any = await response.json();
                setAcceptations(data.applications);
            } else {
                generateAlert()
            }
        } catch {
            generateAlert()
        }
    }, [connectedUser]);

    useEffect(() => {
        fetchAcceptations();
    }, [fetchAcceptations, connectedUser])

    return (
        <Container className="vh-100">
            <PageHeader title="Acceptations non validées"/>
            <Row>
                <Col className="bg-light p-0" style={{height: 400}}>
                    <Table className="text-center" hover>
                        <thead className="bg-primary text-white">
                        <tr>
                            <th>Étudiant</th>
                            <th>Employé</th>
                            <th>Position</th>
                            <th>Compagnie</th>
                            <th>Approuver</th>
                        </tr>
                        </thead>
                        <tbody className="bg-light">
                        {acceptations.length === 0
                            ? <tr>
                                <td colSpan={5}>
                                    <p className="h1">Aucun étudiant accepté</p>
                                </td>
                            </tr>
                            : acceptations.map((acceptation, index) => {
                                return (
                                    <tr key={index}>
                                        <td>{acceptation.studentFullName}</td>
                                        <td>{acceptation.employFullName}</td>
                                        <td>{acceptation.position}</td>
                                        <td>{acceptation.companyName}</td>
                                        <td><Button variant="success">Approuver</Button></td>
                                    </tr>
                                );
                            })}
                        </tbody>
                    </Table>
                </Col>
            </Row>
        </Container>
    );
}

export default UnvalidatedAcceptationsPage;