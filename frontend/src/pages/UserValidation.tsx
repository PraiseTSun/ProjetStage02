import React, {useState} from "react";
import {Col, Row, ToggleButton, ToggleButtonGroup } from "react-bootstrap";
import ValidationStudent from "../components/ValidationStudent";
import ValidationCompany from "../components/ValidationCompany";
import ValidationGetionnaire from "../components/ValidationGetionnaire";

const UserValidation = () => {
    const [user, setUser] = useState("Student");

    return (
        <Col className="vh-100 p-lg-5">
            <Row className="p-0">
                <ToggleButtonGroup type="radio" name="options" defaultValue="Student">
                    <ToggleButton id="StudentValid" value="Student" onClick={() => setUser("Student")}>
                        Student
                    </ToggleButton>
                    <ToggleButton id="CompanyValid" value="Company"  onClick={() => setUser("Company")}>
                        Company
                    </ToggleButton>
                    <ToggleButton id="GestionnairValid" value="Gestionnaire"  onClick={() => setUser("Gestionnaire")}>
                        Gestionnaire
                    </ToggleButton>
                </ToggleButtonGroup>
            </Row>
            <Row>
                {user == "Student" ? <ValidationStudent/> :
                 user == "Company" ? <ValidationCompany/> :
                     <ValidationGetionnaire/>
                }
            </Row>
        </Col>
    );
}

export default UserValidation;