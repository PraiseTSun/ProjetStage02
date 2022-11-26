import React from "react";
import {Button, Row} from "react-bootstrap";
import {Link} from "react-router-dom";
import IUser from "../../models/IUser";
import ReportButton from "../../components/universalComponents/ReportButton";

const StudentDashboard = ({user, deconnexion}: { user: IUser, deconnexion: Function }): JSX.Element => {

    return (
        <div className="min-vh-100">
            <Button className="btn btn-danger my-2" onClick={() => deconnexion()}>
                Déconnexion
            </Button>
            <h1 data-testid="titleStudentDashboard"
                className="pt-5 text-center fw-bold text-white display-4">Bienvenue {user.firstName} {user.lastName}</h1>
            <ReportButton/>
            <Row className="d-flex justify-content-center">
                <Link to="/uploaderCV" className="btn btn-outline-primary text-white mb-3">Mon curriculum vitae</Link>
                <Link to="/offres" className="btn btn-outline-primary text-white mb-3">Offres de stage</Link>
                <Link to="/myContracts" className="btn btn-outline-primary text-white mb-3">Mes contrats</Link>
            </Row>
        </div>
    );
}

export default StudentDashboard;