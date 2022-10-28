import React, {useState} from "react";
import {Col, Container, Row, ToggleButton, ToggleButtonGroup} from "react-bootstrap";
import ValidationStudent from "../../components/gestionnaireComponents/ValidationStudent";
import ValidationCompany from "../../components/gestionnaireComponents/ValidationCompany";
import CreateGestionnaireForm from "../../components/gestionnaireComponents/CreateGestionnaireForm";
import {Link} from "react-router-dom";
import IUser from "../../models/IUser";
import {
    deleteRemoveCompany,
    deleteRemoveStudent,
    putValidateCompany,
    putValidateStudent
} from "../../services/gestionnaireServices/GestionnaireFetchService";
import {generateAlert} from "../../services/universalServices/UniversalUtilService";

const UserValidation = ({connectedUser}: { connectedUser: IUser }) => {
    const [user, setUser] = useState("Student");

    const onValidation = async (id: string, type: string) => {
        let response: Response

        if (type === "Student") {
            response = await putValidateStudent(id, connectedUser.token);
        } else if (type === "Company") {
            response = await putValidateCompany(id, connectedUser.token);
        }

        if (!response!.ok) {
            generateAlert()
        }
    }

    const onRemove = async (id: string, type: string) => {
        let response: Response

        if (type === "student") {
            response = await deleteRemoveStudent(id, connectedUser.token);
        } else if (type === "company") {
            response = await deleteRemoveCompany(id, connectedUser.token);
        }

        if (!response!.ok) {
            generateAlert()
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
                    <ToggleButton data-testid="studentInput" id="StudentValid" value="Student"
                                  onClick={() => setUser("Student")}>
                        Étudiants
                    </ToggleButton>
                    <ToggleButton id="CompanyValid" data-testid="companyInput" value="Company"
                                  onClick={() => setUser("Company")}>
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
                {user === "Student" ?
                    <ValidationStudent connectedUser={connectedUser} onRemove={onRemove} onValidation={onValidation}/> :
                    user === "Company" ? <ValidationCompany connectedUser={connectedUser} onRemove={onRemove}
                                                            onValidation={onValidation}/> :
                        <CreateGestionnaireForm connectedUser={connectedUser}/>
                }
            </Row>
        </Container>
    );
}

export default UserValidation;