import React, {useEffect, useState} from "react";
import {Button, Col, Container, Row, Table} from "react-bootstrap";
import {Link} from "react-router-dom";
import IUser from "../../models/IUser";
import {Viewer} from '@react-pdf-viewer/core';
import {
    putRefuseCv,
    putStudentCv,
    putUnvalidatedCvStudents,
    putValidateCv
} from "../../services/gestionnaireServices/GestionnaireFetchService";
import {generateAlert} from "../../services/universalServices/UniversalUtilService";

const StudentCvValidationPage = ({connectedUser}:
                                     { connectedUser: IUser }): JSX.Element => {
    const [students, setStudents] = useState<any[]>([]);
    const [pdf, setPDF] = useState<Uint8Array>(new Uint8Array([]))
    const [showPDF, setShowPDF] = useState<boolean>(false)

    useEffect(() => {
        const fetchUnvalidatedCvStudents = async () => {
            try {
                const response = await putUnvalidatedCvStudents(connectedUser.token)

                if (response.ok) {
                    const data = await response.json();
                    setStudents(data);
                } else {
                    generateAlert()
                }
            } catch {
                generateAlert()
            }
        }
        fetchUnvalidatedCvStudents();
    }, [connectedUser]);

    async function validateCV(studentId: number, valid: boolean): Promise<void> {
        try {
            const response = valid
                ? await putValidateCv(studentId.toString(), connectedUser.token)
                : await putRefuseCv(studentId.toString(), connectedUser.token);

            if (response.ok) {
                setStudents(students.filter(student => student.id !== studentId));
            } else {
                generateAlert()
            }
        } catch (exception) {
            generateAlert()
        }
    }

    async function getPDF(studentId: number): Promise<void> {
        try {
            const response = await putStudentCv(studentId.toString(), connectedUser.token)
            if (response.ok) {
                const data = await response.json();
                setPDF(new Uint8Array(JSON.parse(data.pdf)));
                setShowPDF(true);
            } else {
                generateAlert()
            }
        } catch (exception) {
            generateAlert()
        }
    }

    if (showPDF) {
        return (
            <Container className="min-vh-100 bg-white p-0">
                <div className="bg-dark p-2">
                    <Button className="Btn btn-primary" onClick={() => setShowPDF(false)}>
                        Fermer
                    </Button>
                </div>
                <div>
                    <Viewer fileUrl={pdf}/>
                </div>
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
                    <h1 className="fw-bold text-white display-3 pb-2">Liste des CVs non validés</h1>
                </Col>
                <Col sm={2}></Col>
            </Row>
            <Row>
                <Col>
                    <Table className="text-center" hover>
                        <thead className="bg-primary text-white">
                        <tr>
                            <th>Prénom</th>
                            <th>Nom</th>
                            <th>Departement</th>
                            <th>CV</th>
                            <th>CV Valide</th>
                        </tr>

                        </thead>
                        <tbody className="bg-light">
                        {students.map((student, index) => {
                            return (
                                <tr key={index}>
                                    <td>{student.firstName}</td>
                                    <td>{student.lastName}</td>
                                    <td>{student.department}</td>
                                    <td><Button className="btn btn-warning"
                                                onClick={async () => await getPDF(student.id)}>CV</Button></td>
                                    <td>
                                        <Button className="btn btn-success mx-2" onClick={
                                            () => validateCV(student.id, true)}>
                                            O
                                        </Button>
                                        <Button className="btn btn-danger" onClick={
                                            () => validateCV(student.id, false)}>
                                            X
                                        </Button>
                                    </td>
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

export default StudentCvValidationPage;