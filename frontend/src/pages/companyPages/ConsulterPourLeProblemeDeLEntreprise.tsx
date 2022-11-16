import {Button, Container, Form, Row} from "react-bootstrap";
import PageHeader from "../../components/universalComponents/PageHeader";
import {BeatLoader} from "react-spinners";
import IUser from "../../models/IUser";
import React, {useState} from "react";

const ConsulterPourLeProblemeDeLEntreprise = ({connectedUser} : {connectedUser : IUser}) => {
    const [waiting, setWaiting] = useState<boolean>(false);
    const [validated, setValidated] = useState<boolean>(false);
    const [commentaires, setCommentaires] = useState<string>("");

    const onSubmit = async (event: React.SyntheticEvent) => {

        const form: any = event.currentTarget;
        event.preventDefault();

        if (form.checkValidity()) {
            setWaiting(true)


        }
    }

    if (waiting) {
        return (
            <div className="d-flex justify-content-center py-5 bg-light">
                <BeatLoader className="text-center" color="#292b2c" size={100}/>
            </div>
        );
    }
    return (
        <Container className="min-vh-100">
            <PageHeader title={"Consulter pour le problème"}/>
            <Form onSubmit={onSubmit} validated={validated} noValidate>
                <Form.Group>
                    <Form.Label className="fw-bold mt-2 h5">Commentaires</Form.Label>
                    <Form.Control as="textarea" type="text" minLength={2} rows={5} required
                                  value={commentaires}
                                  onChange={e => setCommentaires(e.target.value)}></Form.Control>
                    <Form.Control.Feedback type="invalid">Champ
                        requis</Form.Control.Feedback>
                </Form.Group>
                <Row className="mt-3  mx-1">
                    <Button type="submit"
                            className="btn btn-success mx-auto">Envoyer</Button>
                </Row>
            </Form>
        </Container>
    )
}
export default ConsulterPourLeProblemeDeLEntreprise
