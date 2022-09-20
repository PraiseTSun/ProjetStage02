import React, {useEffect, useState} from "react";
import {Button, Col, Row} from "react-bootstrap";
import myDebugger from '../Debuger/StudentDebugger.json';

const ValidationStudent = ({ onValidation, onRemove }: { onValidation: Function, onRemove : Function}) => {
    const user = "Student";
    const [students, setStudents] = useState<any[]>([]);

    function approve(id: string, index: number){
        onValidation(id, user);
    }

    function remove(id: string){
        onRemove(id, user);
    }

    useEffect(() => {
        fetch(`http://localhost:8080/unvalidatedStudents`,
            {
                method: "GET",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                },
            })
            .then(response => response.json())
            .then(data => {
                setStudents(data)
            });
    }, []);

    return(
        <Col>
            {students.map((data, idx) => {
                return (
                    <div key={data.id}>
                        <Row className="square border-bottom bg-light py-3">
                            <div className="d-flex justify-content-between">
                                <div className="my-auto">{data.firstName} {data.lastName}</div>
                                <div className="my-auto">{data.department}</div>
                                <div>
                                    <Button className="me-2" variant="success" onClick={() => approve(data.id, idx)}>O</Button>
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

export default ValidationStudent;