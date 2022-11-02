import React from "react";
import {Button, Row} from "react-bootstrap";
import IUser from "../../models/IUser";
import {Link} from 'react-router-dom';

const CompanyDashboard = ({user, deconnexion}: { user: IUser, deconnexion: Function }): JSX.Element => {

    return (<>
            <Button className="btn btn-danger my-2" onClick={() => deconnexion()}>
                Déconnexion
            </Button>
            <h1 className="p-5 text-center fw-bold text-white display-4">Bienvenue {user.firstName} {user.lastName}</h1>
            <Row className="d-flex justify-content-center">
                <Link to="/soumettreOffre"
                      className="btn btn-primary">Soumettre une offre de stage</Link>
                <Link to="/myOffers"
                      className="btn btn-primary mt-3">Mes offres</Link>
                <Link to="/SignerEntenteDeStageParCompagnie"
                      className="btn btn-primary mt-3">Signer l'entente de stage</Link>
            </Row>
        </>
    );
}

export default CompanyDashboard;