import React, {useEffect, useState} from "react";
import {Button, Row} from "react-bootstrap";
import {Link} from "react-router-dom";
import IUser from "../../models/IUser";
import {generateAlert} from "../../services/universalServices/UniversalUtilService";
import {putGestionnaireNotification} from "../../services/gestionnaireServices/GestionnaireFetchService";

const GestionnaireDashboard = ({user, deconnexion}: { user: IUser, deconnexion: Function }): JSX.Element => {
    const [changementPourValidationDesUtilisateurs, setChangementPourValidationDesUtilisateurs] = useState<number>(0)
    const [changementPourValidationDesCurriculumsVitaeDesEtudiants, setChangementPourValidationDesCurriculumsVitaeDesEtudiants] = useState<number>(0)
    const [changementPourValidationNouvelleOffreStage, setChangementPourValidationNouvelleOffreStage] = useState<number>(0)
    const [changementPourEvaluerLeMilieuDeStage, setChangementPourEvaluerLeMilieuDeStage] = useState<number>(0)
    const [changementPourCreerLesEntentesDeStage, setChangementPourCreerLesEntentesDeStage] = useState<number>(0)
    const [changementPourConsulterLesEvaluationsDesStages, setChangementPourConsulterLesEvaluationsDesStages] = useState<number>(0)
    const [changementPourConsulterLesEvaluationsDesEtudiant, setChangementPourConsulterLesEvaluationsDesEtudiant] = useState<number>(0)
    const [changementPourSignerLEntenteDeStage, setChangementPourSignerLEntenteDeStage] = useState<number>(0)

    useEffect(() => {
        const fetchGestionnaireNotification = async () => {
            await putGestionnaireNotification(user.token).then(async reponse => {
                if (reponse.status === 200) {
                    const data = await reponse.json()
                    setChangementPourValidationDesUtilisateurs(data.nbUnvalidatedUser)
                    setChangementPourValidationDesCurriculumsVitaeDesEtudiants(data.nbUnvalidatedCV)
                    setChangementPourValidationNouvelleOffreStage(data.nbUnvalidatedOffer)
                    setChangementPourEvaluerLeMilieuDeStage(data.nbEvaluateMilieuStage)
                    setChangementPourCreerLesEntentesDeStage(data.nbCreateContract)
                    setChangementPourConsulterLesEvaluationsDesStages(data.nbConsultStageEvaluation)
                    setChangementPourConsulterLesEvaluationsDesEtudiant(data.nbConsultStudentEvaluation)
                    setChangementPourSignerLEntenteDeStage(data.nbSigneContract)
                } else {
                    generateAlert()
                }
            })
        }
        fetchGestionnaireNotification()
    }, [user])

    return (
        <div className="min-vh-100">
            <Button className="btn btn-danger my-2" onClick={() => deconnexion()}>
                Déconnexion
            </Button>
            <h1 className="p-5 text-center fw-bold text-white display-4">Bienvenue {user.firstName} {user.lastName}</h1>
            <Row className="d-flex justify-content-center">
                {
                    changementPourValidationDesUtilisateurs === 0 ?
                        <Link to="/userValidation" className="btn btn-primary">Validation des utilisateurs</Link> :
                        <Link to="/userValidation" className="btn btn-danger">Validation des
                            utilisateurs ({changementPourValidationDesUtilisateurs})</Link>
                }
                {
                    changementPourValidationDesCurriculumsVitaeDesEtudiants === 0 ?
                        <Link to="/cvValidation" className="btn btn-primary mt-3">Validation des curriculums vitae des
                            étudiants</Link> :
                        <Link to="/cvValidation" className="btn btn-danger mt-3">Validation des curriculums vitae des
                            étudiants ({changementPourValidationDesCurriculumsVitaeDesEtudiants})</Link>
                }
                {
                    changementPourValidationNouvelleOffreStage === 0 ?
                        <Link to="/validerNouvelleOffre" className="btn btn-primary mt-3">Validation nouvelle offre
                            stage</Link> :
                        <Link to="/validerNouvelleOffre" className="btn btn-danger mt-3">Validation nouvelle offre
                            stage ({changementPourValidationNouvelleOffreStage})</Link>
                }
                {
                    changementPourEvaluerLeMilieuDeStage === 0 ?
                        <Link to="/evaluerLeMilieuDeStage" className="btn btn-primary mt-3">Évaluer le milieu de
                            stage</Link> :
                        <Link to="/evaluerLeMilieuDeStage" className="btn btn-danger mt-3">Évaluer le milieu de stage
                            ({changementPourEvaluerLeMilieuDeStage})</Link>
                }
                {
                    changementPourCreerLesEntentesDeStage === 0 ?
                        <Link to="/acceptationsValidation" className="btn btn-primary mt-3">Créer les ententes de
                            stage</Link> :
                        <Link to="/acceptationsValidation" className="btn btn-danger mt-3">Créer les ententes de stage
                            ({changementPourCreerLesEntentesDeStage})</Link>
                }
                {
                    changementPourConsulterLesEvaluationsDesStages === 0 ?
                        <Link to="/consulterEvaluation" className="btn btn-primary mt-3">Consulter les évaluations des
                            stages</Link> :
                        <Link to="/consulterEvaluation" className="btn btn-danger mt-3">Consulter les évaluations des
                            stages ({changementPourConsulterLesEvaluationsDesStages})</Link>
                }
                {
                    changementPourConsulterLesEvaluationsDesEtudiant === 0 ?
                        <Link to="/consulterEvaluationParEntreprise" className="btn btn-primary mt-3">Consulter les
                            évaluations
                            des étudiants</Link> :
                        <Link to="/consulterEvaluationParEntreprise" className="btn btn-danger mt-3">Consulter les
                            évaluations des étudiants ({changementPourConsulterLesEvaluationsDesEtudiant})</Link>
                }
                {
                    changementPourSignerLEntenteDeStage === 0 ?
                        <Link to="/signerententedestage" className="btn btn-primary mt-3">Signer l'entente de
                            stage</Link> :
                        <Link to="/signerententedestage" className="btn btn-danger mt-3">Signer l'entente de stage
                            ({changementPourSignerLEntenteDeStage})</Link>
                }
            </Row>
        </div>
    );
}

export default GestionnaireDashboard;