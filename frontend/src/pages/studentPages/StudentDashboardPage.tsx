import React, {useEffect, useState} from "react";
import {Button, Row} from "react-bootstrap";
import {Link} from "react-router-dom";
import IUser from "../../models/IUser";
import {putStudentNotification} from "../../services/studentServices/StudentFetchService";
import {generateAlert} from "../../services/universalServices/UniversalUtilService";
import ReportButton from "../../components/universalComponents/ReportButton";

const StudentDashboard = ({user, deconnexion}: { user: IUser, deconnexion: Function }): JSX.Element => {
    const [changementPourUploaderMonCV, setChangementPourUploaderMonCV] = useState<number>(0)
    const [changementPourListeDeStages, setChangementPourListeDeStages] = useState<number>(0)
    const [changementPourMesContrats, setChangementPourMesContrats] = useState<number>(0)

    useEffect(() => {
        const fetchStudentNotification = async () => {
            await putStudentNotification(user.id, user.token).then(async reponse => {
                if (reponse.status === 200) {
                    const data = await reponse.json()
                    setChangementPourUploaderMonCV(data.nbUploadCv)
                    setChangementPourListeDeStages(data.nbStages)
                    setChangementPourMesContrats(data.nbContracts)
                } else {
                    generateAlert()
                }
            })
        }
        fetchStudentNotification()
    }, [user])

    return (
        <div className="min-vh-100">
            <Button className="btn btn-danger my-2" onClick={() => deconnexion()}>
                Déconnexion
            </Button>
            <h1 data-testid="titleStudentDashboard"
                className="pt-5 text-center fw-bold text-white display-4">Bienvenue {user.firstName} {user.lastName}</h1>
            <ReportButton/>
            <Row className="d-flex justify-content-center">
                {
                    changementPourUploaderMonCV === 0
                        ?
                        <Link to="/uploaderCV" className="btn btn-outline-primary text-white mb-3">Mon curriculum vitae</Link>
                        :
                        <Link to="/uploaderCV" className="btn btn-outline-danger text-white mb-3">Mon curriculum vitae
                            ({changementPourUploaderMonCV})</Link>
                }
                {
                    changementPourListeDeStages === 0
                        ?
                        <Link to="/offres" className="btn btn-outline-primary text-white mb-3">Offres de stage</Link>
                        :
                        <Link to="/offres" className="btn btn-outline-danger text-white mb-3">Offres de stage
                            ({changementPourListeDeStages})</Link>

                }
                {
                    changementPourMesContrats === 0
                        ?
                        <Link to="/myContracts" className="btn btn-outline-primary text-white mb-3">Mes contrats</Link>
                        :
                        <Link to="/myContracts" className="btn btn-outline-danger text-white mb-3">Mes contrats
                            ({changementPourMesContrats})</Link>
                }
            </Row>
        </div>
    );
}

export default StudentDashboard;