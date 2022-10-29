import React from "react";
import {Button, Row} from "react-bootstrap";
import {Link} from "react-router-dom";
import IUser from "../../models/IUser";

const GestionnaireDashboard = ({user, deconnexion}: { user: IUser, deconnexion: Function }): JSX.Element => {

    return (
        <div className="min-vh-100">
            <Button className="btn btn-danger my-2" onClick={() => deconnexion()}>
                Déconnexion
            </Button>
            <h1 className="p-5 text-center fw-bold text-white display-4">Bienvenue {user.firstName} {user.lastName}</h1>
            <Row className="d-flex justify-content-center">
                <Link to="/userValidation" className="btn btn-primary">Validation des utilisateurs</Link>
                <Link to="/cvValidation" className="btn btn-primary mt-3">Validation des curriculums vitae des
                    étudiants</Link>
                <Link to="/validerNouvelleOffre" className="btn btn-primary mt-3">Validation nouvelle offre stage</Link>
                <Link to="/offerHistory" className="btn btn-primary mt-3">Historique des offres</Link>
                <Link to="/acceptationsValidation" className="btn btn-primary mt-3">Listes des étudiants acceptés</Link>
            </Row>
        </div>
    );
}

export default GestionnaireDashboard;