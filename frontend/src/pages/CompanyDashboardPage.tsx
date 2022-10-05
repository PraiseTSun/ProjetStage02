import React from "react";
import {Button, Row} from "react-bootstrap";
import IUser from "../models/IUser";
import {Link} from 'react-router-dom';

const CompanyDashboard = ({ user, deconnexion }: { user: IUser, deconnexion: Function }): JSX.Element => {

    return (<>
        <Button className="btn btn-danger my-2" onClick={() => deconnexion()}>
            Déconnexion
        </Button>
        <h1 className="p-5 text-center ">Bienvenue {user.firstName} {user.lastName}</h1>
        <Row className="d-flex justify-content-center">
            <Link to="/soumettreOffre"
             className="btn btn-primary">Soumettre une offre de stage</Link>
        </Row>
    </>
    );
}

export default CompanyDashboard;