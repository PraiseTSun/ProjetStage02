import {Button, Col, Container, Form, Row} from "react-bootstrap";
import React, {useEffect, useState} from "react";
import {BeatLoader} from "react-spinners";
import IUser from "../../models/IUser";
import {Link} from "react-router-dom";
import {putStatusCv, putUploadStudentCV} from "../../services/studentServices/StudentFetchService";
import {generateAlert} from "../../services/universalServices/UniversalUtilService";
import CvStatus from "../../models/CvStatus";
import {Viewer} from "@react-pdf-viewer/core";

export const statusCV : CvStatus = {
    status: "",
    refusalMessage: ""
}

const StudentCvUploadPage = ({connectedUser}: { connectedUser: IUser }) => {
    const [waiting, setWaiting] = useState<boolean>(false);
    const [validated, setValidated] = useState<boolean>(false);
    const [cv, setCv] = useState<number[]>([0])
    const [isChoisi, setIsChoisi] = useState<boolean>(false)
    const [cvStatus, setCvStatus] = useState(statusCV)
    // const [z, setCvCourant] = useState<Uint8Array>(new Uint8Array(JSON.parse(connectedUser.cvToValidate)));
    const [showCV, setShowCV] = useState<boolean>(false)

    useEffect(() => {
        const fetcheStatusCV = async () => {
            await putStatusCv(connectedUser.id, connectedUser.token).then(async reponse => {
                if (reponse.status == 200) {
                    const data = await reponse.json()
                    setCvStatus(data)
                } else {
                    generateAlert()
                }
            })
        }
        fetcheStatusCV()
    }, [])
    const onSubmit = async (event: React.SyntheticEvent) => {
        const form: any = event.currentTarget;
        event.preventDefault();
        if (form.checkValidity()) {
            setWaiting(true)

            try {
                const response = await putUploadStudentCV(connectedUser.id, cv, connectedUser.token)

                if (response.ok) {
                    alert("CV envoyé")
                } else {
                    generateAlert()
                }
            } catch {
                generateAlert()
            }
            setWaiting(false);
            window.location.href = "/"
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
        setCv(array)
        setIsChoisi(true)
    }
    if (waiting) {
        return (
            <div className="d-flex justify-content-center py-5 bg-light min-vh-100">
                <BeatLoader className="text-center" color="#292b2c" size={100}/>
            </div>
        );
    }


    async function getCv(): Promise<void> {
        if(connectedUser.cvToValidate == null){
            alert("Il y a pas de CV a valider courant, svp envoyez votre CV")
            return ;
        }
        // const enc = new TextEncoder(); // always utf-8
        // console.log(connectedUser.cv)
        // setCvCourant(new Uint8Array(JSON.parse(data.pdf)));
        setShowCV(true);

    }
    if (showCV) {
        return (
            <Container className="min-vh-100 bg-white p-0">
                <div className="bg-dark p-2">
                    <Button className="Btn btn-primary" onClick={() => setShowCV(false)}>
                        Fermer
                    </Button>
                </div>
                <div>
                    <Viewer fileUrl={new Uint8Array(JSON.parse(connectedUser.cvToValidate!))}/>
                </div>
            </Container>
        );
    }
    return (
        <Container className="justify-content-center min-vh-100">
            <Col className="col-12 ">
                <Row>
                    <Col sm={2}>
                        <Link to="/" className="btn btn-primary my-3">Home</Link>
                    </Col>
                    <Col sm={8} className="text-center pt-2">
                        <h1 className="fw-bold text-white display-3 pb-2">Téléverser CV</h1>
                    </Col>
                    <Col sm={2}></Col>
                </Row>
                <Form onSubmit={onSubmit} validated={validated} noValidate className="">
                    <Row>
                        <Form.Group className="">
                            <input data-testid="uploaderMonCV" className="form-control" accept="application/pdf"
                                   name="file"
                                   required type="file" onChange={async (e) => {
                                await uploadFile(e.target.files![0]);
                            }}/>
                            <Form.Control.Feedback type="invalid">Champ requis</Form.Control.Feedback>
                        </Form.Group>
                        {isChoisi ? <h5></h5> : <h5 className="text-danger mt-3">Choix votre CV</h5>}
                    </Row>
                    <Row className="m-4">
                        <Button data-testid="buttonid" type="submit"
                                className="btn btn-success mx-auto w-75">Envoyer</Button>
                    </Row>
                </Form>
                <table className="table table-bordered pt-2 text-white">
                    <tbody>
                    <tr>
                        <th>Mon Cv</th>
                        <td className="text-center">
                            <Button className="btn" onClick={async () => await getCv()}>CV</Button>
                        </td>
                    </tr>
                    <tr>
                        <th>Status :</th>
                        <td  className="text-center">{cvStatus.status}</td>
                    </tr>
                    <tr>
                        <th> Refusal Message :</th>
                        <td>{cvStatus.refusalMessage}</td>
                    </tr>
                    </tbody>
                </table>
            </Col>
        </Container>
    )
}
export default StudentCvUploadPage