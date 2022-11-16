import React, {useState} from "react";
import {Button, Row} from "react-bootstrap";
import IUser from "../../models/IUser";
import {Link} from 'react-router-dom';

const CompanyDashboard = ({user, deconnexion}: { user: IUser, deconnexion: Function }): JSX.Element => {
    const [changementPourSoumettreUneOffreDeStage, setChangementPourSoumettreUneOffreDeStage] = useState<number>(0)
    const [changementPourMesOffres, setChangementPourMesOffres] = useState<number>(0)
    const [changementPourMesContrats, setChangementPourMesContrats] = useState<number>(0)
    return (<>
            <Button className="btn btn-danger my-2" onClick={() => deconnexion()}>
                Déconnexion
            </Button>
            <h1 className="p-5 text-center fw-bold text-white display-4">Bienvenue {user.firstName} {user.lastName}</h1>
            <Row className="d-flex justify-content-center">
                {
                    changementPourSoumettreUneOffreDeStage === 0 ?
                        <Link to="/soumettreOffre"
                              className="btn btn-primary">Soumettre une offre de stage</Link> :
                        <Link to="/soumettreOffre"
                              className="btn btn-danger">Soumettre une offre de stage
                            ({changementPourSoumettreUneOffreDeStage})</Link>
                }
                {
                    changementPourMesOffres === 0 ?
                        <Link to="/myOffers"
                              className="btn btn-primary mt-3">Mes offres</Link> :
                        <Link to="/myOffers"
                              className="btn btn-danger mt-3">Mes offres ({changementPourMesOffres})</Link>
                }
                {
                    changementPourMesContrats === 0 ?
                        <Link to="/CompanyContractsPage"
                              className="btn btn-primary mt-3">Mes contrats</Link> :
                        <Link to="/CompanyContractsPage"
                              className="btn btn-primary mt-3">Mes contrats ({changementPourMesContrats})</Link>
                }

            </Row>
        </>
    );
}

export default CompanyDashboard;