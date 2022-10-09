import {Button, Col, Container, Form, Row} from "react-bootstrap";
import React, {useState} from "react";
import {BeatLoader} from "react-spinners";
import IUser from "../models/IUser";

const UploaderMonCV = ({user} : {user : IUser}) => {
    const [waiting, setWaiting] = useState(false);
    const [isSelected, setIsSelected] = useState(false);
    const [validated, setValidated] = useState(false);
    const [cv, setCv] = useState([0])
    const onSubmit = async (event: React.SyntheticEvent) => {
        const form: any = event.currentTarget;
        event.preventDefault();
        if (form.checkValidity()) {
            setWaiting(true)
            const obj = {
                id : user.id,
                pdf: cv,
                token : user.token
            }
            console.log(obj)
            const headers = {
                method: "PUT",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify(obj)
            };
            const res = await fetch("http://localhost:8080/uploadStudentCV", headers)
            if (res.ok) {
                alert("CV envoyé")
            }
            setWaiting(false);
            location.href = "/"
        }
        setValidated(true);
    }

    //source: https://stackoverflow.com/questions/8482309/converting-javascript-integer-to-byte-array-and-back
    const intToByteArray = (array: Uint8Array) => {
        let bytes: number[] = []
        for (let i = 0; i < array.length; i++) {

            let long = array[i]
            let byteArray = [0, 0, 0, 0];
            // we want to represent the input as a 4-bytes array
            for (let index = 0; index < byteArray.length; index++) {
                let byte = long & 0xff;
                byteArray[index] = byte;
                long = (long - byte) / 256;
            }
            bytes.push(...byteArray)
        }
        return bytes;
    };

    const uploadFile = async (file:File) => {
        const fileText = await file.arrayBuffer()
        const view = new Uint8Array(fileText)
        const array = intToByteArray(view)
        setCv(array)
        setIsSelected(true)
    }

    if (waiting) {
        return (
            <div className="d-flex justify-content-center py-5 bg-light">
                <BeatLoader className="text-center" color="#292b2c" size={100}/>
            </div>
        );
    }

    return (
        <Container className="justify-content-center">
            <Col className="col-12 ">
                <h4 className="fw-bold  mb-4 text-center">Document CV</h4>
                <Form onSubmit={onSubmit} validated={validated} noValidate className="">
                    <Row>
                        <Form.Group className="">
                            <input data-testid="uploaderMonCV" className="form-control" accept=".pdf" name="file"
                                   required type="file" onChange={(e) => {
                                uploadFile(e.target.files![0]);
                            }}/>
                            <Form.Control.Feedback type="invalid">Champ requis</Form.Control.Feedback>
                        </Form.Group>
                        {isSelected ?
                            (<div></div>) :
                            (<h5 className="text-danger mt-3">Choix votre CV</h5>)
                        }
                    </Row>
                    <Row className="m-4">
                        <Button data-testid="buttonid" type="submit" className="btn btn-success mx-auto w-75">Envoyer</Button>
                    </Row>
                </Form>
            </Col>
        </Container>
    )
}
export default UploaderMonCV