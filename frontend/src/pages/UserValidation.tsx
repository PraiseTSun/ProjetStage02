import React from "react";
import {Container, Col, Row, ToggleButton, ToggleButtonGroup } from "react-bootstrap";

const UserValidation = () => {
    return (
        <Container fluid>
            <Col xs={1}>
                <ToggleButtonGroup type="radio" name="options" defaultValue={1}>
                    <ToggleButton id="StudentValid" value={1}>
                        Student
                    </ToggleButton>
                    <ToggleButton id="CompanyValid" value={2}>
                        Company
                    </ToggleButton>
                    <ToggleButton id="GestionnairValid" value={3}>
                        Gestionnaire
                    </ToggleButton>
                </ToggleButtonGroup>
            </Col>
            <Col xs={11}>

            </Col>
        </Container>
    );
}

export default UserValidation;