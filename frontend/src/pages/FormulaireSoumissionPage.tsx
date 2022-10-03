import React from "react";
import {useState} from 'react';
import {Button, Col, Container, Form, Row} from "react-bootstrap";
import {BeatLoader} from "react-spinners";
import IUser from "../models/IUser";

declare type FormControlElement = HTMLInputElement | HTMLTextAreaElement;

const FormulaireSoumissionPage = ({user}:{user:IUser}): JSX.Element => {
    const [waiting, setWaiting] = useState(false);
    const [validated, setValidated] = useState(false);
    const [company, setCompany] = useState("")
    const [department, setDepartment] = useState("")
    const [poste, setPoste] = useState("")
    const [hoursPerWeek, setHoursPerWeek] = useState(40)
    const [address, setAddress] = useState("")
    const [pdf, setPdf] = useState([0])

    const onSubmit = async (event: React.SyntheticEvent): Promise<void> => {
        const form: any = event.currentTarget;
        event.preventDefault();
        if (form.checkValidity()) {
            setWaiting(true)
            const obj = {
                nomDeCompagnie: company,
                department: department,
                position: poste,
                heureParSemaine: hoursPerWeek,
                adresse: address,
                pdf: pdf,
                token: user.token
            }

            const headers = {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify(obj)
            };
            const res = await fetch("http://localhost:8080/createOffre", headers)
            if (res.ok) {
                alert("Formulaire envoyé")
            }
            setWaiting(false);
            location.href = "/"
        }
        setValidated(true);
    }

    const hoursRegEx = new RegExp(
        '^[0-9]{0,3}$'
    );
    const setHoursPerWeekFromField = (event: React.ChangeEvent<FormControlElement>) => {
        if (hoursRegEx.test(event.target.value)) {
            setHoursPerWeek(Number.parseInt(event.target.value))
        } else {
            setHoursPerWeek(hoursPerWeek)
        }
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

    const uploadFile = async (file: File) => {
        const fileText = await file.arrayBuffer()
        const view = new Uint8Array(fileText)
        const array = intToByteArray(view)
        setPdf(array)
    }
    if (waiting) {
        return (
            <div className="d-flex justify-content-center py-5 bg-light">
                <BeatLoader className="text-center" color="#292b2c" size={100}/>
            </div>
        );
    }
    return (
        <Container className="d-flex justify-content-center">

            <Row className="col-12 my-3 card">
                <h3 className="card-header text-center">Formulaire de soumission de stage</h3>
                <Form className="card-body p-3" onSubmit={onSubmit} validated={validated} noValidate>
                    <Form.Group>
                        <Form.Label className="fw-bold h5">Nom de la compagnie</Form.Label>
                        <Form.Control type="text" required value={company}
                                      onChange={field => setCompany(field.target.value)}></Form.Control>
                        <Form.Control.Feedback type="invalid">Champ requis</Form.Control.Feedback>
                    </Form.Group>
                    <Form.Group>
                        <Form.Label className="fw-bold mt-2 h5">Département</Form.Label>
                        <Form.Select required
                                     value={department} onChange={(e) => setDepartment(e.target.value)}>
                            <option hidden value="" disabled>Choix d'un département</option>
                            <option value="Techniques de linformatique">
                                Technique de l'informatique
                            </option>
                            <option value="Techniques de la logistique du transport">
                                Technique de la logistique du transport
                            </option>
                        </Form.Select>
                        <Form.Control.Feedback type="invalid">Champ requis</Form.Control.Feedback>
                    </Form.Group>
                    <Form.Group>
                        <Form.Label className="fw-bold h5">Poste</Form.Label>
                        <Form.Control type="text" required value={poste}
                                      onChange={field => setPoste(field.target.value)}></Form.Control>
                        <Form.Control.Feedback type="invalid">Champ requis</Form.Control.Feedback>
                    </Form.Group>
                    <Form.Group>
                        <Form.Label className="fw-bold h5">Heures par semaine</Form.Label>
                        <Form.Control type="number" min="1" max="40" required value={hoursPerWeek}
                                      onChange={field => setHoursPerWeekFromField(field)}></Form.Control>
                        <Form.Control.Feedback type="invalid">Nombre d'heures entre 0 et 40</Form.Control.Feedback>
                    </Form.Group>
                    <Form.Group>
                        <Form.Label className="fw-bold h5">Adresse</Form.Label>
                        <Form.Control type="text" required value={address}
                                      onChange={field => setAddress(field.target.value)}></Form.Control>
                        <Form.Control.Feedback type="invalid">Champ requis</Form.Control.Feedback>
                    </Form.Group>
                    <Form.Group>
                        <Form.Label className="fw-bold h5">Document PDF</Form.Label>
                        <input className="form-control" accept=".pdf"
                               required type="file" onChange={(e) => {
                            uploadFile(e.target.files![0]);
                        }}/>
                        <Form.Control.Feedback type="invalid">Champ requis</Form.Control.Feedback>
                    </Form.Group>
                    <Row className="mt-3">
                        <Col className="text-center">
                            <Button type="submit" className="btn btn-success mx-auto w-75">Envoyer</Button>
                        </Col>
                    </Row>
                </Form>
            </Row>
        </Container>
    );


}
export default FormulaireSoumissionPage


