import React from "react";
import {Button, Col, Row} from "react-bootstrap";
import myDebugger from "../Debuger/CompanyDebugger.json";

const ValidationCompany = ({ onValidation, onRemove}: { onValidation: Function, onRemove: Function }) => {
    const companys = myDebugger;
    const user = "Company";

    function approve(id: string){
        onValidation(id, user);
    }

    function remove(id: string){
        onRemove(id, user);
    }

    return(
        <Col>
            {companys.map((postData) => {
                return (
                    <Row className="square border-bottom bg-light py-3">
                        <div className="d-flex justify-content-between">
                            <div className="my-auto">{postData.name}</div>
                            <div className="my-auto">{postData.department}</div>
                            <div>
                                <Button className="me-2" variant="success" onClick={() => approve(postData.id)}>O</Button>
                                <Button variant="danger"  onClick={() => remove(postData.id)}>X</Button>
                            </div>
                        </div>
                    </Row>
                );
            })}
        </Col>
    );
}

export default ValidationCompany;