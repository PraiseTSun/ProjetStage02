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
            <h1 className="p-5 pb-3 text-center fw-bold text-white display-4">Bienvenue {user.firstName} {user.lastName}</h1>
            <Row sm={10}>
                <Link to="/userValidation" className="btn text-white btn-outline-primary">Validation des
                    utilisateurs</Link>
                <Link to="/cvValidation" className="btn btn-outline-primary text-white mt-3">Validation des
                    curriculums
                    vitae des
                    étudiants</Link>
                <Link to="/validerNouvelleOffre" className="btn btn-outline-primary text-white mt-3">Validation des
                    nouvelles offres de
                    stage</Link>
                <Link to="/offerHistory" className="btn btn-outline-primary text-white mt-3">Historique des
                    offres</Link>
                <Link to="/evaluerLeMilieuDeStage" className="btn btn-outline-primary text-white mt-3">Évaluation
                    des
                    milieux de
                    stage</Link>
                <Link to="/acceptationsValidation" className="btn btn-outline-primary text-white mt-3">Création
                    d'ententes de stage</Link>
                <Link to="/consulterEvaluation" className="btn btn-outline-primary text-white mt-3">Consulter les
                    évaluations des
                    stages</Link>
                <Link to="/consulterEvaluationParEntreprise" className="btn btn-outline-primary text-white mt-3">Consulter
                    les évaluations
                    des étudiants</Link>
                <Link to="/signerententedestage" className="btn btn-outline-primary text-white mt-3">Signer les
                    ententes
                    de
                    stage</Link>
                <Link to="/reportedProblems" className="btn btn-outline-primary text-white mt-3">Problèmes
                    signalés</Link>
            </Row>
        </div>
    );
}

export default GestionnaireDashboard;