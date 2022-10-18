import {Button, Col, Container, Form, Row} from "react-bootstrap";
import React, {useState} from "react";
import {BeatLoader} from "react-spinners";
import IUser from "../models/IUser";
import {Link} from "react-router-dom";

const UploaderMonCV = ({user}: { user: IUser }) => {
    const [waiting, setWaiting] = useState<boolean>(false);
    const [isSelected, setIsSelected] = useState<boolean>(false);
    const [validated, setValidated] = useState<boolean>(false);
    const [cv, setCv] = useState<number[]>([0])

    const onSubmit = async (event: React.SyntheticEvent) => {
        const form: any = event.currentTarget;
        event.preventDefault();
        if (form.checkValidity()) {
            setWaiting(true)
            const headers = {
                method: "PUT",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({
                    studentId: user.id,
                    pdf: cv,
                    token: user.token
                })
            };
            const res = await fetch("http://localhost:8080/uploadStudentCV", headers)
            if (res.ok) {
                alert("CV envoyé")
            } else {
                alert("Une erreur est survenue")
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
    }
    if (waiting) {
        return (
            <div className="d-flex justify-content-center py-5 bg-light min-vh-100">
                <BeatLoader className="text-center" color="#292b2c" size={100} />
            </div>
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
                        {isSelected ?
                            (<div></div>) :
                            (<h5 className="text-danger mt-3">Choix votre CV</h5>)
                        }
                    </Row>
                    <Row className="m-4">
                        <Button data-testid="buttonid" type="submit"
                                className="btn btn-success mx-auto w-75">Envoyer</Button>
                    </Row>
                </Form>
            </Col>
        </Container>
    )
}
export default UploaderMonCV