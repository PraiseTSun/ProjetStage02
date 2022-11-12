import React, {useEffect, useState} from "react";
import IUser from "../../models/IUser";
import {Button, Col, Container, Form, ListGroup, Row, Tab, Table} from "react-bootstrap";
import {Link} from "react-router-dom";
import {BeatLoader} from "react-spinners";
import PageHeader from "../../components/universalComponents/PageHeader";
import {generateAlert} from "../../services/universalServices/UniversalUtilService";
import InfoDuContrat from "../../models/InfoDuContrat";
import {
    postEvaluationStage, putGetContrats,
    putInfoContratPourEvaluateStage
} from "../../services/gestionnaireServices/GestionnaireFetchService";
import IContrat from "../../models/IContrat";
import SignaturePad from "react-signature-canvas";

const EvaluerLeMilieuDeStage = ({user}: { user: IUser }): JSX.Element => {
    const [contrats, setContrats] = useState<IContrat[]>([]);
    const [afficheFormulaire, setAfficheFormuaire] = useState<boolean>(false)
    const [waiting, setWaiting] = useState<boolean>(false);
    const [validated, setValidated] = useState<boolean>(false);
    //varialbles pour formulaire la partie identification de l'entreprise
    const [infosContrat, setInfosContrat] = useState<InfoDuContrat>({
        adresse: "",
        dateStageDebut: "",
        dateStageFin: "",
        departement: "",
        emailCompagnie: "",
        emailEtudiant: "",
        heuresParSemaine: 0,
        nomCompagnie: "",
        nomContact: "",
        nomEtudiant: "",
        poste: "",
        prenomContact: "",
        prenomEtudiant: "",
        salaire: 0,
        session: ""
    });
    // varialbles pour formulaire la partie identification du stagiaire
    const [contratId, setContratId] = useState<number>(0)
    const [tachesAnnonces, setTachesAnnonces] = useState<string>("")
    const [integration, setIntegration] = useState<string>("")
    const [tempsReelConsacre, setTempsReelConsacre] = useState<string>("")
    const [heureTotalPremierMois, setHeureTotalPremierMois] = useState<number>(0)//les heures par mois
    const [heureTotalDeuxiemeMois, setHeureTotalDeuxiemeMois] = useState<number>(0)
    const [heureTotalTroisiemeMois, setHeureTotalTroisiemeMois] = useState<number>(0)
    const [environnementDeTravail, setenvironnementDeTravail] = useState<string>("")
    const [climatDeTravail, setclimatDeTravail] = useState<string>("")
    const [milieuDeStage, setMilieuDeStage] = useState<string>("")
    const [salaireOffert, setSalaireOffert] = useState<string>("")
    const [salaireParHeure, setSalaireParHeure] = useState<string>("")
    const [communicationAvecSuperviser, setCommunicationAvecSuperviser] = useState<string>("")
    const [equipementFourni, setEquipementFourni] = useState<string>("")
    const [volumeDeTravail, setVolumeDeTravail] = useState<string>("")
    const [commentaires, setCommentaires] = useState<string>("")
    const [signature, setSignature] = useState<string>("")
    const [dateNow, setDateNow] = useState<Date>(new Date())
    const [dateSignature, setDateSignature] = useState<string>(`${dateNow.getDate().toString()}-${(dateNow.getMonth()+1).toString()}-${dateNow.getFullYear().toString()}`)
    const [isSigner, setIsSigner] = useState(false)
    let sigPad: SignaturePad | null
    useEffect(() => {
        const fetchContracts = async (): Promise<void> => {
            try {
                const response: Response = await putGetContrats(user.token);

                if (response.ok) {
                    const data = await response.json()
                    setContrats(await data.contracts)
                } else {
                    generateAlert()
                }
            } catch {
                generateAlert()
            }
        }
        fetchContracts()
    }, [user])

    const fetchContractParId = async (contractId: number): Promise<void> => {
        try {
            const response: Response = await putInfoContratPourEvaluateStage(contractId, user.token);
            if (response.ok) {
                const data = await response.json()
                setInfosContrat(data)
            } else {
                generateAlert()
            }
        } catch {
            generateAlert()
        }
    }

    const showFormulaires = (id: number) => {
        setAfficheFormuaire(true)
        setContratId(id)
        fetchContractParId(id)
    }
    const hideFormulaires = () => {
        setAfficheFormuaire(false)
    }
    const onSubmit = async (event: React.SyntheticEvent) => {
        const form: any = event.currentTarget;
        event.preventDefault();
        if (form.checkValidity()) {
            setWaiting(true)

            if (!tachesAnnonces || !integration || !tempsReelConsacre || !heureTotalPremierMois || !heureTotalDeuxiemeMois
                || !heureTotalTroisiemeMois || !environnementDeTravail || !climatDeTravail || !milieuDeStage
                || !salaireOffert || !salaireParHeure || !communicationAvecSuperviser
                || !equipementFourni || !volumeDeTravail || !commentaires || !signature || !dateSignature) {
                alert("Veuillez remplir chaque champ de  la partie l'évaluation.")
                setWaiting(false)
                return;
            }

            const res = await postEvaluationStage(contratId, user.token, tachesAnnonces, integration, tempsReelConsacre,
                environnementDeTravail, climatDeTravail, milieuDeStage, heureTotalPremierMois,
                heureTotalDeuxiemeMois, heureTotalTroisiemeMois, communicationAvecSuperviser,
                equipementFourni, volumeDeTravail, commentaires, signature, dateSignature);

            if (res.ok) {
                alert("L'évaluation a été envoyé.");
            } else {
                generateAlert()
            }
            setWaiting(false)
            setValidated(false)
            setAfficheFormuaire(false)
            setInfosContrat({
                adresse: "",
                dateStageDebut: "",
                dateStageFin: "",
                departement: "",
                emailCompagnie: "",
                emailEtudiant: "",
                heuresParSemaine: 0,
                nomCompagnie: "",
                nomContact: "",
                nomEtudiant: "",
                poste: "",
                prenomContact: "",
                prenomEtudiant: "",
                salaire: 0,
                session: ""
            })

            setTachesAnnonces("")
            setIntegration("")
            setTempsReelConsacre("")
            setHeureTotalPremierMois(0)
            setHeureTotalDeuxiemeMois(0)
            setHeureTotalTroisiemeMois(0)
            setenvironnementDeTravail("")
            setclimatDeTravail("")
            setMilieuDeStage("")
            setSalaireOffert("")
            setSalaireParHeure("")
            setCommunicationAvecSuperviser("")
            setEquipementFourni("")
            setVolumeDeTravail("")
            setCommentaires("")
        }
        setValidated(true)
        window.location.href = "/evaluerLeMilieuDeStage"
    }

    if (waiting) {
        return (
            <div className="d-flex justify-content-center py-5 bg-light">
                <BeatLoader className="text-center" color="#292b2c" size={100}/>
            </div>
        );
    }
    if (isSigner) {
        return (
            <Container className="vh-100">
                <Row className="bg-dark p-2">
                    <Col sm={1}><Button variant="danger" onClick={() => {
                        setIsSigner(false)
                    }}>Fermer</Button></Col>
                    <Col sm={10}></Col>
                    <Col sm={1}><Button variant="success" onClick={() => {
                        if (sigPad!.isEmpty()) {
                            alert("Vous devez signer!")
                        } else {
                            setSignature(sigPad!.toDataURL())
                            setIsSigner(false)
                        }
                    }}>Signer</Button></Col>
                </Row>
                <Row>
                    <Col sm={4} className="mx-auto mt-3">
                        <SignaturePad canvasProps={{width: 500, height: 200, className: 'border border-5 bg-light'}}
                                      ref={(ref) => {
                                          sigPad = ref
                                      }}/>
                        <Button onClick={() => {
                            sigPad!.clear()
                        }}>Recommencer</Button>
                    </Col>
                </Row>
            </Container>
        );
    }
    if (afficheFormulaire) {
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
                                                         value={tachesAnnonces}
                                                         onChange={(e) => setTachesAnnonces(e.target.value)}>
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
                                                         value={integration}
                                                         onChange={(e) => setIntegration(e.target.value)}>
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
                                                         value={tempsReelConsacre}
                                                         onChange={(e) => setTempsReelConsacre(e.target.value)}>
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
                                                <Form.Control type="number" minLength={1} required
                                                              value={heureTotalPremierMois}
                                                              onChange={e => setHeureTotalPremierMois(Number(e.target.value))}></Form.Control>
                                                <Form.Control.Feedback type="invalid">Premier mois
                                                    invalide</Form.Control.Feedback>
                                            </Form.Group>
                                        </Col>
                                        <Col>
                                            <Form.Group>
                                                <Form.Label className="fw-bold h5">Deuxième mois</Form.Label>
                                                <Form.Control type="number" minLength={1} required
                                                              value={heureTotalDeuxiemeMois}
                                                              onChange={e => setHeureTotalDeuxiemeMois(Number(e.target.value))}></Form.Control>
                                                <Form.Control.Feedback type="invalid">Deuxième mois
                                                    invalide</Form.Control.Feedback>
                                            </Form.Group>
                                        </Col>
                                        <Col>
                                            <Form.Group>
                                                <Form.Label className="fw-bold h5">Troisième mois</Form.Label>
                                                <Form.Control type="number" minLength={1} required
                                                              value={heureTotalTroisiemeMois}
                                                              onChange={e => setHeureTotalTroisiemeMois(Number(e.target.value))}></Form.Control>
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
                                            <Form.Select className="mt-2" value={environnementDeTravail}
                                                         onChange={(e) => setenvironnementDeTravail(e.target.value)}>
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
                                            <Form.Select className="mt-2" required value={climatDeTravail}
                                                         onChange={(e) => setclimatDeTravail(e.target.value)}>
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
                                            <Form.Select className="mt-2" required value={milieuDeStage}
                                                         onChange={(e) => setMilieuDeStage(e.target.value)}>
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
                                            <Form.Select className="mt-2" required value={salaireOffert}
                                                         onChange={(e) => setSalaireOffert(e.target.value)}>
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
                                            <Form.Control type="number" required value={salaireParHeure}
                                                          onChange={e => setSalaireParHeure(e.target.value)}></Form.Control>
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
                                                         value={communicationAvecSuperviser}
                                                         onChange={(e) => setCommunicationAvecSuperviser(e.target.value)}>
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
                                            <Form.Select className="mt-2" required value={equipementFourni}
                                                         onChange={(e) => setEquipementFourni(e.target.value)}>
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
                                            <Form.Select className="mt-2" required value={volumeDeTravail}
                                                         onChange={(e) => setVolumeDeTravail(e.target.value)}>
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
                                                              value={commentaires}
                                                              onChange={e => setCommentaires(e.target.value)}></Form.Control>
                                                <Form.Control.Feedback type="invalid">Champ
                                                    requis</Form.Control.Feedback>
                                            </Form.Group>
                                        </Col>
                                    </Row>
                                    <Row className="mb-4">
                                        <Col>
                                            {
                                                signature === ""
                                                    ?
                                                    <p className="fw-bold h5">Signature</p>
                                                    :
                                                    <p className="fw-bold h5">Signature
                                                        <span className="text-success">   (vous avez déjà signé)</span>
                                                    </p>
                                            }
                                            <Button className="btn btn-primary mt-2 w-75" onClick={() => {
                                                setIsSigner(true)
                                            }}>Signer</Button>
                                        </Col>
                                        <Col>
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

    return (
        <Container className="min-vh-100">
            <PageHeader title={"Évaluation des contrats"}></PageHeader>
            <Row>
                <Col>
                    <Table className="text-center" hover>
                        <thead className="bg-primary text-white">
                        <tr>
                            <th>Compagnie</th>
                            <th>Position</th>
                            <th>Étudiant</th>
                            <th>Description</th>
                            <th>Évalation</th>
                        </tr>
                        </thead>
                        <tbody className="bg-light">
                        {contrats.map((contrat, index) => {
                            return (
                                <tr key={index}>
                                    <td>{contrat.companyName}</td>
                                    <td>{contrat.position}</td>
                                    <td>{contrat.studentFullName}</td>
                                    <td>{contrat.description}</td>
                                    <td><Button className="btn btn-warning" onClick={() => {
                                        showFormulaires(Number(contrat.contractId))
                                    }}>Évaluer</Button></td>
                                </tr>
                            );
                        })}
                        </tbody>
                    </Table>
                </Col>
            </Row>
        </Container>
    )
}
export default EvaluerLeMilieuDeStage