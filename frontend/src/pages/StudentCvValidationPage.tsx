import React, { useEffect, useState } from "react";
import { Container, Row, Col, Table, Button } from "react-bootstrap";
import { Link } from "react-router-dom";

const StudentCvValidationPage = ({ deconnexion }: { deconnexion: Function }): JSX.Element => {
    const [students, setStudents] = useState<any[]>([]);

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
                                        <td><Link to={"/cv/" + student.id} className="btn btn-warning">CV</Link></td>
                                        <td>
                                            <Button className="btn btn-success mx-2">O</Button>
                                            <Button className="btn btn-danger">X</Button>
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