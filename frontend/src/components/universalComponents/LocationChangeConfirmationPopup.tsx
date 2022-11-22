import {Button, Col, Container, Row} from "react-bootstrap";
import React from "react";

const LocationChangeConfirmationPopup = ({
                                             message,
                                             locationOnConfirm
                                         }: { message: string, locationOnConfirm: string }): JSX.Element => {
    return (
        <Container
            className="vh-100 vw-100">
            <Row className="vh-100 m-0">
                <Col sm={4} className="text-center rounded p-3 m-auto bg-white">
                    <h1 className="text-success fw-bold">{message}</h1>
                    <Row className="px-5">
                        <Button onClick={() => {
                            window.location.href = locationOnConfirm;
                        }}>Ok</Button>
                    </Row>
                </Col>
            </Row>
        </Container>
    );
}

export default LocationChangeConfirmationPopup;