import React, { useState } from "react";
import { Col, Container, Row, ToggleButton, ToggleButtonGroup } from "react-bootstrap";
import ValidationStudent from "../components/ValidationStudent";
import ValidationCompany from "../components/ValidationCompany";
import ValidationGestionnaire from "../components/CreateGestionnaireForm";
import { Link } from "react-router-dom";

const UserValidation = () => {
    const [user, setUser] = useState("Student");

    const onValidation = async (id: string, type: string) => {
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

    const onRemove = async (id: string, type: string) => {
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

    return (
        <Container className="">
            <Link to="/" className="btn btn-primary my-3">Home</Link>
            <Row>
                <ToggleButtonGroup className="" type="radio" name="options" defaultValue="Student">
                    <ToggleButton id="StudentValid" value="Student" onClick={() => setUser("Student")}>
                        Étudiants
                    </ToggleButton>
                    <ToggleButton id="CompanyValid" value="Company" onClick={() => setUser("Company")}>
                        Compagnies
                    </ToggleButton>
                    <ToggleButton id="GestionnairValid" value="Gestionnaire" onClick={() => setUser("Gestionnaire")}>
                        Gestionnaires
                    </ToggleButton>
                </ToggleButtonGroup>
            </Row>
            <Row>
                {user == "Student" ? <ValidationStudent onRemove={onRemove} onValidation={onValidation} /> :
                    user == "Company" ? <ValidationCompany onRemove={onRemove} onValidation={onValidation} /> :
                        <ValidationGestionnaire />
                }
            </Row>
        </Container>
    );
}

export default UserValidation;