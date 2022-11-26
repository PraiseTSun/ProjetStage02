import React, {useEffect, useState} from "react";
import {Button, Row} from "react-bootstrap";
import IUser from "../../models/IUser";
import {Link} from 'react-router-dom';
import {generateAlert} from "../../services/universalServices/UniversalUtilService";
import {putCompanyNotification} from "../../services/companyServices/CompanyFetchService";

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

    return (<>
            <Button className="btn btn-danger my-2" onClick={() => deconnexion()}>
                Déconnexion
            </Button>
            <h1 className="p-5 text-center fw-bold text-white display-4">Bienvenue {user.firstName} {user.lastName}</h1>
            <Row className="d-flex justify-content-center">
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