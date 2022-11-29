import React, {useEffect, useState} from "react";
import {Button, Col, Row} from "react-bootstrap";
import IUser from "../../models/IUser";
import {putUnvalidatedCompanies} from "../../services/gestionnaireServices/GestionnaireFetchService";
import {generateAlert} from "../../services/universalServices/UniversalUtilService";

const ValidationCompany = ({
                               connectedUser,
                               onValidation,
                               onRemove
                           }: { connectedUser: IUser, onValidation: Function, onRemove: Function }) => {
    const user = "Company";
    const [companies, setCompanies] = useState<any[]>([]);

    function approve(id: string) {
        onValidation(id, user);
        setCompanies(companies.filter(company => company.id !== id));
    }

    function remove(id: string) {
        onRemove(id, user);
        setCompanies(companies.filter(company => company.id !== id));
    }

    useEffect(() => {
        const fetchUnvalidatedCompanies = async () => {
            const response = await putUnvalidatedCompanies(connectedUser.token)

            if (response.ok) {
                const data = await response.json();
                setCompanies(data);
            } else {
                generateAlert()
            }
        }

        fetchUnvalidatedCompanies()
    }, [connectedUser]);

    return (
        <Col className="mx-3">
            {companies.length === 0
                ? <p className="h1 text-center">Aucune compagnie</p>
                : companies.map((company: any, idx: number) => {
                    return (
                        <div key={company.id}>
                            <Row className="square border-bottom bg-light py-3">
                                <div className="d-flex justify-content-between">
                                    <div className="my-auto">{company.companyName}</div>
                                    <div className="my-auto">{company.department}</div>
                                    <div>
                                        <Button className="me-2" variant="success"
                                                onClick={() => approve(company.id)}>O</Button>
                                        <Button variant="danger" onClick={() => remove(company.id)}>X</Button>
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