import {Button, Col, Container, Form, Row} from "react-bootstrap";
import React, {useState} from "react";
import RatingComponent from "./RatingComponent";
import IStudentEvaluationFormFields from "../../models/IStudentEvaluationFormFields";
import SignaturePad from "react-signature-canvas";
import {BeatLoader} from "react-spinners";
import IUser from "../../models/IUser";
import {postEvaluateStudent} from "../../services/companyServices/CompanyFetchService";
import {generateAlert} from "../../services/universalServices/UniversalUtilService";
import FormFeedBackInvalid from "../universalComponents/FormFeedBackInvalid";
import LocationChangeConfirmationPopup from "../universalComponents/LocationChangeConfirmationPopup";

const StudentEvaluationForm = ({
                                   contractId,
                                   connectedUser,
                                   setShowEvaluationForm,
                               }: {
    contractId: string,
    connectedUser: IUser,
    setShowEvaluationForm: Function,
}): JSX.Element => {
    const [waiting, setWaiting] = useState<boolean>(false);
    const [hasSigned, setHasSigned] = useState<boolean>(true);
    const [validated, setValidated] = useState<boolean>(false);
    const [formSent, setFormSent] = useState<boolean>(false);
    const [formFields, setFormFields] = useState<IStudentEvaluationFormFields>(new IStudentEvaluationFormFields())
    let sigPad: SignaturePad | null

    const onSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        const form: any = event.currentTarget;

        if (!form.checkValidity()) {
            setValidated(true)
            return;
        }
        setValidated(false)

        if (sigPad?.isEmpty()) {
            setHasSigned(false)
            return
        }

        setWaiting(true);
        formFields.contractId = contractId;
        formFields.signature = sigPad!.toDataURL();
        formFields.dateSignature = new Date().toISOString().split("T")[0];
        const response: Response = await postEvaluateStudent(connectedUser.token, formFields);

        if (response.ok) {
            await new Promise((r) => setTimeout(r, 3000));
            setFormSent(true)
        } else {
            generateAlert()
        }
    }

    if (formSent) {
        return (
            <LocationChangeConfirmationPopup message={"Formulaire envoy??"} locationOnConfirm={"/CompanyContractsPage"}/>
        );
    }

    return (
        <Container className="py-5">
            <Form className="card" onSubmit={event => onSubmit(event)} validated={validated}
                  noValidate>
                <Row className="card-header">
                    <Col sm={2}>
                        <Button variant="danger" onClick={() => {
                            setShowEvaluationForm(false);
                        }}>Fermer</Button>
                    </Col>
                    <Col>
                        <h1 className="text-center">FICHE D'??VALUATION DU STAGIAIRE</h1>
                    </Col>
                    <Col sm={2}></Col>
                </Row>
                <div className="card-body">

                    <div className="p-3 rounded">
                        <h2>PRODUCTIVIT??</h2>
                        <p className="fw-bold">Capacit?? d'optimiser son rendement au travail</p>
                        <RatingComponent value={formFields.travailEfficace}
                                         label="Planifier et organiser son travail de fa??on efficace"
                                         setValue={(value: string) => {
                                             setFormFields({...formFields, travailEfficace: value})
                                         }}/>
                        <RatingComponent value={formFields.comprendRapidement}
                                         label="Comprendre rapidement les directives relatives ?? son travail"
                                         setValue={(value: string) => {
                                             setFormFields({...formFields, comprendRapidement: value})
                                         }}/>
                        <RatingComponent value={formFields.rythmeSoutenu}
                                         label="Maintenir un rythme de travail soutenu"
                                         setValue={(value: string) => {
                                             setFormFields({...formFields, rythmeSoutenu: value})
                                         }}/>
                        <RatingComponent value={formFields.etablirPriorites} label="??tablir ses priorit??s"
                                         setValue={(value: string) => {
                                             setFormFields({...formFields, etablirPriorites: value})
                                         }}/>
                        <RatingComponent value={formFields.respecteEcheances} label="Respecter ses ??ch??anciers"
                                         setValue={(value: string) => {
                                             setFormFields({...formFields, respecteEcheances: value})
                                         }}/>
                        <Form.Group className="mt-2">
                            <Form.Label>Commentaires</Form.Label>
                            <Form.Control value={formFields.commentairesProductivite}
                                          onChange={event =>
                                              setFormFields({
                                                  ...formFields,
                                                  commentairesProductivite: event.target.value
                                              })}
                                          as="textarea"
                                          rows={3}/>
                        </Form.Group>
                    </div>

                    <div className="p-3 rounded mt-3">
                        <h2>QUALIT?? DU TRAVAIL</h2>
                        <p className="fw-bold">
                            Capacit?? de s???acquitter des t??ches sous sa responsabilit??
                            en s???imposant personnellement des normes de qualit??
                        </p>
                        <RatingComponent value={formFields.respecteMandatsDemandes}
                                         label="Respecter les mandats qui lui ont ??t?? confi??s"
                                         setValue={(value: string) => {
                                             setFormFields({...formFields, respecteMandatsDemandes: value})
                                         }}/>
                        <RatingComponent value={formFields.attentionAuxDetails}
                                         label="Porter attention aux d??tails dans la r??alisation de ses t??ches"
                                         setValue={(value: string) => {
                                             setFormFields({...formFields, attentionAuxDetails: value})
                                         }}/>
                        <RatingComponent value={formFields.doubleCheckTravail}
                                         label="V??rifier son travail, s'assurer que rien n'a ??t?? oubli??"
                                         setValue={(value: string) => {
                                             setFormFields({...formFields, doubleCheckTravail: value})
                                         }}/>
                        <RatingComponent value={formFields.occasionsDePerfectionnement}
                                         label="Rechercher des occasions de se perfectionner"
                                         setValue={(value: string) => {
                                             setFormFields({...formFields, occasionsDePerfectionnement: value})
                                         }}/>
                        <RatingComponent value={formFields.bonneAnalyseProblemes}
                                         label="Faire une bonne analyse des probl??mes rencontr??s"
                                         setValue={(value: string) => {
                                             setFormFields({...formFields, bonneAnalyseProblemes: value})
                                         }}/>
                        <Form.Group className="mt-2">
                            <Form.Label>Commentaires</Form.Label>
                            <Form.Control value={formFields.commentairesQualite}
                                          onChange={event => setFormFields({
                                              ...formFields,
                                              commentairesQualite: event.target.value
                                          })} as="textarea"
                                          rows={3}/>
                        </Form.Group>
                    </div>

                    <div className="rounded p-3 mt-3">
                        <h2>QUALIT?? DES RELATIONS INTERPERSONNELLES</h2>
                        <p className="fw-bold">
                            Capacit?? d?????tablir des interrelations harmonieuses
                            dans son milieu de travail
                        </p>
                        <RatingComponent value={formFields.planifieTravail}
                                         label="Planifier et organiser son travail de fa??on efficace"
                                         setValue={(value: string) => setFormFields({
                                             ...formFields,
                                             planifieTravail: value
                                         })}/>
                        <RatingComponent value={formFields.contactsFaciles}
                                         label="??tablir facilement des contacts avec les gens"
                                         setValue={
                                             (value: string) => setFormFields({
                                                 ...formFields,
                                                 contactsFaciles: value
                                             })}/>
                        <RatingComponent value={formFields.travailEnEquipe}
                                         label="Contribuer activement au travail d?????quipe"
                                         setValue={
                                             (value: string) => setFormFields({
                                                 ...formFields,
                                                 travailEnEquipe: value
                                             })}/>
                        <RatingComponent value={formFields.adapteCulture}
                                         label="S???adapter facilement ?? la culture de l???entreprise"
                                         setValue={
                                             (value: string) => setFormFields({
                                                 ...formFields,
                                                 adapteCulture: value
                                             })}/>
                        <RatingComponent value={formFields.accepteCritiques}
                                         label="Accepter les critiques constructives"
                                         setValue={
                                             (value: string) => setFormFields({
                                                 ...formFields,
                                                 accepteCritiques: value
                                             })}/>
                        <RatingComponent value={formFields.respecteAutres} label="??tre respectueux envers les gens"
                                         setValue={
                                             (value: string) => setFormFields({
                                                 ...formFields,
                                                 respecteAutres: value
                                             })}/>
                        <RatingComponent value={formFields.ecouteActiveComprendrePDVautre}
                                         label=" Faire preuve d?????coute active en essayant
                                 de comprendre le point de vue de l???autre"
                                         setValue={
                                             (value: string) => setFormFields({
                                                 ...formFields,
                                                 ecouteActiveComprendrePDVautre: value
                                             })}/>
                        <Form.Group className="mt-2">
                            <Form.Label>Commentaires</Form.Label>
                            <Form.Control value={formFields.commentairesRelationsInterpersonnelles}
                                          onChange={event =>
                                              setFormFields({
                                                  ...formFields,
                                                  commentairesRelationsInterpersonnelles: event.target.value
                                              })}
                                          as="textarea"
                                          rows={3}/>
                        </Form.Group>
                    </div>

                    <div className="rounded p-3 mt-3">
                        <h2>HABILET??S PERSONNELLES</h2>
                        <p className="fw-bold">
                            Capacit?? de faire preuve d???attitudes ou de
                            comportements matures et responsables
                        </p>
                        <RatingComponent value={formFields.interetMotivation}
                                         label="D??montrer de l???int??r??t et de la motivation au travail"
                                         setValue={
                                             (value: string) => setFormFields({
                                                 ...formFields,
                                                 interetMotivation: value
                                             })}/>
                        <RatingComponent value={formFields.exprimeIdees}
                                         label="Exprimer clairement ses id??es"
                                         setValue={
                                             (value: string) => setFormFields({
                                                 ...formFields,
                                                 exprimeIdees: value
                                             })}/>
                        <RatingComponent value={formFields.initiative} label="Faire preuve d???initiative"
                                         setValue={
                                             (value: string) => setFormFields({
                                                 ...formFields,
                                                 initiative: value
                                             })}/>
                        <RatingComponent value={formFields.travailSecuritaire} label="Travailler de fa??on s??curitaire"
                                         setValue={
                                             (value: string) => setFormFields({
                                                 ...formFields,
                                                 travailSecuritaire: value
                                             })}/>
                        <RatingComponent value={formFields.responsableAutonome} label="D??montrer un bon sens des responsabilit??s ne
                                    requ??rant qu???un minimum de supervision"
                                         setValue={
                                             (value: string) => setFormFields({
                                                 ...formFields,
                                                 responsableAutonome: value
                                             })}/>
                        <RatingComponent value={formFields.ponctuel}
                                         label="??tre ponctuel et assidu ?? son travail"
                                         setValue={
                                             (value: string) => setFormFields({
                                                 ...formFields,
                                                 ponctuel: value
                                             })}/>
                        <Form.Group className="mt-2">
                            <Form.Label>Commentaires</Form.Label>
                            <Form.Control value={formFields.commentairesHabilites}
                                          onChange={event =>
                                              setFormFields({
                                                  ...formFields,
                                                  commentairesHabilites: event.target.value
                                              })}
                                          as="textarea"
                                          rows={3}/>
                        </Form.Group>
                    </div>

                    <div className="p-3 rounded mt-3">
                        <h2>APPR??CIATION GLOBALE DU STAGIARE</h2>
                        <Form.Group className="mt-2">
                            <Form.Select value={formFields.habiletesDemontres} required
                                         onChange={event => setFormFields({
                                             ...formFields,
                                             habiletesDemontres: event.target.value
                                         })}>
                                <option value="" hidden disabled>Veuillez choisir une option</option>
                                <option value="depassentBeacoupAttentes">
                                    Les habilet??s d??montr??es d??passent de beaucoup les attentes
                                </option>
                                <option value="depassentAttentes">
                                    Les habilet??s d??montr??es d??passent les attentes
                                </option>
                                <option value="repondentAttentes">
                                    Les habilet??s d??montr??es r??pondent pleinement aux attentes
                                </option>
                                <option value="repondentPartiellementAttentes">
                                    Les habilet??s d??montr??es r??pondent partiellement aux attentes
                                </option>
                                <option value="repondentPasAttentes">
                                    Les habilet??s d??montr??es ne r??pondent pas aux attentes
                                </option>
                            </Form.Select>
                            <FormFeedBackInvalid/>
                        </Form.Group>
                        <Form.Group className="mt-2">
                            <Form.Label>Commentaires</Form.Label>
                            <Form.Control value={formFields.commentairesAppreciation}
                                          onChange={event =>
                                              setFormFields({
                                                  ...formFields,
                                                  commentairesAppreciation: event.target.value
                                              })}
                                          as="textarea"
                                          rows={3}/>
                        </Form.Group>
                        <Form.Group className="mt-2">
                            <Form.Label>Cette ??valuation a ??t?? discut??e avec le stagiaire</Form.Label>
                            <Form.Select value={formFields.discuteAvecStagiaire} required
                                         onChange={event => setFormFields({
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
                            <FormFeedBackInvalid/>
                        </Form.Group>
                        <Form.Group className="mt-2">
                            <Form.Label>
                                Veuillez indiquer le nombre d???heures r??el par semaine d???encadrement accord?? au stagiaire
                            </Form.Label>
                            <Form.Control value={formFields.heuresEncadrement}
                                          onChange={event => setFormFields({
                                              ...formFields,
                                              heuresEncadrement: event.target.value
                                          })}
                                          min={0}
                                          max={40}
                                          required
                                          type="number"/>
                            <Form.Control.Feedback type="invalid">Doit ??tre entre 0 et 40
                                (inclusif)</Form.Control.Feedback>
                        </Form.Group>
                    </div>

                    <div className="p-3 rounded mt-3">
                        <h2>L'ENTREPRISE AIMERAIT ACCUEILLIR CET ??L??VE POUR SON PROCHAIN STAGE</h2>
                        <Form.Group className="mt-2">
                            <Form.Select value={formFields.acueillirPourProchainStage} required
                                         onChange={event => setFormFields({
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
                                    Peut-??tre
                                </option>
                            </Form.Select>
                            <FormFeedBackInvalid/>
                        </Form.Group>
                        <Form.Group className="mt-2">
                            <Form.Label>
                                La formation technique du stagiaire ??tait-elle suffisante pour accomplir le mandat de
                                stage?
                            </Form.Label>
                            <Form.Control required
                                          value={formFields.formationTechniqueSuffisante}
                                          onChange={event => setFormFields({
                                              ...formFields,
                                              formationTechniqueSuffisante: event.target.value
                                          })}
                                          as="textarea"
                                          rows={3}/>
                            <FormFeedBackInvalid/>
                        </Form.Group>
                    </div>
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
                            {validated && <h1 className="text-danger">Vous devez remplir tous les champs!</h1>}
                            {
                                waiting
                                    ? <BeatLoader color="#0275d8 " size={25}/>
                                    :
                                    <Button variant="success w-100" className="mt-2 text-center"
                                            type="submit">Soumettre</Button>
                            }
                        </Col>
                    </Row>
                </div>
            </Form>
        </Container>
    );
}

export default StudentEvaluationForm;