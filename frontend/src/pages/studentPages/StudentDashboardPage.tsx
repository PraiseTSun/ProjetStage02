import React from "react";
import {Button, Row} from "react-bootstrap";
import {Link} from "react-router-dom";
import IUser from "../../models/IUser";

const StudentDashboard = ({user, deconnexion}: { user: IUser, deconnexion: Function }): JSX.Element => {

    return (
        <div className="min-vh-100">
            <Button className="btn btn-danger my-2" onClick={() => deconnexion()}>
                Déconnexion
            </Button>
            <h1 data-testid="titleStudentDashboard"
                className="p-5 text-center fw-bold text-white display-4">Bienvenue {user.firstName} {user.lastName}</h1>
            <Row className="d-flex justify-content-center">
                <Link to="/uploaderCV" className="btn btn-primary mb-3">Uploader Mon CV</Link>
                <Link to="/offres" className="btn btn-primary">Liste de stages</Link>
            </Row>
        </div>
    );
}

export default StudentDashboard;