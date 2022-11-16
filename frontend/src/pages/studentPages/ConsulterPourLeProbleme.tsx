import {Container, Form} from "react-bootstrap";
import PageHeader from "../../components/universalComponents/PageHeader";
import React, {useState} from "react";
import {BeatLoader} from "react-spinners";
import IUser from "../../models/IUser";

const ConsulterPourLeProbleme = ({connectedUser} : {connectedUser : IUser}) => {
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
                    <Form.Control as="textarea" type="text" minLength={2} required
                                  value={commentaires}
                                  onChange={e => setCommentaires(e.target.value)}></Form.Control>
                    <Form.Control.Feedback type="invalid">Champ
                        requis</Form.Control.Feedback>
                </Form.Group>
            </Form>
        </Container>
    )
}
export default ConsulterPourLeProbleme