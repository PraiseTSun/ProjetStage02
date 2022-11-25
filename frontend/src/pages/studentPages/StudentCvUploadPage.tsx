import {Button, Col, Container, Form, Row, Table} from "react-bootstrap";
import React, {useEffect, useState} from "react";
import {BeatLoader} from "react-spinners";
import IUser from "../../models/IUser";
import {Link} from "react-router-dom";
import {putStatusCv, putUploadStudentCV} from "../../services/studentServices/StudentFetchService";
import {generateAlert} from "../../services/universalServices/UniversalUtilService";
import CvStatus from "../../models/CvStatus";
import PdfComponent from "../../components/universalComponents/PdfComponent";

export const statusCV: CvStatus = {
    status: "",
    refusalMessage: ""
}

const StudentCvUploadPage = ({connectedUser}: { connectedUser: IUser }) => {
    const [waiting, setWaiting] = useState<boolean>(false);
    const [validated, setValidated] = useState<boolean>(false);
    const [cvToValidate, setCvToValidate] = useState<number[]>([0])
    const [isChoisi, setIsChoisi] = useState<boolean>(false)
    const [cvStatus, setCvStatus] = useState<CvStatus>(statusCV)
    const [showCV, setShowCV] = useState<boolean>(false)
    const [sentCv, setSentCv] = useState<boolean>(false)
    const [showCvToValidate, setShowCvToValidate] = useState<boolean>(false)

    useEffect(() => {
        const fetchStatusCV = async () => {
            await putStatusCv(connectedUser.id, connectedUser.token).then(async reponse => {
                if (reponse.status === 200) {
                    const data = await reponse.json()
                    setCvStatus(data)
                } else {
                    generateAlert()
                }
            })
        }
        fetchStatusCV()
    }, [connectedUser])

    const onSubmit = async (event: React.SyntheticEvent) => {
        const form: any = event.currentTarget;
        event.preventDefault();
        if (form.checkValidity()) {
            setWaiting(true)

            try {
                const response = await putUploadStudentCV(connectedUser.id, cvToValidate, connectedUser.token)

                if (response.ok) {
                    connectedUser.cvToValidate = JSON.stringify(cvToValidate);
                    setSentCv(true)
                    setWaiting(false)
                } else {
                    generateAlert()
                }
            } catch {
                generateAlert()
            }
        }
        setValidated(true);
    }

    const intToByteArray = (array: Uint8Array): number[] => {
        let bytes: number[] = []
        array.forEach(value => {
            bytes.push(value)
        })
        return bytes;
    };

    const uploadFile = async (file: File) => {
        const fileText = await file.arrayBuffer()
        const view = new Uint8Array(fileText)
        const array = intToByteArray(view)
        setCvToValidate(array)
        setIsChoisi(true)
    }

    const translateStatus = (refusalMessage: string): string => {
        switch (refusalMessage) {
            case "NOTHING":
                return "Aucun CV en attente "
            case "PENDING":
                return "En attente"
            case "ACCEPTED":
                return "Accepté"
            case "REFUSED":
                return "Refusé"
            default:
                return "Problem"
        }
    }

    if (showCV) {
        return (
            <PdfComponent pdf={new Uint8Array(JSON.parse(connectedUser.cv!))} setShowPdf={setShowCV}/>
        );
    }

    if (showCvToValidate) {
        return (
            <PdfComponent pdf={new Uint8Array(JSON.parse(connectedUser.cvToValidate!))}
                          setShowPdf={setShowCvToValidate}/>
        );
    }

    return (
        <Container className="min-vh-100">
            <Row>
                <Col sm={2}>
                    <Link to="/" className="btn btn-primary my-3">Home</Link>
                </Col>
                <Col sm={8} className="text-center pt-2">
                    <h1 className="fw-bold text-white display-3 pb-2">Mon curriculum vitae</h1>
                </Col>
                <Col sm={2}></Col>
            </Row>
            <Row>
                <Col sm={12} md={6} className="bg-white px-0" style={{minHeight: 200}}>
                    <Table className="text-center" hover>
                        <thead className="bg-primary text-white">
                        <tr>
                            <th>Mon Cv</th>
                            <th>Status</th>
                            <th>Message de refus</th>
                            <th>Cv à valider</th>
                        </tr>
                        </thead>
                        <tbody className="bg-white">
                        <tr>
                            <td className="text-center">
                                <Button variant="warning" disabled={connectedUser.cv?.length === 2}
                                        onClick={() => setShowCV(true)}>CV</Button>
                            </td>
                            <td className="text-center">{translateStatus(cvStatus.status)}</td>
                            <td className="text-center">{cvStatus.refusalMessage}</td>
                            <td data-testid="CvToValidate" className="text-center">
                                <Button variant="warning" disabled={connectedUser.cvToValidate?.length === 2}
                                        onClick={() => setShowCvToValidate(true)}>Cv à valider</Button>
                            </td>
                        </tr>
                        </tbody>
                    </Table>
                </Col>
                <Col sm={12} md={6} className="px-0">
                    <Form onSubmit={onSubmit} validated={validated} noValidate
                          className="bg-white border border-light border-5 p-2"
                          style={{minHeight: 200}}>
                        {sentCv &&
                            <h5 className="text-success text-center fw-bold">Votre CV a été envoyé avec succès</h5>}
                        {(!isChoisi || !sentCv) &&
                            <h5 className="text-danger text-center fw-bold">Choisissez votre CV</h5>}
                        <Form.Group className="">
                            <input data-testid="uploaderMonCV" className="form-control" accept="application/pdf"
                                   name="file"
                                   required type="file" onChange={async (e) => {
                                await uploadFile(e.target.files![0]);
                            }}/>
                            <Form.Control.Feedback type="invalid">Champ requis</Form.Control.Feedback>
                        </Form.Group>
                        <Row className="m-4">
                            <Col className="text-center">
                                {
                                    waiting
                                        ? <BeatLoader color="#0275d8 " size={25}/>
                                        : <Button data-testid="buttonid" type="submit"
                                                  className="btn btn-success mx-auto w-75">Envoyer</Button>
                                }
                            </Col>
                        </Row>
                    </Form>
                </Col>
            </Row>
        </Container>
    )
}
export default StudentCvUploadPage