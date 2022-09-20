import React, {useState} from "react";
import {Col, Row, ToggleButton, ToggleButtonGroup } from "react-bootstrap";
import ValidationStudent from "../components/ValidationStudent";
import ValidationCompany from "../components/ValidationCompany";
import ValidationGetionnaire from "../components/ValidationGetionnaire";

const UserValidation = () => {
    const [user, setUser] = useState("Student");
    const [info, setinfo] = useState([]);

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
    }

    const getInfo = async(type: string) =>{
        return fetch(`http://localhost:8080/unvalidated${type}`)
            .then(response => response.json());
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
                {user == "Student" ? <ValidationStudent getInfo={getInfo} onRemove={onRemove} onValidation={onValidation}/> :
                 user == "Company" ? <ValidationCompany getInfo={getInfo} onRemove={onRemove} onValidation={onValidation}/> :
                     <ValidationGetionnaire/>
                }
            </Row>
        </Col>
    );
}

export default UserValidation;