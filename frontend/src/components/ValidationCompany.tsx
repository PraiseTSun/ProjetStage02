import React, {useEffect, useState} from "react";
import {Button, Col, Row} from "react-bootstrap";
import myDebugger from "../Debuger/CompanyDebugger.json";

const ValidationCompany = ({ onValidation, onRemove}: { onValidation: Function, onRemove: Function }) => {
    const user = "Company";
    const [comapnies, setCompanies] = useState<any[]>([]);

    function approve(id: string){
        onValidation(id, user);
    }

    function remove(id: string){
        onRemove(id, user);
    }

    useEffect(() => {
        fetch(`http://localhost:8080/unvalidatedCompanies`,
            {
                method: "GET",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                },
            })
            .then(response => response.json())
            .then(data => {
                setCompanies(data)
            });
    }, []);

    return(
        <Col>
            {comapnies.map((data) => {
                return (
                    <div key={data.id}>
                        <Row className="square border-bottom bg-light py-3">
                            <div className="d-flex justify-content-between">
                                <div className="my-auto">{data.name}</div>
                                <div className="my-auto">{data.department}</div>
                                <div>
                                    <Button className="me-2" variant="success" onClick={() => approve(data.id)}>O</Button>
                                    <Button variant="danger"  onClick={() => remove(data.id)}>X</Button>
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