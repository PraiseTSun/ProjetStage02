import {Button, Col, Form, Row} from "react-bootstrap";
import React, {useState} from "react";
import RatingComponent from "./RatingComponent";
import IStudentEvaluationFormFields from "../../models/IStudentEvaluationFormFields";
import SignaturePad from "react-signature-canvas";
import {BeatLoader} from "react-spinners";

const StudentEvaluationForm = (): JSX.Element => {
    const [waiting, setWaiting] = useState<boolean>(false);
    const [hasSigned, setHasSigned] = useState<boolean>(true);
    const [formFields, setformFields] = useState<IStudentEvaluationFormFields>(new IStudentEvaluationFormFields())
    let sigPad: SignaturePad | null

    const onSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        if (!sigPad?.isEmpty()) {
            setWaiting(true);
            console.log("data sent")
        } else {
            setHasSigned(false)
        }
    }

    return (
        <Form className="bg-white text-dark p-3" onSubmit={event => onSubmit(event)}>
            <h1 className="text-center">FICHE D'ÉVALUATION DU STAGIAIRE</h1>
            <div className="bg-primary p-2 rounded">
                <h2>PRODUCTIVITÉ</h2>
                <p className="fw-bold">Capacité d'optimiser son rendement au travail</p>
                <RatingComponent value={formFields.travailEfficace}
                                 label="Planifier et organiser son travail de façon efficace"
                                 setValue={(value: string) => {
                                     setformFields({...formFields, travailEfficace: value})
                                 }}/>
                <RatingComponent value={formFields.comprendRapidement}
                                 label="Comprendre rapidement les directives relatives à son travail"
                                 setValue={(value: string) => {
                                     setformFields({...formFields, comprendRapidement: value})
                                 }}/>
                <RatingComponent value={formFields.rythmeSoutenu}
                                 label="Maintenir un rythme de travail soutenu"
                                 setValue={(value: string) => {
                                     setformFields({...formFields, rythmeSoutenu: value})
                                 }}/>
                <RatingComponent value={formFields.etablirPriorites} label="Établir ses priorités"
                                 setValue={(value: string) => {
                                     setformFields({...formFields, etablirPriorites: value})
                                 }}/>
                <RatingComponent value={formFields.respecteEcheances} label="Respecter ses échéanciers"
                                 setValue={(value: string) => {
                                     setformFields({...formFields, respecteEcheances: value})
                                 }}/>
                <Form.Group className="mt-2">
                    <Form.Label>Commentaires</Form.Label>
                    <Form.Control value={formFields.commentairesProductivite}
                                  onChange={event =>
                                      setformFields({...formFields, commentairesProductivite: event.target.value})}
                                  as="textarea"
                                  rows={3}/>
                </Form.Group>
            </div>

            <div className="bg-primary p-2 rounded mt-3">
                <h2>QUALITÉ DU TRAVAIL</h2>
                <p className="fw-bold">
                    Capacité de s’acquitter des tâches sous sa responsabilité
                    en s’imposant personnellement des normes de qualité
                </p>
                <RatingComponent value={formFields.respecteMandatsDemandes}
                                 label="Respecter les mandats qui lui ont été confiés"
                                 setValue={(value: string) => {
                                     setformFields({...formFields, respecteMandatsDemandes: value})
                                 }}/>
                <RatingComponent value={formFields.attentionAuxDetails}
                                 label="Porter attention aux détails dans la réalisation de ses tâches"
                                 setValue={(value: string) => {
                                     setformFields({...formFields, attentionAuxDetails: value})
                                 }}/>
                <RatingComponent value={formFields.doubleCheckTravail}
                                 label="Vérifier son travail, s'assurer que rien n'a été oublié"
                                 setValue={(value: string) => {
                                     setformFields({...formFields, doubleCheckTravail: value})
                                 }}/>
                <RatingComponent value={formFields.occasionsDePerfectionnement}
                                 label="Rechercher des occasions de se perfectionner"
                                 setValue={(value: string) => {
                                     setformFields({...formFields, occasionsDePerfectionnement: value})
                                 }}/>
                <RatingComponent value={formFields.bonneAnalyseProblemes}
                                 label="Faire une bonne analyse des problèmes rencontrés"
                                 setValue={(value: string) => {
                                     setformFields({...formFields, bonneAnalyseProblemes: value})
                                 }}/>
                <Form.Group className="mt-2">
                    <Form.Label>Commentaires</Form.Label>
                    <Form.Control value={formFields.commentairesQualite}
                                  onChange={event => setformFields({
                                      ...formFields,
                                      commentairesQualite: event.target.value
                                  })} as="textarea"
                                  rows={3}/>
                </Form.Group>
            </div>

            <div className="bg-primary rounded p-2 mt-3">
                <h2>QUALITÉ DES RELATIONS INTERPERSONNELLES</h2>
                <p className="fw-bold">
                    Capacité d’établir des interrelations harmonieuses
                    dans son milieu de travail
                </p>
                <RatingComponent value={formFields.planifieTravail}
                                 label="Planifier et organiser son travail de façon efficace"
                                 setValue={(value: string) => setformFields({
                                     ...formFields,
                                     planifieTravail: value
                                 })}/>
                <RatingComponent value={formFields.contactsFaciles}
                                 label="Établir facilement des contacts avec les gens"
                                 setValue={
                                     (value: string) => setformFields({
                                         ...formFields,
                                         contactsFaciles: value
                                     })}/>
                <RatingComponent value={formFields.travailEnEquipe}
                                 label="Contribuer activement au travail d’équipe"
                                 setValue={
                                     (value: string) => setformFields({
                                         ...formFields,
                                         travailEnEquipe: value
                                     })}/>
                <RatingComponent value={formFields.adapteCulture}
                                 label="S’adapter facilement à la culture de l’entreprise"
                                 setValue={
                                     (value: string) => setformFields({
                                         ...formFields,
                                         adapteCulture: value
                                     })}/>
                <RatingComponent value={formFields.accepteCritiques} label="Accepter les critiques constructives"
                                 setValue={
                                     (value: string) => setformFields({
                                         ...formFields,
                                         accepteCritiques: value
                                     })}/>
                <RatingComponent value={formFields.respecteAutres} label="Être respectueux envers les gens"
                                 setValue={
                                     (value: string) => setformFields({
                                         ...formFields,
                                         respecteAutres: value
                                     })}/>
                <RatingComponent value={formFields.ecouteActiveComprendrePDVautre}
                                 label=" Faire preuve d’écoute active en essayant
                                 de comprendre le point de vue de l’autre"
                                 setValue={
                                     (value: string) => setformFields({
                                         ...formFields,
                                         ecouteActiveComprendrePDVautre: value
                                     })}/>
                <Form.Group className="mt-2">
                    <Form.Label>Commentaires</Form.Label>
                    <Form.Control value={formFields.commentairesRelationsInterpersonnelles}
                                  onChange={event =>
                                      setformFields({
                                          ...formFields,
                                          commentairesRelationsInterpersonnelles: event.target.value
                                      })}
                                  as="textarea"
                                  rows={3}/>
                </Form.Group>
            </div>

            <div className="bg-primary rounded p-2 mt-3">
                <h2>HABILETÉS PERSONNELLES</h2>
                <p className="fw-bold">
                    Capacité de faire preuve d’attitudes ou de
                    comportements matures et responsables
                </p>
                <RatingComponent value={formFields.interetMotivation}
                                 label="Démontrer de l’intérêt et de la motivation au travail"
                                 setValue={
                                     (value: string) => setformFields({
                                         ...formFields,
                                         interetMotivation: value
                                     })}/>
                <RatingComponent value={formFields.exprimeIdees}
                                 label="Exprimer clairement ses idées"
                                 setValue={
                                     (value: string) => setformFields({
                                         ...formFields,
                                         exprimeIdees: value
                                     })}/>
                <RatingComponent value={formFields.initiative} label="Faire preuve d’initiative"
                                 setValue={
                                     (value: string) => setformFields({
                                         ...formFields,
                                         initiative: value
                                     })}/>
                <RatingComponent value={formFields.travailSecuritaire} label="Travailler de façon sécuritaire"
                                 setValue={
                                     (value: string) => setformFields({
                                         ...formFields,
                                         travailSecuritaire: value
                                     })}/>
                <RatingComponent value={formFields.responsableAutonome} label="Démontrer un bon sens des responsabilités ne
                                    requérant qu’un minimum de supervision"
                                 setValue={
                                     (value: string) => setformFields({
                                         ...formFields,
                                         responsableAutonome: value
                                     })}/>
                <RatingComponent value={formFields.ponctuel}
                                 label="Être ponctuel et assidu à son travail"
                                 setValue={
                                     (value: string) => setformFields({
                                         ...formFields,
                                         ponctuel: value
                                     })}/>
                <Form.Group className="mt-2">
                    <Form.Label>Commentaires</Form.Label>
                    <Form.Control value={formFields.commentairesHabilites}
                                  onChange={event =>
                                      setformFields({
                                          ...formFields,
                                          commentairesHabilites: event.target.value
                                      })}
                                  as="textarea"
                                  rows={3}/>
                </Form.Group>
            </div>

            <div className="bg-primary p-2 rounded mt-3">
                <h2>APPRÉCIATION GLOBALE DU STAGIARE</h2>
                <Form.Group className="mt-2">
                    <Form.Select value={formFields.habiletesDemontres} required
                                 onChange={event => setformFields({
                                     ...formFields,
                                     habiletesDemontres: event.target.value
                                 })}>
                        <option value="" hidden disabled>Veuillez choisir une option</option>
                        <option value="depassentBeacoupAttentes">
                            Les habiletés démontrées dépassent de beaucoup les attentes
                        </option>
                        <option value="depassentAttentes">
                            Les habiletés démontrées dépassent les attentes
                        </option>
                        <option value="repondentAttentes">
                            Les habiletés démontrées répondent pleinement aux attentes
                        </option>
                        <option value="repondentPartiellementAttentes">
                            Les habiletés démontrées répondent partiellement aux attentes
                        </option>
                        <option value="repondentPasAttentes">
                            Les habiletés démontrées ne répondent pas aux attentes
                        </option>
                    </Form.Select>
                </Form.Group>
                <Form.Group className="mt-2">
                    <Form.Label>Commentaires</Form.Label>
                    <Form.Control value={formFields.commentairesAppreciation}
                                  onChange={event =>
                                      setformFields({
                                          ...formFields,
                                          commentairesAppreciation: event.target.value
                                      })}
                                  as="textarea"
                                  rows={3}/>
                </Form.Group>
                <Form.Group className="mt-2">
                    <Form.Label>Cette évaluation a été discutée avec le stagiaire</Form.Label>
                    <Form.Select value={formFields.discuteAvecStagiaire} required
                                 onChange={event => setformFields({
                                     ...formFields,
                                     discuteAvecStagiaire: event.target.value
                                 })}>
                        <option value="" hidden disabled>Veuillez choisir une option</option>
                        <option value="oui">
                            Oui
                        </option>
                        <option value="non">
                            Non
                        </option>
                    </Form.Select>
                </Form.Group>
                <Form.Group className="mt-2">
                    <Form.Label>
                        Veuillez indiquer le nombre d’heures réel par semaine d’encadrement accordé au stagiaire
                    </Form.Label>
                    <Form.Control value={formFields.heuresEncadrement}
                                  onChange={event => setformFields({
                                      ...formFields,
                                      heuresEncadrement: event.target.value
                                  })}
                                  required
                                  type="number"/>
                </Form.Group>
            </div>

            <div className="bg-primary p-2 rounded mt-3">
                <h2>L'ENTREPRISE AIMERAIT ACCUEILLIR CET ÉLÈVE POUR SON PROCHAIN STAGE</h2>
                <Form.Group className="mt-2">
                    <Form.Select value={formFields.acueillirPourProchainStage} required
                                 onChange={event => setformFields({
                                     ...formFields,
                                     acueillirPourProchainStage: event.target.value
                                 })}>
                        <option value="" hidden disabled>Veuillez choisir une option</option>
                        <option value="oui">
                            Oui
                        </option>
                        <option value="non">
                            Non
                        </option>
                        <option value="peutEtre">
                            Peut-être
                        </option>
                    </Form.Select>
                </Form.Group>
                <Form.Group className="mt-2">
                    <Form.Label>
                        La formation technique du stagiaire était-elle suffisante pour accomplir le mandat de stage?
                    </Form.Label>
                    <Form.Control value={formFields.formationTechniqueSuffisante}
                                  onChange={event => setformFields({
                                      ...formFields,
                                      formationTechniqueSuffisante: event.target.value
                                  })}
                                  as="textarea"
                                  rows={3}/>
                </Form.Group>
            </div>
            <Row>
                <Col sm={4} className="mx-auto mt-3">
                    <SignaturePad canvasProps={{width: 500, height: 200, className: 'border border-5 bg-light'}}
                                  ref={(ref) => {
                                      sigPad = ref
                                  }}/>
                    <Button onClick={() => {
                        sigPad!.clear()
                    }}>Recommencer</Button>
                    {!hasSigned && <h2 className="text-danger">Vous devez signez!</h2>}
                </Col>
            </Row>
            <Row>
                {
                    waiting
                        ? <BeatLoader className="text-center" color="#292b2c"/>
                        : <Button variant="success" className="mt-2" type="submit">Soumettre</Button>
                }
            </Row>
        </Form>
    );
}

export default StudentEvaluationForm;