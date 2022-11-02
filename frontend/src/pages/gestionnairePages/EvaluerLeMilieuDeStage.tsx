import React, { useEffect, useState } from "react";
import IUser from "../../models/IUser";
import { Button, Col, Container, Form, ListGroup, Row, Tab, Table } from "react-bootstrap";
import { Link } from "react-router-dom";
import { BeatLoader } from "react-spinners";
import PageHeader from "../../components/universalComponents/PageHeader";
import { generateAlert } from "../../services/universalServices/UniversalUtilService";
import InfoDuContrat from "../../models/InfoDuContrat";
import {
    postEvaluationStage, putContrats,
    putInfoContratPourEvaluateStage
} from "../../services/gestionnaireServices/GestionnaireFetchService";
import IContrat from "../../models/IContrat";

const EvaluerLeMilieuDeStage = ({ user }: { user: IUser }): JSX.Element => {
    const [contrats, setContrats] = useState<IContrat[]>([]);
    const [afficheFormulaire, setAfficheFormuaire] = useState<boolean>(false)
    const [waiting, setWaiting] = useState<boolean>(false);
    const [validated, setValidated] = useState<boolean>(false);
    // varialbles pour formulaire la partie identification de l'entreprise
    const [infosContrat, setInfosContrat] = useState<InfoDuContrat>({
        adresse: "",
        dateStage: "",
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
    const [ville, setVille] = useState("")
    const [telephone, setTelephone] = useState("")
    const [telecopieur, setTelecopieur] = useState("")
    const [codePostal, setCodePostal] = useState("")
    // varialbles pour formulaire la partie identification du stagiaire
    const [tachesAnnoncees, setTachesAnnoncees] = useState("")
    const [integration, setIntegration] = useState("")
    const [tempsReelConsacre, setTempsReelConsacre] = useState("")
    const [heureTotalPremierMois, setHeureTotalPremierMois] = useState(0)//les heures par mois
    const [heureTotalDeuxiemeMois, setHeureTotalDeuxiemeMois] = useState(0)
    const [heureTotalTroisiemeMois, setHeureTotalTroisiemeMois] = useState(0)
    const [environnementDeTravail, setenvironnementDeTravail] = useState("")
    const [climatDeTravail, setclimatDeTravail] = useState("")
    const [milieuDeStage, setMilieuDeStage] = useState("")
    const [salaireOffert, setSalaireOffert] = useState("")
    const [salaireParHeure, setSalaireParHeure] = useState("")
    const [communicationAvecSuperviser, setCommunicationAvecSuperviser] = useState("")
    const [equipementFourni, setEquipementFourni] = useState("")
    const [volumeDeTravail, setVolumeDeTravail] = useState("")
    const [commentaires, setCommentaires] = useState("")

    useEffect(() => {

        const fetchContracts = async (): Promise<void> => {
            try {
                const response: Response = await putContrats(user.token);

                if (response.ok) {
                    const data = await response.json()
                    setContrats(data.contracts)
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
        fetchContractParId(id)
    }
    const notShowFormulaires = () => {
        setAfficheFormuaire(false)
    }
    const onSubmit = async (event: React.SyntheticEvent) => {
        const form: any = event.currentTarget;
        event.preventDefault();
        if (form.checkValidity()) {
            setWaiting(true)

            if (!tachesAnnoncees || !integration || !tempsReelConsacre || !heureTotalPremierMois || !heureTotalDeuxiemeMois
                || !heureTotalTroisiemeMois || !environnementDeTravail || !climatDeTravail || !milieuDeStage
                || !salaireOffert || !salaireParHeure || !communicationAvecSuperviser
                || !equipementFourni || !volumeDeTravail || !commentaires) {
                alert("Veuillez remplir chaque champ de  la partie l'évaluation.")
                setWaiting(false)
                return;
            }

            const res = await postEvaluationStage(user.token, tachesAnnoncees, integration, tempsReelConsacre,
                environnementDeTravail, climatDeTravail, milieuDeStage, heureTotalPremierMois,
                heureTotalDeuxiemeMois, heureTotalTroisiemeMois, communicationAvecSuperviser,
                equipementFourni, volumeDeTravail, commentaires);

            if (!res.ok) {
                alert("La évaluation a été envoyé.");
            } else {
                generateAlert()
            }
            setWaiting(false)
            setValidated(false)
            setAfficheFormuaire(false)
            setInfosContrat({
                adresse: "",
                dateStage: "",
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
            setVille("")
            setTelephone("")
            setTelecopieur("")
            setCodePostal("")
            setTachesAnnoncees("")
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
    }

    if (waiting) {
        return (
            <div className="d-flex justify-content-center py-5 bg-light">
                <BeatLoader className="text-center" color="#292b2c" size={100} />
            </div>
        );
    }

    if (afficheFormulaire) {
        return (
            <Container className="min-vh-100">
                <Row>
                    <Col sm={2}>
                        <Link to="/evaluerLeMilieuDeStage" onClick={() => {
                            notShowFormulaires()
                        }} className="btn btn-primary my-3">Page précédente</Link>
                    </Col>
                    <Col sm={8} className="text-center pt-2">
                        <h1 className="fw-bold text-white display-3 pb-2">Évaluation du milieu de stage</h1>
                    </Col>
                    <Col sm={2}></Col>
                </Row>

                <Tab.Container defaultActiveKey="#etudiant">
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
                                            <Form.Group>
                                                <Form.Label className="fw-bold h5">Nom Entreprise</Form.Label>
                                                <Form.Control type="text" minLength={2}
                                                    value={infosContrat?.nomCompagnie}>
                                                </Form.Control>
                                            </Form.Group>
                                        </Col>
                                        <Col>
                                            <Form.Group>
                                                <Form.Label className="fw-bold h5">Personne Contact</Form.Label>
                                                <Form.Control type="text"
                                                    value={`${infosContrat?.nomContact} ${infosContrat?.prenomContact}`}>
                                                </Form.Control>
                                            </Form.Group>
                                        </Col>
                                    </Row>
                                    <Row>
                                        <Form.Group>
                                            <Form.Label className="fw-bold mt-2 h5">Adresse</Form.Label>
                                            <Form.Control type="text" value={infosContrat?.adresse}></Form.Control>
                                        </Form.Group>
                                    </Row>
                                    <Row>
                                        <Col>
                                            <Form.Group>
                                                <Form.Label className="fw-bold mt-2 h5">Ville</Form.Label>
                                                <Form.Control type="text" value={ville}
                                                    onChange={e => setVille(e.target.value)}></Form.Control>

                                            </Form.Group>
                                        </Col>
                                        <Col>
                                            <Form.Group>
                                                <Form.Label className="fw-bold mt-2 h5">Telephone</Form.Label>
                                                <Form.Control type="text" minLength={10} required value={telephone}
                                                    onChange={e => setTelephone(e.target.value)}></Form.Control>
                                                <Form.Control.Feedback type="invalid">Telephone invalide, la longeur
                                                    minimale 10</Form.Control.Feedback>
                                            </Form.Group>
                                        </Col>
                                        <Col>
                                            <Form.Group>
                                                <Form.Label className="fw-bold mt-2 h5">Telecopieur</Form.Label>
                                                <Form.Control type="text" minLength={10} required value={telecopieur}
                                                    onChange={e => setTelecopieur(e.target.value)}></Form.Control>
                                                <Form.Control.Feedback type="invalid">Telecopieur invalide, la longeur
                                                    minimale 10</Form.Control.Feedback>

                                            </Form.Group>
                                        </Col>
                                        <Col>
                                            <Form.Group>
                                                <Form.Label className="fw-bold mt-2 h5">Code Postal</Form.Label>
                                                <Form.Control type="text" minLength={6} required value={codePostal}
                                                    onChange={e => setCodePostal(e.target.value)}></Form.Control>
                                                <Form.Control.Feedback type="invalid">Code Postal invalide, la longeur
                                                    minimale 6</Form.Control.Feedback>

                                            </Form.Group>
                                        </Col>
                                        <Row className="mt-3">
                                            <Button type="submit"
                                                className="btn btn-success mx-auto">Enregistrer</Button>
                                        </Row>
                                    </Row>
                                </Col>
                            </Tab.Pane>
                            <Tab.Pane eventKey="#stagiaire">
                                <Col className="bg-light px-4 pb-2 pt-1">
                                    <Row>
                                        <Col>
                                            <Form.Group>
                                                <Form.Label className="fw-bold  mt-2 h5">Nom Stagiaire</Form.Label>
                                                <Form.Control type="text"
                                                    value={`${infosContrat?.nomEtudiant} ${infosContrat?.prenomEtudiant}`}>
                                                </Form.Control>
                                            </Form.Group>
                                        </Col>
                                    </Row>
                                    <Row>
                                        <Col className="col-10">
                                            <Form.Group>
                                                <Form.Label className="fw-bold mt-2 h5">Date Stage(Début -
                                                    Fin)</Form.Label>
                                                <Form.Control type="text" value={infosContrat?.dateStage}>
                                                </Form.Control>
                                            </Form.Group>
                                        </Col>
                                        <Col className="col-2">
                                            <Form.Group>
                                                <Form.Label className="fw-bold mt-2 h5">Stage Session
                                                    <Form.Control type="text" value={infosContrat?.session}>
                                                    </Form.Control>
                                                </Form.Label>
                                            </Form.Group>
                                        </Col>
                                    </Row>

                                    <Row className="mt-3">
                                        <Button type="submit"
                                            className="btn btn-success mx-auto">Enregistrer</Button>
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
                                                value={tachesAnnoncees}
                                                onChange={(e) => setTachesAnnoncees(e.target.value)}>
                                                <option hidden value="" disabled>Choix
                                                </option>
                                                <option value="totalemenEnAccord">Totalement en accord
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
                                                <option value="totalemenEnAccord">Totalement en accord
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
                                                <option value="totalemenEnAccord">Totalement en accord
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
                                        <h3 className="fw-bold mt-2 h5">Préciser le nombre d'heures/semaine:</h3>
                                    </Row>
                                    <Row>
                                        <Col>
                                            <Form.Group>
                                                <Form.Label className="fw-bold h5">Premier mois</Form.Label>
                                                <Form.Control type="text" minLength={6} required
                                                    value={heureTotalPremierMois}
                                                    onChange={e => setHeureTotalPremierMois(Number(e.target.value))}></Form.Control>
                                                <Form.Control.Feedback type="invalid">Premier mois
                                                    invalide</Form.Control.Feedback>
                                            </Form.Group>
                                        </Col>
                                        <Col>
                                            <Form.Group>
                                                <Form.Label className="fw-bold h5">Deuxième mois</Form.Label>
                                                <Form.Control type="text" minLength={6} required
                                                    value={heureTotalDeuxiemeMois}
                                                    onChange={e => setHeureTotalDeuxiemeMois(Number(e.target.value))}></Form.Control>
                                                <Form.Control.Feedback type="invalid">Deuxième mois
                                                    invalide</Form.Control.Feedback>
                                            </Form.Group>
                                        </Col>
                                        <Col>
                                            <Form.Group>
                                                <Form.Label className="fw-bold h5">Troisième mois</Form.Label>
                                                <Form.Control type="text" minLength={6} required
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
                                                <option value="totalemenEnAccord">Totalement en accord
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
                                                <option value="totalemenEnAccord">Totalement en accord
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
                                                <option value="totalemenEnAccord">Totalement en accord
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
                                                <option value="totalemenEnAccord">Totalement en accord
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
                                            <Form.Control type="text" required value={salaireParHeure}
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
                                                <option value="totalemenEnAccord">Totalement en accord
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
                                                <option value="totalemenEnAccord">Totalement en accord
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
                                                <option value="totalemenEnAccord">Totalement en accord
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
                                            showFormulaires(Number(contrat.id))
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