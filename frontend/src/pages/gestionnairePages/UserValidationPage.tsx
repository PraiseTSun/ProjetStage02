import React, {useState} from "react";
import {Col, Container, Row, ToggleButton, ToggleButtonGroup} from "react-bootstrap";
import ValidationStudent from "../../components/gestionnaireComponents/ValidationStudent";
import ValidationCompany from "../../components/gestionnaireComponents/ValidationCompany";
import CreateGestionnaireForm from "../../components/gestionnaireComponents/CreateGestionnaireForm";
import IUser from "../../models/IUser";
import {
    deleteRemoveCompany,
    deleteRemoveStudent,
    putValidateCompany,
    putValidateStudent
} from "../../services/gestionnaireServices/GestionnaireFetchService";
import {generateAlert} from "../../services/universalServices/UniversalUtilService";
import PageHeader from "../../components/universalComponents/PageHeader";

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
            <PageHeader title={"Validation d'utilisateurs"}/>
            <Row>
                <Col className="bg-light p-0" style={{minHeight: 300}}>
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
                        {user === "Student" ?
                            <ValidationStudent connectedUser={connectedUser} onRemove={onRemove}
                                               onValidation={onValidation}/> :
                            user === "Company" ? <ValidationCompany connectedUser={connectedUser} onRemove={onRemove}
                                                                    onValidation={onValidation}/> :
                                <CreateGestionnaireForm connectedUser={connectedUser}/>
                        }
                    </Row>
                </Col>
            </Row>
        </Container>
    );
}

export default UserValidation;