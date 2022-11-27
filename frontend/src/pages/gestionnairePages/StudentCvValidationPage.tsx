import React, {useCallback, useEffect, useState} from "react";
import {Button, Col, Container, Form, Row, Table} from "react-bootstrap";
import IUser from "../../models/IUser";
import {
    putRefuseCv,
    putStudentUnvalidatedCv,
    putUnvalidatedCvStudents,
    putValidateCv
} from "../../services/gestionnaireServices/GestionnaireFetchService";
import {generateAlert} from "../../services/universalServices/UniversalUtilService";
import PageHeader from "../../components/universalComponents/PageHeader";
import PdfComponent from "../../components/universalComponents/PdfComponent";

const StudentCvValidationPage = ({connectedUser}:
                                     { connectedUser: IUser }): JSX.Element => {
    const [students, setStudents] = useState<any[]>([]);
    const [pdf, setPDF] = useState<Uint8Array>(new Uint8Array([]))
    const [showPDF, setShowPDF] = useState<boolean>(false)
    const [isRefuse, setIsRefuse] = useState<boolean>(false)
    const [refuserMessge, setRefuserMessge] = useState<string>("")
    const [validated, setValidated] = useState<boolean>(false);
    const [studentId, setStudentId] = useState<string>("")

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

    const onSubmit = async (event: React.SyntheticEvent): Promise<void> => {
        const form: any = event.currentTarget;
        event.preventDefault();
        if (form.checkValidity()) {
            sendRefusalMessage();
        }
        setValidated(true);
    }

    const sendRefusalMessage = useCallback(async () => {
        const response = await putRefuseCv(studentId.toString(), connectedUser.token, refuserMessge,)

        if (response.ok) {
            setIsRefuse(false)
            setStudents(students.filter((student) => student.id.toString() !== studentId));
            setValidated(false)
            setRefuserMessge("")
        } else {
            generateAlert()
        }
    }, [studentId, refuserMessge]);

    async function validateCV(studentId: number, valid: boolean): Promise<void> {
        if (!valid) {
            setIsRefuse(true)
            setStudentId(studentId.toString())
            return;
        }
        try {
            const response = await putValidateCv(studentId.toString(), connectedUser.token)

            if (response.ok) {
                setStudents([...students.filter((student) => student.id !== studentId)]);
            } else {
                generateAlert()
            }

        } catch (exception) {
            generateAlert()
        }
    }

    async function getPDF(studentId: number): Promise<void> {
        try {
            const response = await putStudentUnvalidatedCv(studentId.toString(), connectedUser.token)
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
            <PdfComponent pdf={pdf} setShowPdf={setShowPDF}/>
        );
    }

    return (
        <Container className="min-vh-100">
            {isRefuse &&
                <div>
                    <Container
                        className="position-absolute min-vh-100 p-0 m-0 min-vw-100 bg-dark
                            p-0 top-0 start-0 end-0 opacity-50">
                    </Container>
                    <Container
                        className="position-absolute min-vh-100 min-vw-100 p-0 top-0 start-0 end-0">
                        <Row className="min-vh-100 m-0">
                            <Col sm={4} className="rounded m-auto bg-white">
                                <Form className="p-3 text-center" onSubmit={onSubmit} validated={validated}
                                      noValidate>
                                    <Form.Group>
                                        <Form.Label className="fw-bold h3">Message de refus</Form.Label>
                                        <Form.Control type="text" required
                                                      value={refuserMessge}
                                                      onChange={field => setRefuserMessge(field.target.value)}>

                                        </Form.Control>
                                        <Form.Control.Feedback type="invalid">Champ
                                            requis</Form.Control.Feedback>
                                    </Form.Group>
                                    <Row className="mt-3">
                                        <Col className="text-center">
                                            <Button type="submit"
                                                    className="btn btn-success mx-auto w-75">Envoyer</Button>
                                        </Col>
                                    </Row>
                                </Form>
                            </Col>
                        </Row>
                    </Container>
                </div>
            }
            <PageHeader title={"Liste de CV non validé"}/>
            <Row>
                <Col className="bg-light p-0" style={{height: 400}}>
                    <Table className="text-center" hover>
                        <thead className="bg-primary text-white">
                        <tr>
                            <th>Prénom</th>
                            <th>Nom</th>
                            <th>Département</th>
                            <th>CV</th>
                            <th>CV Valide</th>
                        </tr>
                        </thead>
                        <tbody className="bg-light">
                        {students.length === 0
                            ? <tr>
                                <td colSpan={5}>
                                    <p className="h1">Aucune évaluation</p>
                                </td>
                            </tr>
                            : students.map((student, index) => {
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