import React from "react";
import {Button, Col, Row} from "react-bootstrap";
import myDebugger from '../Debuger/StudentDebugger.json';

const ValidationStudent = ({ onValidation }: { onValidation: Function }) => {
    const students = myDebugger;
    
    function approve(id: string){
        onValidation(id, "Student")
    }

    function remove(){

    }

    return(
        <Col>
            {students.map((postData) => {
                return (
                    <Row className="square border-bottom bg-light py-3">
                        <div className="d-flex justify-content-between">
                            <div className="my-auto">{postData.firstName} {postData.lastName}</div>
                            <div className="my-auto">{postData.department}</div>
                            <div>
                                <Button className="me-2" variant="success" onClick={() => approve(postData.id)}>O</Button>
                                <Button variant="danger"  onClick={() => remove()}>X</Button>
                            </div>
                        </div>
                    </Row>
                );
            })}
        </Col>
    );
}

export default ValidationStudent;