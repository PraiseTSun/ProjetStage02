import {Button, Col, Container, Form, Row} from "react-bootstrap";
import React from "react";

const InterviewDateForm = ({setShowDateSelector}: { setShowDateSelector: Function }): JSX.Element => {
    const MIN_DATE = new Date().toISOString().split(/:[0-9][0-9]\./)[0];
    const [firstDate, setFirstDate] = React.useState<string>("")
    const [secondDate, setSecondDate] = React.useState<string>("")
    const [thirdDate, setThirdDate] = React.useState<string>("")

    const onSubmit = async (event: React.FormEvent<HTMLFormElement>): Promise<void> => {
        event.preventDefault();

        // TODO : Send the dates to the backend
        console.log(firstDate)
        console.log(secondDate)
        console.log(thirdDate)
    }


    return (
        <Container>
            <Row className="min-vh-100">
                <Col sm={8} className="m-auto bg-white">
                    <Row className="bg-dark p-2">
                        <Col sm={1}>
                            <Button variant="danger" onClick={() => setShowDateSelector(false)}>
                                Annuler
                            </Button>
                        </Col>
                    </Row>
                    <div>
                        <Form className="p-3" onSubmit={event => onSubmit(event)}>
                            <Form.Group className="mt-2">
                                <Form.Label>Première disponibilité</Form.Label>
                                <Form.Control required type="datetime-local" min={MIN_DATE}
                                              onChange={event => setFirstDate(event.target.value)}/>
                            </Form.Group>
                            <Form.Group className="mt-2">
                                <Form.Label>Deuxième disponibilité</Form.Label>
                                <Form.Control required type="datetime-local" min={MIN_DATE}
                                              onChange={event => setSecondDate(event.target.value)}/>
                            </Form.Group>
                            <Form.Group className="mt-2">
                                <Form.Label>Troisième disponibilité</Form.Label>
                                <Form.Control required type="datetime-local" min={MIN_DATE}
                                              onChange={event => setThirdDate(event.target.value)}/>
                            </Form.Group>
                            <Button className="mt-2" type="submit">Soumettre</Button>
                        </Form>
                    </div>
                </Col>
            </Row>
        </Container>
    );
}

export default InterviewDateForm;