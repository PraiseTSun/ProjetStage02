import React, {useState} from "react";
import IUser from "../../models/IUser";
import {Button, Col, Container, Form, ListGroup, Row, Tab, Table} from "react-bootstrap";
import {Link} from "react-router-dom";
import IOffer from "../../models/IOffer";
import {BeatLoader} from "react-spinners";

const EvaluerLeMilieuDeStage = ({user}: { user: IUser }): JSX.Element => {
    const [offers, setOffers] = useState<IOffer[]>([]);
    const [afficheFormulaire, setAfficheFormuaire] = useState(false)
    const [waiting, setWaiting] = useState(false);
    const [validated, setValidated] = useState(false);
    // varialbles pour formulaire la partie identification de l'entreprise
    const [nomEntreprise, setNomEntreprise] = useState("")
    const [personneContact, setPersonneContact] = useState("")
    const [adresse, setAdresse] = useState("")
    const [ville, setVille] = useState("")
    const [telephone, setTelephone] = useState("")
    const [telecopieur, setTelecopieur] = useState("")
    const [codePostal, setCodePostal] = useState("")
    // varialbles pour formulaire la partie identification du stagiaire
    const [nomStagiaire, setNomStagiaire] = useState("")
    const [dateStage, setDateStage] = useState("")
    const [stageSession, setStageSession] = useState("")
    // variables pour formulaire la partie évaluation
    const [tachesAnnoncees, setTachesAnnoncees] = useState("")
    const [integration, setIntegration] = useState("")
    const [tempsReelConsacre, setTempsReelConsacre] = useState("")
    const [premierMois, setPremierMois] = useState(0)//les heures par mois
    const [deuxiemeMois, setDeuxiemeMois] = useState(0)
    const [troisiemeMois, setTroisiemeMois] = useState(0)
    const [environnementDeTravail, setenvironnementDeTravail] = useState("")
    const [climatDeTravail, setclimatDeTravail] = useState("")
    const [milieuDeStage, setMilieuDeStage] = useState("")
    const [salaireOffert, setSalaireOffert] = useState("")
    const [salaireParHeure, setSalaireParHeure] = useState("")
    const [communicationAvecLeSuperviseurDeStage, setCommunicationAvecLeSuperviseurDeStage] = useState("")
    const [equipementFourni, setEquipementFourni] = useState("")
    const [volumeDeTravail, setVolumeDeTravail] = useState("")
    const [commentaire, setCommentaire] = useState("")
    const [signature, setSignature] = useState("")
    const [dateSigner, setDateSigner] = useState("")

    const showFormulaires = () => {
        setAfficheFormuaire(true)
    }
    const notShowFormulaires = () => {
        setAfficheFormuaire(false)
    }
    const onSubmit = async (event: React.SyntheticEvent) => {
        const form: any = event.currentTarget;
        event.preventDefault();
        if (form.checkValidity()) {
            setWaiting(true)
            if (!nomEntreprise || !personneContact || !adresse || !ville || !telephone || !telecopieur
                || !codePostal) {
                alert("Veuillez remplir chaque champ de la partie l'identification de l'entreprise. ")
                setWaiting(false)
                return;
            }

            if (!nomStagiaire || !dateStage || !stageSession) {
                alert("Veuillez remplir chaque champ de la partie l'identification du stagiaire.")
                setWaiting(false)
                return;
            }

            if (!tachesAnnoncees || !integration || !tempsReelConsacre || !premierMois || !deuxiemeMois
                || !troisiemeMois || !environnementDeTravail || !climatDeTravail || !milieuDeStage
                || !salaireOffert || !salaireParHeure || !communicationAvecLeSuperviseurDeStage
                || !equipementFourni || !volumeDeTravail || !commentaire || !signature || !dateSigner) {
                alert("Veuillez remplir chaque champ de  la partie l'évaluation.")
                setWaiting(false)
                return;
            }

            // const res = await putEvaluation()
            //
            // if (!res.ok) {
            //     const data = await res.json();
            //     alert(data.error);
            // } else {
            //     alert("Le courriel de confirmation à été envoyé.");
            // }
            setWaiting(false)
            setValidated(false)
            //set les champs
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
    if (afficheFormulaire) {
        return (
            <Container className="min-vh-100">
                <Row>
                    <Col sm={2}>
                        <Link to="/evaluerLeMilieuDeStage" onClick={()=>{notShowFormulaires()}} className="btn btn-primary my-3">Page précédente</Link>
                    </Col>
                    <Col sm={8} className="text-center pt-2">
                        <h1 className="fw-bold text-white display-3 pb-2">Évluation du milieu de stage</h1>
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
                                                <Form.Control type="text" minLength={2} required value={nomEntreprise}
                                                              onChange={e => setNomEntreprise(e.target.value)}></Form.Control>

                                                <Form.Control.Feedback type="invalid">Champ
                                                    requis</Form.Control.Feedback>
                                            </Form.Group>
                                        </Col>
                                        <Col>
                                            <Form.Group>
                                                <Form.Label className="fw-bold h5">Personne Contact</Form.Label>
                                                <Form.Control minLength={2} type="text" required value={personneContact}
                                                              onChange={e => setPersonneContact(e.target.value)}></Form.Control>

                                                <Form.Control.Feedback type="invalid">Champ
                                                    requis</Form.Control.Feedback>
                                            </Form.Group>
                                        </Col>
                                    </Row>
                                    <Row>
                                        <Form.Group>
                                            <Form.Label className="fw-bold mt-2 h5">Adresse</Form.Label>
                                            <Form.Control type="text" minLength={2} required value={adresse}
                                                          onChange={e => setAdresse(e.target.value)}></Form.Control>

                                            <Form.Control.Feedback type="invalid">Adresse
                                                invalide</Form.Control.Feedback>
                                        </Form.Group>
                                    </Row>
                                    <Row>
                                        <Col>
                                            <Form.Group>
                                                <Form.Label className="fw-bold mt-2 h5">Ville</Form.Label>
                                                <Form.Control type="text" required minLength={2} value={ville}
                                                              onChange={e => setVille(e.target.value)}></Form.Control>

                                                <Form.Control.Feedback type="invalid">Ville
                                                    invalide</Form.Control.Feedback>
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
                                                <Form.Control type="text" minLength={2} required value={nomStagiaire}
                                                              onChange={e => setNomStagiaire(e.target.value)}></Form.Control>
                                                <Form.Control.Feedback type="invalid">Champ
                                                    requis</Form.Control.Feedback>
                                            </Form.Group>
                                        </Col>
                                    </Row>
                                    <Row>
                                        <Col className="col-10">
                                            <Form.Group>
                                                <Form.Label className="fw-bold mt-2 h5">Date Stage(Début - Fin)</Form.Label>
                                                <Form.Control minLength={12} type="text" required value={dateStage}
                                                              onChange={e => setDateStage(e.target.value)}></Form.Control>

                                                <Form.Control.Feedback type="invalid">Champ
                                                    requis</Form.Control.Feedback>
                                            </Form.Group>
                                        </Col>
                                        <Col className="col-2">
                                            <Form.Group>
                                                <Form.Label className="fw-bold mt-2 h5">Stage Session
                                                    <Form.Select className="mt-2"  required
                                                                 value={stageSession} onChange={(e) => setStageSession(e.target.value)}>
                                                        <option hidden value="" disabled>Session
                                                        </option>
                                                        <option value="1">1
                                                        </option>
                                                        <option value="2">2
                                                        </option>
                                                    </Form.Select>
                                                </Form.Label>

                                                <Form.Control.Feedback type="invalid">Stage Session
                                                    invalide</Form.Control.Feedback>
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
                                <Col  className="bg-light px-4 pb-2 pt-1">
                                    <Row>
                                        <Form.Group>
                                            <Form.Label className="fw-bold mt-2 h5">
                                                Les tâches confiées au stagiaire sont conformes aux tâches annoncées
                                                dans l'entente de stage.
                                            </Form.Label>
                                            <Form.Select className="mt-2"  required
                                                         value={tachesAnnoncees} onChange={(e) => setTachesAnnoncees(e.target.value)}>
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
                                            <Form.Select className="mt-2"  required
                                                         value={integration} onChange={(e) => setIntegration(e.target.value)}>
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
                                            <Form.Select className="mt-2"  required
                                                         value={tempsReelConsacre} onChange={(e) => setTempsReelConsacre(e.target.value)}>
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
                                                <Form.Control type="text" minLength={6} required value={premierMois}
                                                              onChange={e => setPremierMois(Number(e.target.value))}></Form.Control>
                                                <Form.Control.Feedback type="invalid">Premier mois invalide</Form.Control.Feedback>
                                            </Form.Group>
                                        </Col>
                                        <Col>
                                            <Form.Group>
                                                <Form.Label className="fw-bold h5">Deuxième mois</Form.Label>
                                                <Form.Control type="text" minLength={6} required value={deuxiemeMois}
                                                              onChange={e => setDeuxiemeMois(Number(e.target.value))}></Form.Control>
                                                <Form.Control.Feedback type="invalid">Deuxième mois invalide</Form.Control.Feedback>
                                            </Form.Group>
                                        </Col>
                                        <Col>
                                            <Form.Group>
                                                <Form.Label className="fw-bold h5">Troisième mois</Form.Label>
                                                <Form.Control type="text" minLength={6} required value={troisiemeMois}
                                                              onChange={e => setTroisiemeMois(Number(e.target.value))}></Form.Control>
                                                <Form.Control.Feedback type="invalid">Troisième mois invalide</Form.Control.Feedback>
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
                                            <Form.Select className="mt-2"  required value={climatDeTravail}
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
                                            <Form.Select className="mt-2"  required  value={milieuDeStage}
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
                                            <Form.Select className="mt-2"  required  value={salaireOffert}
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
                                                <Form.Control type="text" minLength={6} required value={salaireParHeure}
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
                                            <Form.Select className="mt-2"  required  value={communicationAvecLeSuperviseurDeStage}
                                                         onChange={(e) => setCommunicationAvecLeSuperviseurDeStage(e.target.value)}>
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
                                            <Form.Select className="mt-2"  required  value={equipementFourni}
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
                                            <Form.Select className="mt-2"  required  value={volumeDeTravail}
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
                                                <Form.Control as="textarea"  type="text" minLength={2} required value={commentaire}
                                                              onChange={e => setCommentaire(e.target.value)}></Form.Control>
                                                <Form.Control.Feedback type="invalid">Champ
                                                    requis</Form.Control.Feedback>
                                            </Form.Group>
                                        </Col>
                                    </Row>
                                    <Row className="mt-3">
                                        <Col>
                                            <Form.Group>
                                                <Form.Control type="text" minLength={6} required value={signature}
                                                              onChange={e => setSignature(e.target.value)}></Form.Control>
                                                <Form.Label className="fw-bold h5">Signature de l'enseignant responsable du stagiaire</Form.Label>
                                                <Form.Control.Feedback type="invalid">Champ requis</Form.Control.Feedback>
                                            </Form.Group>
                                        </Col>
                                        <Col>
                                            <Form.Group>
                                                <Form.Control type="text" minLength={6} required value={dateSigner}
                                                              onChange={e => setDateSigner(e.target.value)}></Form.Control>
                                                <Form.Label className="fw-bold h5">Date</Form.Label>
                                                <Form.Control.Feedback type="invalid">Champ requis</Form.Control.Feedback>
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
            <Row>
                <Col sm={2}>
                    <Link to="/" className="btn btn-primary my-3">Home</Link>
                </Col>
                <Col sm={8} className="text-center pt-2">
                    <h1 className="fw-bold text-white display-3 pb-2">Évaluation des offres</h1>
                </Col>
                <Col sm={2}></Col>
            </Row>
            <Row>
                <Col>
                    <Table className="text-center" hover>
                        <thead className="bg-primary text-white">
                        <tr>
                            <th>Compagnie</th>
                            <th>Position</th>
                            <th>Heures par semaine</th>
                            <th>Salaire</th>
                            <th>Adresse</th>
                            <th>Évalation</th>
                        </tr>
                        </thead>
                        <tbody className="bg-light">
                        {offers.map((offer, index) => {
                            return (
                                <tr key={index}>
                                    <td>{offer.nomDeCompagnie}</td>
                                    <td>{offer.position}</td>
                                    <td>{offer.heureParSemaine}</td>
                                    <td>{offer.salaire}$</td>
                                    <td>{offer.adresse}</td>
                                    <td><Button className="btn btn-warning" onClick={() => {
                                        showFormulaires()
                                    }}>Évaluer</Button></td>
                                </tr>
                            );
                        })}
                        </tbody>
                    </Table>
                </Col>
            </Row>
            <Button className="btn btn-warning" onClick={() => {
                showFormulaires()
            }}>Évaluer</Button>
        </Container>
    )
}
export default EvaluerLeMilieuDeStage