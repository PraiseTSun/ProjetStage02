import {Button, Col, Container, Form, Row} from "react-bootstrap";
import React from "react";
import {BeatLoader} from "react-spinners";
import {generateAlert} from "../../services/universalServices/UniversalUtilService";
import {postCreateInterview} from "../../services/companyServices/CompanyFetchService";
import IUser from "../../models/IUser";
import IInterview from "../../models/IInterview";

const InterviewDateForm = ({
                               connectedUser,
                               offerId,
                               studentId,
                               setShowDateSelector,
                               interviews,
                               setInterviews
                           }: {
    connectedUser: IUser,
    offerId: string,
    studentId: string,
    interviews: IInterview[],
    setInterviews: Function,
    setShowDateSelector: Function
}): JSX.Element => {
    const MIN_DATE = new Date().toISOString().split(/:[0-9][0-9]\./)[0];
    const MAX_DATE = new Date(new Date().setMonth(new Date().getMonth() + 1))
        .toISOString().split(/:[0-9][0-9]\./)[0];
    const [firstDate, setFirstDate] = React.useState<string>("")
    const [secondDate, setSecondDate] = React.useState<string>("")
    const [thirdDate, setThirdDate] = React.useState<string>("")
    const [waiting, setWaiting] = React.useState<boolean>(false)

    const onSubmit = async (event: React.FormEvent<HTMLFormElement>): Promise<void> => {
        event.preventDefault();

        try {
            setWaiting(true)
            const response: Response = await postCreateInterview(
                [firstDate, secondDate, thirdDate],
                connectedUser.token,
                connectedUser.id,
                offerId,
                studentId
            );

            if (response.ok) {
                const data: IInterview = await response.json();
                setInterviews([...interviews, data])
                setShowDateSelector(false)
            } else {
                generateAlert()
            }

        } catch {
            generateAlert()
        }
    }


    return (
        <Container>
            <Row>
                <Col className="bg-white">
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
                                <Form.Control required type="datetime-local" min={MIN_DATE} max={MAX_DATE}
                                              onChange={event => setFirstDate(event.target.value)}/>
                            </Form.Group>
                            <Form.Group className="mt-2">
                                <Form.Label>Deuxième disponibilité</Form.Label>
                                <Form.Control required type="datetime-local" min={MIN_DATE} max={MAX_DATE}
                                              onChange={event => setSecondDate(event.target.value)}/>
                            </Form.Group>
                            <Form.Group className="mt-2">
                                <Form.Label>Troisième disponibilité</Form.Label>
                                <Form.Control required type="datetime-local" min={MIN_DATE} max={MAX_DATE}
                                              onChange={event => setThirdDate(event.target.value)}/>
                            </Form.Group>
                            {waiting
                                ? <BeatLoader className="mt-2" color="#292b2c"/>
                                : <Button className="mt-2" type="submit">Soumettre</Button>}
                        </Form>
                    </div>
                </Col>
            </Row>
        </Container>
    );
}

export default InterviewDateForm;