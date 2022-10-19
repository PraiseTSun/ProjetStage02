import React, {useEffect, useState} from "react";
import {Button, Col, Row} from "react-bootstrap";
import IUser from "../../models/IUser";
import {putUnvalidatedCompanies} from "../../services/gestionnaireServices/GestionnaireFetchService";

const ValidationCompany = ({
                               connectedUser,
                               onValidation,
                               onRemove
                           }: { connectedUser: IUser, onValidation: Function, onRemove: Function }) => {
    const user = "Company";
    const [companies, setCompanies] = useState<any[]>([]);

    function approve(id: string, index: number) {
        onValidation(id, user);
        setCompanies(companies.splice(index + 1, 1));
    }

    function remove(id: string, index: number) {
        onRemove(id, user);
        setCompanies(companies.splice(index + 1, 1));
    }

    useEffect(() => {
        const fetchUnvalidatedCompanies = async () => {
            const response = await putUnvalidatedCompanies(connectedUser.token)

            if (response.ok) {
                const data = await response.json();
                setCompanies(data);
            } else {
                alert("Une erreur est survenue! Ressayez!");
                window.location.href = "/";
            }
        }
        
        fetchUnvalidatedCompanies()
    }, [connectedUser]);

    return (
        <Col className="mx-3">
            {companies.map((company: any, idx: number) => {
                return (
                    <div key={company.id}>
                        <Row className="square border-bottom bg-light py-3">
                            <div className="d-flex justify-content-between">
                                <div className="my-auto">{company.companyName}</div>
                                <div className="my-auto">{company.department}</div>
                                <div>
                                    <Button className="me-2" variant="success"
                                            onClick={() => approve(company.id, idx)}>O</Button>
                                    <Button variant="danger" onClick={() => remove(company.id, idx)}>X</Button>
                                </div>
                            </div>
                        </Row>
                    </div>
                );
            })}
        </Col>
    );
}

export default ValidationCompany;