import React from "react";
import { Button, Row } from "react-bootstrap";
import IUser from "../models/IUser";
const StudentDashboard = ({ user, deconnexion }: { user: IUser, deconnexion:Function }): JSX.Element => {

    return (
        <>
            <Button className="btn btn-danger my-3" onClick={() => deconnexion()}>
                Déconnexion
            </Button>
            <h1 className="p-5 text-center ">Bienvenue {user.firstName} {user.lastName}</h1>
            <Row>
                <h1>testS</h1>
            </Row>
        </>
    );
}

export default StudentDashboard;