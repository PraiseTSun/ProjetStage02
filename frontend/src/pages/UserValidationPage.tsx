import React, { useState } from "react";
import { Col, Container, Row, ToggleButton, ToggleButtonGroup } from "react-bootstrap";
import ValidationStudent from "../components/ValidationStudent";
import ValidationCompany from "../components/ValidationCompany";
import CreateGestionnaireForm from "../components/CreateGestionnaireForm";
import { Link } from "react-router-dom";
import IUser from "../models/IUser";

const UserValidation = ({ connectedUser }: { connectedUser: IUser }) => {
    const [user, setUser] = useState("Student");

    const onValidation = async (id: string, type: string) => {
        const res = await fetch(`http://localhost:8080/validate${type}/${id}`,
            {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ token: connectedUser.token })
            });

        if (res.status === 409) {
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
                },
                body: JSON.stringify({ token: connectedUser.token })
            });

        if (res.status === 409) {
            const data = await res.json();
            alert(data.error);
        }
    }

    return (
        <Container className="min-vh-100">
            <Row>
                <Col sm={2}>
                    <Link to="/" className="btn btn-primary my-3">Home</Link>
                </Col>
                <Col sm={8} className="text-center pt-2">
                    <h1 className="fw-bold text-white display-3 pb-2">Validation d'utilisateurs</h1>
                </Col>
                <Col sm={2}></Col>
            </Row>
            <Row>
                <ToggleButtonGroup className="" type="radio" name="options" defaultValue="Student">
                    <ToggleButton data-testid="studentInput" id="StudentValid" value="Student" onClick={() => setUser("Student")}>
                        Étudiants
                    </ToggleButton>
                    <ToggleButton id="CompanyValid" data-testid="companyInput" value="Company" onClick={() => setUser("Company")}>
                        Compagnies
                    </ToggleButton>
                    <ToggleButton id="GestionnairValid" value="Gestionnaire" onClick={() => {
                        setUser("Gestionnaire")
                    }}>
                        Gestionnaires
                    </ToggleButton>
                </ToggleButtonGroup>
            </Row>
            <Row>
                {user === "Student" ? <ValidationStudent connectedUser={connectedUser} onRemove={onRemove} onValidation={onValidation} /> :
                    user === "Company" ? <ValidationCompany connectedUser={connectedUser} onRemove={onRemove} onValidation={onValidation} /> :
                        <CreateGestionnaireForm user={connectedUser} />
                }
            </Row>
        </Container>
    );
}

export default UserValidation;