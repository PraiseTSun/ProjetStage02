import React from "react";
import { Row, Button } from "react-bootstrap";
import { Link } from "react-router-dom";
import IUser from "../models/IUser";

const GestionnaireDashboard = ({ user, deconnexion }: { user: IUser, deconnexion: Function }): JSX.Element => {

    return (
        <div className="vh-100">
            <Button className="btn btn-danger my-2" onClick={() => deconnexion()}>
                Déconnexion
            </Button>
            <h1 className="p-5 text-center ">Bienvenue {user.firstName} {user.lastName}</h1>
            <Row className="d-flex justify-content-center">
                <Link to="/userValidation" className="btn btn-primary">Validation des utilisateurs</Link>
                <Link to="/cvValidation" className="btn btn-primary mt-3">Validation des curriculums vitae des étudiants</Link>
                <Link to="/validerNouvelleOffre" className="btn btn-primary mt-3">Validation nouvelle offre stage</Link>
            </Row>
        </div>
    );
}

export default GestionnaireDashboard;