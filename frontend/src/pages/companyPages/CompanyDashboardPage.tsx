import React, {useEffect, useState} from "react";
import {Button, Container, Row} from "react-bootstrap";
import IUser from "../../models/IUser";
import {Link} from 'react-router-dom';
import {generateAlert} from "../../services/universalServices/UniversalUtilService";
import {putCompanyNotification} from "../../services/companyServices/CompanyFetchService";
import ReportButton from "../../components/universalComponents/ReportButton";

const CompanyDashboard = ({user, deconnexion}: { user: IUser, deconnexion: Function }): JSX.Element => {
    const [changementPourMesOffres, setChangementPourMesOffres] = useState<number>(0)
    const [changementPourMesContrats, setChangementPourMesContrats] = useState<number>(0)

    useEffect(() => {
        const fetchCompanyNotification = async () => {
            await putCompanyNotification(user.id, user.token).then(async reponse => {
                if (reponse.status === 200) {
                    const data = await reponse.json()
                    setChangementPourMesOffres(data.nbOffers)
                    setChangementPourMesContrats(data.nbContracts)
                } else {
                    generateAlert()
                }
            })
        }
        fetchCompanyNotification()
    }, [user])

    return (
        <Container className="min-vh-100">
            <Button className="btn btn-danger my-2" onClick={() => deconnexion()}>
                Déconnexion
            </Button>
            <h1 className="pt-5 text-center fw-bold text-white display-4">Bienvenue {user.firstName} {user.lastName}</h1>
            <ReportButton/>
            <Row className="d-flex justify-content-center">
                <Link to="/soumettreOffre"
                      className="btn btn-outline-primary text-white">Soumettre une offre de stage</Link>
                {
                    changementPourMesOffres === 0 ?
                        <Link to="/myOffers"
                              className="btn btn-outline-primary text-white mt-3">Mes offres</Link> :
                        <Link to="/myOffers"
                              className="btn btn-outline-danger text-white mt-3">Mes offres ({changementPourMesOffres})</Link>
                }
                {
                    changementPourMesContrats === 0 ?
                        <Link to="/CompanyContractsPage"
                              className="btn btn-outline-primary text-white mt-3">Mes contrats</Link> :
                        <Link to="/CompanyContractsPage"
                              className="btn btn-outline-danger text-white mt-3">Mes contrats ({changementPourMesContrats})</Link>
                }
            </Row>
        </Container>
    );
}

export default CompanyDashboard;