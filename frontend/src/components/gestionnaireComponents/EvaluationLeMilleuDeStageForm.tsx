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

const EvaluationLeMilleuDeStageForm = ({hideFormulaires,
                                           user,
                                           setAfficheFormuaire,
                                           infosContrat, contratId}
                                           :
                                           {hideFormulaires: Function,
                                               user: IUser,
                                               setAfficheFormuaire: Function,
                                               infosContrat: InfoDuContrat,
                                           contratId : number}): JSX.Element => {
    const [waiting, setWaiting] = useState<boolean>(false);
    const [validated, setValidated] = useState<boolean>(false);
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
        if(evaluerForm.signature === ""){
            alert("Vous devez cliquer signer!")
        }
        const form: any = event.currentTarget;
        event.preventDefault();

        if (form.checkValidity() && evaluerForm.signature !== "") {
            setWaiting(true)

            const res = await postEvaluationStage(contratId, user.token, evaluerForm.tachesAnnonces,
                evaluerForm.integration, evaluerForm.tempsReelConsacre,
                evaluerForm.environnementDeTravail, evaluerForm.climatDeTravail, evaluerForm.milieuDeStage,
                evaluerForm.heureTotalPremierMois, evaluerForm.heureTotalDeuxiemeMois,
                evaluerForm.heureTotalTroisiemeMois, evaluerForm.communicationAvecSuperviser,
                evaluerForm.equipementFourni, evaluerForm.volumeDeTravail, evaluerForm.commentaires,
                evaluerForm.signature, dateSignature);

            if (res.ok) {
                alert("L'évaluation a été envoyé.");
                window.location.href = "/evaluerLeMilieuDeStage"
            }
            setWaiting(false)
            setValidated(false)
            setAfficheFormuaire(false)
            setEvaluerForm({
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
        }
        setValidated(true)

    }

    if (waiting) {
        return (
            <div className="d-flex justify-content-center py-5 bg-light">
                <BeatLoader className="text-center" color="#292b2c" size={100}/>
            </div>
        );
    }

    return (
        <Container className="min-vh-100">
            <Row>
                <Col sm={2}>
                    <Link to="/evaluerLeMilieuDeStage" onClick={() => {
                        hideFormulaires()
                    }} className="btn btn-primary my-3">Page précédente</Link>
                </Col>
                <Col sm={8} className="text-center pt-2">
                    <h1 className="fw-bold text-white display-3 pb-2">Évaluation du milieu de stage</h1>
                </Col>
                <Col sm={2}></Col>
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
                <Form onSubmit={onSubmit} validated={validated} noValidate>
                    <Tab.Content className="mt-3 p-2">
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
                                        <p className="fw-bold mt-2 h5">Date Stage(Début à
                                            Fin)</p>
                                        <p>{infosContrat?.dateStageDebut} à {infosContrat.dateStageFin}</p>
                                    </Col>
                                    <Col className="col-3">
                                        <p className="fw-bold mt-2 h5">Stage Session</p>
                                        <p>{infosContrat?.session}</p>
                                    </Col>
                                </Row>
                            </Col>
                        </Tab.Pane>
                        <Tab.Pane eventKey="#evaluation">
                            <Col className="bg-light px-4 pb-2 pt-1">
                                <Row>
                                    <Form.Group>
                                        <Form.Label className="fw-bold mt-2 h5">
                                            Les tâches confiées au stagiaire sont conformes aux tâches annoncées
                                            dans l'entente de stage.
                                        </Form.Label>
                                        <Form.Select className="mt-2" required
                                                     value={evaluerForm.tachesAnnonces}
                                                     onChange={(e) => setEvaluerForm({...evaluerForm, tachesAnnonces: e.target.value})}>
                                            <option hidden value="" disabled>Choix
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
                                        <Form.Control.Feedback type="invalid">Stage Session
                                            invalide</Form.Control.Feedback>
                                    </Form.Group>
                                </Row>
                                <Row>
                                    <Form.Group>
                                        <Form.Label className="fw-bold mt-2 h5">
                                            Des mesures d'accueil facilitent l'intégration du nouveau stagiaire.
                                        </Form.Label>
                                        <Form.Select className="mt-2" required
                                                     value={evaluerForm.integration}
                                                     onChange={(e) => setEvaluerForm({...evaluerForm, integration: e.target.value})}>
                                            <option hidden value="" disabled>Choix
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
                                        <Form.Control.Feedback type="invalid">Stage Session
                                            invalide</Form.Control.Feedback>
                                    </Form.Group>
                                </Row>
                                <Row>
                                    <Form.Group>
                                        <Form.Label className="fw-bold mt-2 h5">
                                            Le temps réel consacré à l'encadrement du stagiaire est suffisant.
                                        </Form.Label>
                                        <Form.Select className="mt-2" required
                                                     value={evaluerForm.tempsReelConsacre}
                                                     onChange={(e) => setEvaluerForm({...evaluerForm, tempsReelConsacre: e.target.value})}>
                                            <option hidden value="" disabled>Choix
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
                                        <Form.Control.Feedback type="invalid">Stage Session
                                            invalide</Form.Control.Feedback>
                                    </Form.Group>
                                </Row>
                                <Row>
                                    <h3 className="fw-bold mt-2 h5">Préciser le nombre d'heures total:</h3>
                                </Row>
                                <Row>
                                    <Col>
                                        <Form.Group>
                                            <Form.Label className="fw-bold h5">Premier mois</Form.Label>
                                            <Form.Control type="number" minLength={1} required min={1}
                                                          value={evaluerForm.heureTotalPremierMois}
                                                          onChange={e => setEvaluerForm({...evaluerForm, heureTotalPremierMois:Number(e.target.value)})}></Form.Control>
                                            <Form.Control.Feedback type="invalid">Premier mois
                                                invalide</Form.Control.Feedback>
                                        </Form.Group>
                                    </Col>
                                    <Col>
                                        <Form.Group>
                                            <Form.Label className="fw-bold h5">Deuxième mois</Form.Label>
                                            <Form.Control type="number" minLength={1} required min={1}
                                                          value={evaluerForm.heureTotalDeuxiemeMois}
                                                          onChange={e => setEvaluerForm({...evaluerForm, heureTotalDeuxiemeMois: Number(e.target.value)})}></Form.Control>
                                            <Form.Control.Feedback type="invalid">Deuxième mois
                                                invalide</Form.Control.Feedback>
                                        </Form.Group>
                                    </Col>
                                    <Col>
                                        <Form.Group>
                                            <Form.Label className="fw-bold h5">Troisième mois</Form.Label>
                                            <Form.Control type="number" minLength={1} required min={1}
                                                          value={evaluerForm.heureTotalTroisiemeMois}
                                                          onChange={e => setEvaluerForm({...evaluerForm, heureTotalTroisiemeMois: Number(e.target.value)})}></Form.Control>
                                            <Form.Control.Feedback type="invalid">Troisième mois
                                                invalide</Form.Control.Feedback>
                                        </Form.Group>
                                    </Col>
                                </Row>
                                <Row>
                                    <Form.Group>
                                        <Form.Label className="fw-bold mt-2 h5">
                                            L'environnement de travail respecte les normes d'hygiène et
                                            de sécurité au travail.
                                        </Form.Label>
                                        <Form.Select className="mt-2" required value={evaluerForm.environnementDeTravail}
                                                     onChange={(e) => setEvaluerForm({...evaluerForm, environnementDeTravail: e.target.value})}>
                                            <option hidden value="" disabled>Choix
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
                                        <Form.Control.Feedback type="invalid">Environnement de travail
                                            invalide</Form.Control.Feedback>
                                    </Form.Group>
                                </Row>
                                <Row>
                                    <Form.Group>
                                        <Form.Label className="fw-bold mt-2 h5">
                                            Le climat de travail est agréable.
                                        </Form.Label>
                                        <Form.Select className="mt-2" required value={evaluerForm.climatDeTravail}
                                                     onChange={(e) => setEvaluerForm({...evaluerForm, climatDeTravail: e.target.value})}>
                                            <option hidden value="" disabled>Choix
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
                                        <Form.Control.Feedback type="invalid">Le climat de travail
                                            invalide</Form.Control.Feedback>
                                    </Form.Group>
                                </Row>
                                <Row>
                                    <Form.Group>
                                        <Form.Label className="fw-bold mt-2 h5">
                                            Le milieu de stage est accessble par transport en commun.
                                        </Form.Label>
                                        <Form.Select className="mt-2" required value={evaluerForm.milieuDeStage}
                                                     onChange={(e) => setEvaluerForm({...evaluerForm, milieuDeStage: e.target.value})}>
                                            <option hidden value="" disabled>Choix
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
                                        <Form.Control.Feedback type="invalid">Le milieu de stage
                                            invalide</Form.Control.Feedback>
                                    </Form.Group>
                                </Row>
                                <Row>
                                    <Form.Group>
                                        <Form.Label className="fw-bold mt-2 h5">
                                            Le salaire offert est intéressant pour le stagiaire.
                                        </Form.Label>
                                        <Form.Select className="mt-2" required value={evaluerForm.salaireOffert}
                                                     onChange={(e) => setEvaluerForm({...evaluerForm, salaireOffert: e.target.value})}>
                                            <option hidden value="" disabled>Choix
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
                                        <Form.Control.Feedback type="invalid">Le salaire offert
                                            invalide</Form.Control.Feedback>
                                    </Form.Group>
                                </Row>
                                <Row className="mt-4">

                                    <Col className="fw-bold h5 text-nowrap col-2">Préciser :
                                    </Col>
                                    <Col className="col-8">
                                        <Form.Control type="number" min={1} required value={evaluerForm.salaireParHeure}
                                                      onChange={e => setEvaluerForm({...evaluerForm, salaireParHeure: e.target.value})}></Form.Control>
                                    </Col>
                                    <Col className="fw-bold h5 col-2">/l'heure. </Col>
                                </Row>
                                <Row>
                                    <Form.Group>
                                        <Form.Label className="fw-bold mt-2 h5">
                                            La communication avec le superviseur de stage facilite
                                            le déroulement du stage.
                                        </Form.Label>
                                        <Form.Select className="mt-2" required
                                                     value={evaluerForm.communicationAvecSuperviser}
                                                     onChange={(e) => setEvaluerForm({...evaluerForm, communicationAvecSuperviser: e.target.value})}>
                                            <option hidden value="" disabled>Choix
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
                                        <Form.Control.Feedback type="invalid">Le communication
                                            invalide</Form.Control.Feedback>
                                    </Form.Group>
                                </Row>
                                <Row>
                                    <Form.Group>
                                        <Form.Label className="fw-bold mt-2 h5">
                                            L'équipement fourni est adéquat pour réaliser les tâches confiées.
                                        </Form.Label>
                                        <Form.Select className="mt-2" required value={evaluerForm.equipementFourni}
                                                     onChange={(e) => setEvaluerForm({...evaluerForm, equipementFourni: e.target.value})}>
                                            <option hidden value="" disabled>Choix
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
                                        <Form.Control.Feedback type="invalid">L'équipement fourni
                                            invalide</Form.Control.Feedback>
                                    </Form.Group>
                                </Row>
                                <Row>
                                    <Form.Group>
                                        <Form.Label className="fw-bold mt-2 h5">
                                            Le volume de travail est acceptable.
                                        </Form.Label>
                                        <Form.Select className="mt-2" required value={evaluerForm.volumeDeTravail}
                                                     onChange={(e) => setEvaluerForm({...evaluerForm, volumeDeTravail: e.target.value})}>
                                            <option hidden value="" disabled>Choix
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
                                        <Form.Control.Feedback type="invalid">Le volume de travail
                                            invalide</Form.Control.Feedback>
                                    </Form.Group>
                                </Row>
                                <Row className="mb-4">
                                    <Col>
                                        <Form.Group>
                                            <Form.Label className="fw-bold mt-2 h5">Commentaires</Form.Label>
                                            <Form.Control as="textarea" type="text" minLength={2} required
                                                          value={evaluerForm.commentaires}
                                                          onChange={e => setEvaluerForm({...evaluerForm, commentaires: e.target.value})}></Form.Control>
                                            <Form.Control.Feedback type="invalid">Champ
                                                requis</Form.Control.Feedback>
                                        </Form.Group>
                                    </Col>
                                </Row>
                                <Row className="mb-4">
                                    <Col className="col-6">
                                        {
                                            evaluerForm.signature === ""
                                                ?
                                                <p className="fw-bold h5">Signature</p>
                                                :
                                                <p className="fw-bold h5">Signature
                                                    <span className="text-success"> (vous avez déjà signé) </span>
                                                </p>
                                        }
                                        <SignaturePad canvasProps={{
                                            width: 300,
                                            height: 150,
                                            className: 'border border-5 bg-light'
                                        }}
                                                      ref={(ref) => {
                                                          sigPad = ref
                                                      }}/>
                                        <Row>
                                            <Col className="col-6 ">
                                                <Button onClick={() => {
                                                    sigPad!.clear()
                                                    setEvaluerForm({...evaluerForm, signature:""})
                                                }}>Recommencer</Button>
                                            </Col>
                                            <Col className="col-6"><Button variant="success" onClick={() => {
                                                if (sigPad!.isEmpty()) {
                                                    alert("Vous devez signer!")
                                                } else {
                                                    setEvaluerForm({...evaluerForm, signature: sigPad!.toDataURL()})
                                                }
                                            }}>Signer</Button></Col>
                                        </Row>

                                    </Col>
                                    <Col  className="col-5 m-4" >
                                        <Form.Group>
                                            <Form.Label className="fw-bold mt-2 h5">Date
                                                Signature(JJ-MM-AAAA)</Form.Label>
                                            <h5 className="p-2">{dateSignature}</h5>
                                        </Form.Group>
                                    </Col>
                                </Row>
                                <Row className="mt-3">
                                    <Button type="submit"
                                            className="btn btn-success mx-auto">Enregistrer</Button>
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