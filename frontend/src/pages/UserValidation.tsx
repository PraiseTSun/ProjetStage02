import React, {useState} from "react";
import {Col, Row, ToggleButton, ToggleButtonGroup } from "react-bootstrap";
import ValidationStudent from "../components/ValidationStudent";
import ValidationCompany from "../components/ValidationCompany";
import ValidationGetionnaire from "../components/ValidationGetionnaire";

const UserValidation = () => {
    const [user, setUser] = useState("Student");

    const onValidation = async (id: string, type: string) =>{
        const res = await fetch(`http://localhost:8080/validate${type}/${id}`,
            {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                }
            });

        if (res.status == 409) {
            const data = await res.json();
            alert(data.error);
        }

        if (res.status == 201) {
            alert(type + " a été validé.");
        }
    }

    const onRemove = async (id: string, type: string) =>{
        const res = await fetch(`http://localhost:8080/remove${type}/${id}`,
            {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                }
            });

        if (res.status == 409) {
            const data = await res.json();
            alert(data.error);
        }

        if (res.status == 201) {
            alert(type + " a été supprimé.");
        }
    }

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
                {user == "Student" ? <ValidationStudent onValidation={onValidation}/> :
                 user == "Company" ? <ValidationCompany onValidation={onValidation}/> :
                     <ValidationGetionnaire/>
                }
            </Row>
        </Col>
    );
}

export default UserValidation;