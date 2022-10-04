import React, { useEffect, useState } from "react";
import { Container, Row, Col, Table, Button } from "react-bootstrap";
import { Link } from "react-router-dom";

const StudentCvValidationPage = ({ deconnexion }: { deconnexion: Function }): JSX.Element => {
    const [students, setStudents] = useState<any[]>([]);
    const [showCV, setShowCV] = useState<boolean>(false)

    useEffect(() => {
        fetchUnvalidatedCvStudents()
    }, []);

    async function fetchUnvalidatedCvStudents(): Promise<void> {
        const response = await fetch("http://localhost:8080/unvalidatedCvStudents", {
            method: "PUT",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
        });
        const data = await response.json();
        setStudents(data);
    }

    async function validateCV(studentId: number, index: number, valid: boolean) {
        try {
            const url: String = valid ? "http://localhost:8080/validateCV/" : "http://localhost:8080/refuseCV/";
            const response: Response = await fetch(url + studentId.toString(), {
                method: "PUT",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                },
            });

            if (response.ok) {
                setStudents(students.splice(index + 1, 1));
            }
            else if (response.status == 403) {
                alert("Session expiré")
                deconnexion()
            }
            else {
                alert("Une erreur est survenue, ressayez.")
            }
        } catch (exception) {
            alert("Une erreur est survenue, ressayez.")
        }
    }

    return (
        <Container>
            <Row>
                <Col sm={2}>
                    <Link to="/" className="btn btn-primary my-3">Home</Link>
                </Col>
                <Col sm={8} className="text-center pt-2">
                    <h1 className="fw-bold">Validation de CV</h1>
                </Col>
                <Col sm={2}></Col>
            </Row>
            <Row>
                <Col>
                    <Table className="text-center" hover>
                        <thead className="bg-primary">
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
                                        <td><Button></Button></td>
                                        <td>
                                            <Button className="btn btn-success mx-2" onClick={() => validateCV(student.id, index, true)}>O</Button>
                                            <Button className="btn btn-danger" onClick={() => validateCV(student.id, index, false)}>X</Button>
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