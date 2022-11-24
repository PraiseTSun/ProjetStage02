import React from "react";
import {Button, Container, Row} from "react-bootstrap";
import IUser from "../../models/IUser";
import {Link} from 'react-router-dom';

const CompanyDashboard = ({user, deconnexion}: { user: IUser, deconnexion: Function }): JSX.Element => {
    return (
        <Container className="min-vh-100">
            <Button className="btn btn-danger my-2" onClick={() => deconnexion()}>
                Déconnexion
            </Button>
            <h1 className="p-5 pb-3 text-center fw-bold text-white display-4">Bienvenue {user.firstName} {user.lastName}</h1>
            <Row className="d-flex justify-content-center">
                <Link to="/soumettreOffre"
                      className="btn btn-outline-primary text-white">Soumettre une offre de stage</Link>
                <Link to="/myOffers"
                      className="btn btn-outline-primary text-white mt-3">Mes offres</Link>
                <Link to="/CompanyContractsPage"
                      className="btn btn-outline-primary text-white mt-3">Mes contrats</Link>
            </Row>
        </Container>
    );
}

export default CompanyDashboard;