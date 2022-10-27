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
        console.log(afficheFormulaire)
        setAfficheFormuaire(true)
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
                <h1 className="text-center p-3">Évluation du milieu de stage</h1>
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
                                        <Col>
                                            <Form.Group>
                                                <Form.Label className="fw-bold  mt-2  h5">Date Stage(Début - Fin)</Form.Label>
                                                <Form.Control minLength={12} type="text" required value={dateStage}
                                                              onChange={e => setDateStage(e.target.value)}></Form.Control>

                                                <Form.Control.Feedback type="invalid">Champ
                                                    requis</Form.Control.Feedback>
                                            </Form.Group>
                                        </Col>
                                    </Row>
                                    <Row>
                                        <Form.Group>
                                            <Form.Label className="fw-bold mt-2 h5">Stage Session</Form.Label>
                                            <Form.Control type="numbre" required value={stageSession}
                                                          onChange={e => setStageSession(e.target.value)}></Form.Control>

                                            <Form.Control.Feedback type="invalid">Stage Session
                                                invalide</Form.Control.Feedback>
                                        </Form.Group>
                                    </Row>

                                    <Row className="mt-3">
                                        <Button type="submit"
                                                className="btn btn-success mx-auto">Enregistrer</Button>
                                    </Row>
                                </Col>
                            </Tab.Pane>
                            <Tab.Pane eventKey="#evaluation">
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