import React from "react";
import {Col, Row, ToggleButton, ToggleButtonGroup } from "react-bootstrap";

const UserValidation = () => {
    return (
        <Col className="vh-100 p-lg-5">
            <Row>
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
            </Row>
        </Col>
    );
}

export default UserValidation;