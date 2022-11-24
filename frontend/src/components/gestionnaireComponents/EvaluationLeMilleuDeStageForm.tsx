import React, {useState} from "react";
import IEvaluationLeMilieuDeStageForm from "../../models/IEvaluationLeMilieuDeStageForm";
import {Button, Col, Container, Form, ListGroup, Row, Tab} from "react-bootstrap";
import {Link} from "react-router-dom";
import SignaturePad from "react-signature-canvas";
import {
    postEvaluationStage
} from "../../services/gestionnaireServices/GestionnaireFetchService";
import IUser from "../../models/IUser";
import {BeatLoader} from "react-spinners";
import InfoDuContrat from "../../models/InfoDuContrat";
import LocationChangeConfirmationPopup from "../universalComponents/LocationChangeConfirmationPopup";
import {generateAlert} from "../../services/universalServices/UniversalUtilService";
import FormFeedBackInvalid from "../universalComponents/FormFeedBackInvalid";

const EvaluationLeMilleuDeStageForm = ({
                                           hideFormulaires,
                                           user,
                                           infosContrat, contratId
                                       }
                                           :
                                           {
                                               hideFormulaires: Function,
                                               user: IUser,
                                               infosContrat: InfoDuContrat,
                                               contratId: number
                                           }): JSX.Element => {
    const [waiting, setWaiting] = useState<boolean>(false);
    const [validated, setValidated] = useState<boolean>(false);
    const [hasSigned, setHasSigned] = useState<boolean>(true);
    const [formSent, setFormSent] = useState<boolean>(false);
    const [evaluerForm, setEvaluerForm] = useState<IEvaluationLeMilieuDeStageForm>({
        climatDeTravail: "",
        commentaires: "",
        communicationAvecSuperviser: "",
        environnementDeTravail: "",
        equipementFourni: "",
        heureTotalDeuxiemeMois: 0,
        heureTotalPremierMois: 0,
        heureTotalTroisiemeMois: 0,
        integration: "",
        milieuDeStage: "",
        salaireOffert: "",
        salaireParHeure: "",
        signature: "",
        tachesAnnonces: "",
        tempsReelConsacre: "",
        volumeDeTravail: ""
    })

    const dateSignature = new Date().toISOString().split("T")[0]
    let sigPad: SignaturePad | null


    const onSubmit = async (event: React.SyntheticEvent) => {
        const form: any = event.currentTarget;
        event.preventDefault();

        if (form.checkValidity() === false) {
            setValidated(true);
            return
        }
        setValidated(false);

        if (sigPad?.isEmpty()) {
            setHasSigned(false)
            return
        }
        setHasSigned(true)

        setWaiting(true)
        try {
            const res = await postEvaluationStage(contratId, user.token, evaluerForm.tachesAnnonces,
                evaluerForm.integration, evaluerForm.tempsReelConsacre,
                evaluerForm.environnementDeTravail, evaluerForm.climatDeTravail, evaluerForm.milieuDeStage,
                evaluerForm.heureTotalPremierMois, evaluerForm.heureTotalDeuxiemeMois,
                evaluerForm.heureTotalTroisiemeMois, evaluerForm.communicationAvecSuperviser,
                evaluerForm.equipementFourni, evaluerForm.volumeDeTravail, evaluerForm.commentaires,
                sigPad!.toDataURL(), dateSignature);

            if (res.ok) {
                setFormSent(true)
            } else {
                generateAlert()
            }
        } catch (e) {
            generateAlert()
        }
    }

    if (formSent) {
        return (
            <LocationChangeConfirmationPopup message={"Formulaire envoyé"}
                                             locationOnConfirm={"/evaluerLeMilieuDeStage"}/>
        );
    }

    return (
        <Container className="min-vh-100 pb-5">
            <Row>
                <Col sm={2}>
                    <Link to="/evaluerLeMilieuDeStage" onClick={() => {
                        hideFormulaires()
                    }} className="btn btn-primary my-3">Page précédente</Link>
                </Col>
            </Row>

            <Tab.Container defaultActiveKey="#evaluation">
                <ListGroup horizontal className="">
                    <ListGroup.Item action href="#entreprise" variant="primary">
                        IDENTIFICATION DE L'ENTREPRISE
                    </ListGroup.Item>
                    <ListGroup.Item data-testid="entrepriseInscriptionForm" action href="#stagiaire"
                                    variant="primary">
                        IDENTIFICATION DU STAGIAIRE
                    </ListGroup.Item>
                    <ListGroup.Item data-testid="entrepriseInscriptionForm" action href="#evaluation"
                                    variant="primary">
                        ÉVALUATION
                    </ListGroup.Item>
                </ListGroup>
                <Form className="card mt-4" onSubmit={onSubmit} validated={validated} noValidate>
                    <h1 className="text-center card-header">Évaluation du milieu de stage</h1>
                    <Tab.Content className="p-0 card-body">
                        <Tab.Pane eventKey="#entreprise">
                            <Col className="bg-light px-4 pb-2 pt-1">
                                <Row>
                                    <Col>
                                        <p className="fw-bold h5">Nom Entreprise</p>
                                        <p>{infosContrat?.nomCompagnie}</p>
                                    </Col>
                                    <Col>
                                        <p className="fw-bold h5">Personne Contact</p>
                                        <p>{`${infosContrat?.nomContact} ${infosContrat?.prenomContact}`}</p>
                                    </Col>
                                    <Col>
                                        <p className="fw-bold h5">Adresse</p>
                                        <p>{infosContrat?.adresse}</p>
                                    </Col>
                                </Row>
                            </Col>
                        </Tab.Pane>
                        <Tab.Pane eventKey="#stagiaire">
                            <Col className="bg-light px-4 pb-2 pt-1">
                                <Row>
                                    <Col className="col-3">
                                        <p className="fw-bold  mt-2 h5">Nom Stagiaire</p>
                                        <p>{`${infosContrat?.nomEtudiant} ${infosContrat?.prenomEtudiant}`}</p>
                                    </Col>
                                    <Col className="col-5">
                                        <p>Date Stage(Début à
                                            Fin)</p>
                                        <p>{infosContrat?.dateStageDebut} à {infosContrat.dateStageFin}</p>
                                    </Col>
                                    <Col className="col-3">
                                        <p>Stage Session</p>
                                        <p>{infosContrat?.session}</p>
                                    </Col>
                                </Row>
                            </Col>
                        </Tab.Pane>
                        <Tab.Pane eventKey="#evaluation">
                            <Col className="bg-light px-4 pb-2 pt-1">
                                <Row>
                                    <Form.Group className="my-2">
                                        <Form.Label>
                                            Les tâches confiées au stagiaire sont conformes aux tâches annoncées
                                            dans l'entente de stage.
                                        </Form.Label>
                                        <Form.Select className="mt-2" required
                                                     value={evaluerForm.tachesAnnonces}
                                                     onChange={(e) => setEvaluerForm({
                                                         ...evaluerForm,
                                                         tachesAnnonces: e.target.value
                                                     })}>
                                            <option hidden value="" disabled>Veuillez choisir une option
                                            </option>
                                            <option value="totalementEnAccord">Totalement en accord
                                            </option>
                                            <option value="plutotEnAccord">Plutôt en accord
                                            </option>
                                            <option value="plutotEnDesaccord">Plutôt en désaccord
                                            </option>
                                            <option value="totalementEnDesaccord">Totalement en désaccord
                                            </option>
                                            <option value="impossibleDeSePrononcer">Impossible de se prononcer
                                            </option>
                                        </Form.Select>
                                        <FormFeedBackInvalid/>
                                    </Form.Group>
                                </Row>
                                <Row>
                                    <Form.Group className="my-2">
                                        <Form.Label>
                                            Des mesures d'accueil facilitent l'intégration du nouveau stagiaire.
                                        </Form.Label>
                                        <Form.Select className="mt-2" required
                                                     value={evaluerForm.integration}
                                                     onChange={(e) => setEvaluerForm({
                                                         ...evaluerForm,
                                                         integration: e.target.value
                                                     })}>
                                            <option hidden value="" disabled>Veuillez choisir une option
                                            </option>
                                            <option value="totalementEnAccord">Totalement en accord
                                            </option>
                                            <option value="plutotEnAccord">Plutôt en accord
                                            </option>
                                            <option value="plutotEnDesaccord">Plutôt en désaccord
                                            </option>
                                            <option value="totalementEnDesaccord">Totalement en désaccord
                                            </option>
                                            <option value="impossibleDeSePrononcer">Impossible de se prononcer
                                            </option>
                                        </Form.Select>
                                        <FormFeedBackInvalid/>
                                    </Form.Group>
                                </Row>
                                <Row>
                                    <Form.Group className="my-2">
                                        <Form.Label>
                                            Le temps réel consacré à l'encadrement du stagiaire est suffisant.
                                        </Form.Label>
                                        <Form.Select className="mt-2" required
                                                     value={evaluerForm.tempsReelConsacre}
                                                     onChange={(e) => setEvaluerForm({
                                                         ...evaluerForm,
                                                         tempsReelConsacre: e.target.value
                                                     })}>
                                            <option hidden value="" disabled>Veuillez choisir une option
                                            </option>
                                            <option value="totalementEnAccord">Totalement en accord
                                            </option>
                                            <option value="plutotEnAccord">Plutôt en accord
                                            </option>
                                            <option value="plutotEnDesaccord">Plutôt en désaccord
                                            </option>
                                            <option value="totalementEnDesaccord">Totalement en désaccord
                                            </option>
                                            <option value="impossibleDeSePrononcer">Impossible de se prononcer
                                            </option>
                                        </Form.Select>
                                        <FormFeedBackInvalid/>
                                    </Form.Group>
                                </Row>
                                <Row>
                                    <h4 className="mt-2">Préciser le nombre d'heures total:</h4>
                                </Row>
                                <Row>
                                    <Col>
                                        <Form.Group className="my-2">
                                            <Form.Label>Premier mois</Form.Label>
                                            <Form.Control type="number" minLength={1} required min={1}
                                                          value={evaluerForm.heureTotalPremierMois}
                                                          onChange={e => setEvaluerForm({
                                                              ...evaluerForm,
                                                              heureTotalPremierMois: Number(e.target.value)
                                                          })}></Form.Control>
                                            <FormFeedBackInvalid/>
                                        </Form.Group>
                                    </Col>
                                    <Col>
                                        <Form.Group className="my-2">
                                            <Form.Label>Deuxième mois</Form.Label>
                                            <Form.Control type="number" minLength={1} required min={1}
                                                          value={evaluerForm.heureTotalDeuxiemeMois}
                                                          onChange={e => setEvaluerForm({
                                                              ...evaluerForm,
                                                              heureTotalDeuxiemeMois: Number(e.target.value)
                                                          })}></Form.Control>
                                            <FormFeedBackInvalid/>
                                        </Form.Group>
                                    </Col>
                                    <Col>
                                        <Form.Group className="my-2">
                                            <Form.Label>Troisième mois</Form.Label>
                                            <Form.Control type="number" minLength={1} required min={1}
                                                          value={evaluerForm.heureTotalTroisiemeMois}
                                                          onChange={e => setEvaluerForm({
                                                              ...evaluerForm,
                                                              heureTotalTroisiemeMois: Number(e.target.value)
                                                          })}></Form.Control>
                                            <FormFeedBackInvalid/>
                                        </Form.Group>
                                    </Col>
                                </Row>
                                <Row>
                                    <Form.Group className="my-2">
                                        <Form.Label>
                                            L'environnement de travail respecte les normes d'hygiène et
                                            de sécurité au travail.
                                        </Form.Label>
                                        <Form.Select className="mt-2" required
                                                     value={evaluerForm.environnementDeTravail}
                                                     onChange={(e) => setEvaluerForm({
                                                         ...evaluerForm,
                                                         environnementDeTravail: e.target.value
                                                     })}>
                                            <option hidden value="" disabled>Veuillez choisir une option
                                            </option>
                                            <option value="totalementEnAccord">Totalement en accord
                                            </option>
                                            <option value="plutotEnAccord">Plutôt en accord
                                            </option>
                                            <option value="plutotEnDesaccord">Plutôt en désaccord
                                            </option>
                                            <option value="totalementEnDesaccord">Totalement en désaccord
                                            </option>
                                            <option value="impossibleDeSePrononcer">Impossible de se prononcer
                                            </option>
                                        </Form.Select>
                                        <FormFeedBackInvalid/>
                                    </Form.Group>
                                </Row>
                                <Row>
                                    <Form.Group className="my-2">
                                        <Form.Label>
                                            Le climat de travail est agréable.
                                        </Form.Label>
                                        <Form.Select className="mt-2" required value={evaluerForm.climatDeTravail}
                                                     onChange={(e) => setEvaluerForm({
                                                         ...evaluerForm,
                                                         climatDeTravail: e.target.value
                                                     })}>
                                            <option hidden value="" disabled>Veuillez choisir une option
                                            </option>
                                            <option value="totalementEnAccord">Totalement en accord
                                            </option>
                                            <option value="plutotEnAccord">Plutôt en accord
                                            </option>
                                            <option value="plutotEnDesaccord">Plutôt en désaccord
                                            </option>
                                            <option value="totalementEnDesaccord">Totalement en désaccord
                                            </option>
                                            <option value="impossibleDeSePrononcer">Impossible de se prononcer
                                            </option>
                                        </Form.Select>
                                        <FormFeedBackInvalid/>
                                    </Form.Group>
                                </Row>
                                <Row>
                                    <Form.Group className="my-2">
                                        <Form.Label>
                                            Le milieu de stage est accessible par transport en commun.
                                        </Form.Label>
                                        <Form.Select className="mt-2" required value={evaluerForm.milieuDeStage}
                                                     onChange={(e) => setEvaluerForm({
                                                         ...evaluerForm,
                                                         milieuDeStage: e.target.value
                                                     })}>
                                            <option hidden value="" disabled>Veuillez choisir une option
                                            </option>
                                            <option value="totalementEnAccord">Totalement en accord
                                            </option>
                                            <option value="plutotEnAccord">Plutôt en accord
                                            </option>
                                            <option value="plutotEnDesaccord">Plutôt en désaccord
                                            </option>
                                            <option value="totalementEnDesaccord">Totalement en désaccord
                                            </option>
                                            <option value="impossibleDeSePrononcer">Impossible de se prononcer
                                            </option>
                                        </Form.Select>
                                        <FormFeedBackInvalid/>
                                    </Form.Group>
                                </Row>
                                <Row>
                                    <Form.Group className="my-2">
                                        <Form.Label>
                                            Le salaire offert est intéressant pour le stagiaire.
                                        </Form.Label>
                                        <Form.Select className="mt-2" required value={evaluerForm.salaireOffert}
                                                     onChange={(e) => setEvaluerForm({
                                                         ...evaluerForm,
                                                         salaireOffert: e.target.value
                                                     })}>
                                            <option hidden value="" disabled>Veuillez choisir une option
                                            </option>
                                            <option value="totalementEnAccord">Totalement en accord
                                            </option>
                                            <option value="plutotEnAccord">Plutôt en accord
                                            </option>
                                            <option value="plutotEnDesaccord">Plutôt en désaccord
                                            </option>
                                            <option value="totalementEnDesaccord">Totalement en désaccord
                                            </option>
                                            <option value="impossibleDeSePrononcer">Impossible de se prononcer
                                            </option>
                                        </Form.Select>
                                        <FormFeedBackInvalid/>
                                    </Form.Group>
                                </Row>
                                <Row className="mt-4">

                                    <Col sm={1} className="h5 text-nowrap col-2">Préciser :
                                    </Col>
                                    <Col sm={9}>
                                        <Form.Control type="number" min={1} required
                                                      value={evaluerForm.salaireParHeure}
                                                      onChange={e => setEvaluerForm({
                                                          ...evaluerForm,
                                                          salaireParHeure: e.target.value
                                                      })}></Form.Control>
                                        <FormFeedBackInvalid/>
                                    </Col>
                                    <Col sm={2} className="h5">$/l'heure</Col>
                                </Row>
                                <Row>
                                    <Form.Group className="my-2">
                                        <Form.Label>
                                            La communication avec le superviseur de stage facilite
                                            le déroulement du stage.
                                        </Form.Label>
                                        <Form.Select className="mt-2" required
                                                     value={evaluerForm.communicationAvecSuperviser}
                                                     onChange={(e) => setEvaluerForm({
                                                         ...evaluerForm,
                                                         communicationAvecSuperviser: e.target.value
                                                     })}>
                                            <option hidden value="" disabled>Veuillez choisir une option
                                            </option>
                                            <option value="totalementEnAccord">Totalement en accord
                                            </option>
                                            <option value="plutotEnAccord">Plutôt en accord
                                            </option>
                                            <option value="plutotEnDesaccord">Plutôt en désaccord
                                            </option>
                                            <option value="totalementEnDesaccord">Totalement en désaccord
                                            </option>
                                            <option value="impossibleDeSePrononcer">Impossible de se prononcer
                                            </option>
                                        </Form.Select>
                                        <FormFeedBackInvalid/>
                                    </Form.Group>
                                </Row>
                                <Row>
                                    <Form.Group className="my-2">
                                        <Form.Label>
                                            L'équipement fourni est adéquat pour réaliser les tâches confiées.
                                        </Form.Label>
                                        <Form.Select className="mt-2" required value={evaluerForm.equipementFourni}
                                                     onChange={(e) => setEvaluerForm({
                                                         ...evaluerForm,
                                                         equipementFourni: e.target.value
                                                     })}>
                                            <option hidden value="" disabled>Veuillez choisir une option
                                            </option>
                                            <option value="totalementEnAccord">Totalement en accord
                                            </option>
                                            <option value="plutotEnAccord">Plutôt en accord
                                            </option>
                                            <option value="plutotEnDesaccord">Plutôt en désaccord
                                            </option>
                                            <option value="totalementEnDesaccord">Totalement en désaccord
                                            </option>
                                            <option value="impossibleDeSePrononcer">Impossible de se prononcer
                                            </option>
                                        </Form.Select>
                                        <FormFeedBackInvalid/>
                                    </Form.Group>
                                </Row>
                                <Row>
                                    <Form.Group className="my-2">
                                        <Form.Label>
                                            Le volume de travail est acceptable.
                                        </Form.Label>
                                        <Form.Select className="mt-2" required value={evaluerForm.volumeDeTravail}
                                                     onChange={(e) => setEvaluerForm({
                                                         ...evaluerForm,
                                                         volumeDeTravail: e.target.value
                                                     })}>
                                            <option hidden value="" disabled>Veuillez choisir une option
                                            </option>
                                            <option value="totalementEnAccord">Totalement en accord
                                            </option>
                                            <option value="plutotEnAccord">Plutôt en accord
                                            </option>
                                            <option value="plutotEnDesaccord">Plutôt en désaccord
                                            </option>
                                            <option value="totalementEnDesaccord">Totalement en désaccord
                                            </option>
                                            <option value="impossibleDeSePrononcer">Impossible de se prononcer
                                            </option>
                                        </Form.Select>
                                        <FormFeedBackInvalid/>
                                    </Form.Group>
                                </Row>
                                <Row className="mb-4">
                                    <Col>
                                        <Form.Group className="my-2">
                                            <Form.Label>Commentaires</Form.Label>
                                            <Form.Control as="textarea" type="text" minLength={2} required
                                                          value={evaluerForm.commentaires}
                                                          onChange={e => setEvaluerForm({
                                                              ...evaluerForm,
                                                              commentaires: e.target.value
                                                          })}></Form.Control>
                                            <FormFeedBackInvalid/>
                                        </Form.Group>
                                    </Col>
                                </Row>
                                <Row className="text-center pb-3">
                                    <h1>Signature</h1>
                                    <Col className="p-2">
                                        <SignaturePad
                                            canvasProps={{height: 300, className: 'border col-6 border-5 bg-light'}}
                                            ref={(ref) => {
                                                sigPad = ref
                                            }}/>
                                        {!hasSigned && <p className="text-danger fw-bold h5">Vous devez signer!</p>}
                                    </Col>
                                    <Row>
                                        <Button className="w-25 mx-auto" onClick={() => {
                                            sigPad!.clear()
                                        }}>Recommencer</Button>
                                    </Row>
                                </Row>
                                <Row>
                                    <Col className="text-center mx-auto">
                                        {validated &&
                                            <h1 className="text-danger">Vous devez remplir tous les champs!</h1>}
                                        {
                                            waiting
                                                ? <BeatLoader color="#0275d8 " size={25}/>
                                                :
                                                <Button variant="success w-100" className="mt-2 text-center"
                                                        type="submit">Soumettre</Button>
                                        }
                                    </Col>
                                </Row>
                            </Col>
                        </Tab.Pane>
                    </Tab.Content>
                </Form>
            </Tab.Container>
        </Container>
    )
}
export default EvaluationLeMilleuDeStageForm