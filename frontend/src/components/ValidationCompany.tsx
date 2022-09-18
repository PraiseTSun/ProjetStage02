import React from "react";
import {Button, Col, Row} from "react-bootstrap";
import myDebugger from "../Debuger/CompanyDebugger.json";

const ValidationCompany = () => {
    const companys = myDebugger;

    function approve(){

    }

    function remove(){

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
                                <Button className="me-2" variant="success" onClick={() => approve()}>O</Button>
                                <Button variant="danger"  onClick={() => remove()}>X</Button>
                            </div>
                        </div>
                    </Row>
                );
            })}
        </Col>
    );
}

export default ValidationCompany;