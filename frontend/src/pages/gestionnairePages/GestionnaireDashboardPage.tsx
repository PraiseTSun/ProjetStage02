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
    const [changementProblems, setChagementProblems] = useState<number>(0)

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
                    setChagementProblems(data.nbProblems)
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
                D??connexion
            </Button>
            <h1 className="p-5 pb-3 text-center fw-bold text-white display-4">Bienvenue {user.firstName} {user.lastName}</h1>
            <Row sm={10}>
                {
                    changementPourValidationDesUtilisateurs === 0 ?
                        <Link to="/userValidation" className="btn text-white btn-outline-primary">Validation des
                            utilisateurs</Link> :
                        <Link to="/userValidation" className="btn text-white btn btn-outline-danger">Validation des
                            utilisateurs ({changementPourValidationDesUtilisateurs})</Link>
                }
                {
                    changementPourValidationDesCurriculumsVitaeDesEtudiants === 0 ?
                        <Link to="/cvValidation" className="btn btn-outline-primary text-white mt-3">Validation des
                            curriculums vitae des ??tudiants</Link> :
                        <Link to="/cvValidation" className="btn btn-outline-danger text-white mt-3">Validation des
                            curriculums vitae des ??tudiants
                            ({changementPourValidationDesCurriculumsVitaeDesEtudiants})</Link>
                }
                {
                    changementPourValidationNouvelleOffreStage === 0 ?
                        <Link to="/validerNouvelleOffre" className="btn btn-outline-primary text-white mt-3">Validation
                            des
                            nouvelles offres de stage</Link> :
                        <Link to="/validerNouvelleOffre" className="btn btn-outline-danger text-white mt-3">Validation
                            des
                            nouvelles offres de stage ({changementPourValidationNouvelleOffreStage})</Link>
                }
                <Link to="/offerHistory" className="btn btn-outline-primary text-white mt-3">Historique des
                    offres</Link>
                {
                    changementPourEvaluerLeMilieuDeStage === 0 ?
                        <Link to="/evaluerLeMilieuDeStage" className="btn btn-outline-primary text-white mt-3">??valuation
                            des milieux de stage</Link> :
                        <Link to="/evaluerLeMilieuDeStage" className="btn btn-outline-danger text-white mt-3">??valuation
                            des milieux de stage ({changementPourEvaluerLeMilieuDeStage})</Link>
                }
                {
                    changementPourCreerLesEntentesDeStage === 0 ?
                        <Link to="/acceptationsValidation" className="btn btn-outline-primary text-white mt-3">Cr??ation
                            d'ententes de stage</Link> :
                        <Link to="/acceptationsValidation" className="btn btn-outline-danger text-white mt-3">Cr??ation
                            d'ententes de stage ({changementPourCreerLesEntentesDeStage})</Link>
                }
                {
                    changementPourConsulterLesEvaluationsDesStages === 0 ?
                        <Link to="/consulterEvaluation" className="btn btn-outline-primary text-white mt-3">Consulter
                            les
                            ??valuations des stages</Link> :
                        <Link to="/consulterEvaluation" className="btn btn-outline-danger text-white mt-3">Consulter les
                            ??valuations des stages ({changementPourConsulterLesEvaluationsDesStages})</Link>
                }
                {
                    changementPourConsulterLesEvaluationsDesEtudiant === 0 ?
                        <Link to="/consulterEvaluationParEntreprise"
                              className="btn btn-outline-primary text-white mt-3">Consulter
                            les ??valuations des ??tudiants</Link> :
                        <Link to="/consulterEvaluationParEntreprise" className="btn btn-outline-danger text-white mt-3">Consulter
                            les ??valuations des ??tudiants ({changementPourConsulterLesEvaluationsDesEtudiant})</Link>
                }
                {
                    changementPourSignerLEntenteDeStage === 0 ?
                        <Link to="/signerententedestage" className="btn btn-outline-primary text-white mt-3">Signer les
                            ententes de stage</Link> :
                        <Link to="/signerententedestage" className="btn btn-outline-danger text-white mt-3">Signer les
                            ententes de stage ({changementPourSignerLEntenteDeStage})</Link>
                }
                {
                    changementProblems === 0 ?
                        <Link to="/reportedProblems" className="btn btn-outline-primary text-white mt-3">Probl??mes
                            signal??s</Link> :
                        <Link to="/reportedProblems" className="btn btn-outline-danger text-white mt-3">Probl??mes
                            signal??s ({changementProblems})</Link>
                }

            </Row>
        </div>
    );
}

export default GestionnaireDashboard;