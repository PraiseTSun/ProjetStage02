import React from "react";
import {Col, Row} from "react-bootstrap";
import {Link} from "react-router-dom";

const PageHeader = ({title}: { title: String }) => {
    return (
        <Row>
            <Col sm={2}>
                <Link to="/" className="btn btn-primary my-3">Home</Link>
            </Col>
            <Col sm={8} className="text-center pt-2">
                <h1 className="fw-bold text-white display-3 pb-2">{title}</h1>
            </Col>
            <Col sm={2}></Col>
        </Row>
    );
}

export default PageHeader;