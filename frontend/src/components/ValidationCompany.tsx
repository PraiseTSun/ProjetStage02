import React, {useEffect, useState} from "react";
import {Button, Col, Row} from "react-bootstrap";
import myDebugger from "../Debuger/CompanyDebugger.json";

const ValidationCompany = ({ onValidation, onRemove}: { onValidation: Function, onRemove: Function }) => {
    const user = "Company";
    const [comapnies, setCompanies] = useState<any[]>([]);

    function approve(id: string, index: number){
        onValidation(id, user);
        setCompanies(comapnies.splice(index + 1, 1));
    }

    function remove(id: string, index : number){
        onRemove(id, user);
        setCompanies(comapnies.splice(index + 1, 1));
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
            {comapnies.map((data, idx) => {
                return (
                    <div key={data.id}>
                        <Row className="square border-bottom bg-light py-3">
                            <div className="d-flex justify-content-between">
                                <div className="my-auto">{data.name}</div>
                                <div className="my-auto">{data.department}</div>
                                <div>
                                    <Button className="me-2" variant="success" onClick={() => approve(data.id, idx)}>O</Button>
                                    <Button variant="danger"  onClick={() => remove(data.id, idx)}>X</Button>
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