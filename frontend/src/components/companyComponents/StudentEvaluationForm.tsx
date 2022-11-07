import {Button, Form, FormGroup} from "react-bootstrap";
import {useState} from "react";
import RatingComponent from "./RatingComponent";

const StudentEvaluationForm = (): JSX.Element => {
    const [validated, setValidated] = useState(false);
    const [waiting, setWaiting] = useState(false);

    const [productivityOne, setProductivityOne] = useState<string>("");
    const [productivityTwo, setProductivityTwo] = useState<string>("");
    const [productivityThree, setProductivityThree] = useState<string>("");
    const [productivityFour, setProductivityFour] = useState<string>("");
    const [productivityFive, setProductivityFive] = useState<string>("");
    const [productivityComments, setProductivityComments] = useState<string>("");

    const [workQualityOne, setWorkQualityOne] = useState<string>("");
    const [workQualityTwo, setWorkQualityTwo] = useState<string>("");
    const [workQualityThree, setWorkQualityThree] = useState<string>("");
    const [workQualityFour, setWorkQualityFour] = useState<string>("");
    const [workQualityFive, setWorkQualityFive] = useState<string>("");
    const [workQualityComments, setWorkQualityComments] = useState<string>("");

    const [workRelationshipsOne, setWorkRelationshipsOne] = useState<string>("");
    const [workRelationshipsTwo, setWorkRelationshipsTwo] = useState<string>("");
    const [workRelationshipsThree, setWorkRelationshipsThree] = useState<string>("");
    const [workRelationshipsFour, setWorkRelationshipsFour] = useState<string>("");
    const [workRelationshipsFive, setWorkRelationshipsFive] = useState<string>("");
    const [workRelationshipsSix, setWorkRelationshipsSix] = useState<string>("");
    const [workRelationshipsSeven, setWorkRelationshipsSeven] = useState<string>("");
    const [workRelationshipsComments, setWorkRelationshipsComments] = useState<string>("");

    const [personalAbilitiesOne, setPersonalAbilitiesOne] = useState<string>("");
    const [personalAbilitiesTwo, setPersonalAbilitiesTwo] = useState<string>("");
    const [personalAbilitiesThree, setPersonalAbilitiesThree] = useState<string>("");
    const [personalAbilitiesFour, setPersonalAbilitiesFour] = useState<string>("");
    const [personalAbilitiesFive, setPersonalAbilitiesFive] = useState<string>("");
    const [personalAbilitiesSix, setPersonalAbilitiesSix] = useState<string>("");
    const [personalAbilitiesComments, setPersonalAbilitiesComments] = useState<string>("");

    const [globalAppreciation, setGlobalAppreciation] = useState<string>("");
    const [globalAppreciationComments, setGlobalAppreciationComments] = useState<string>("");
    const [evaluationSharedWithStudent, setEvaluationSharedWithStudent] = useState<string>("");
    const [weeklyMentoringHours, setWeeklyMentoringHours] = useState<string>();

    const [companyWouldHireAgain, setCompanyWouldHireAgain] = useState<string>("");
    const [wasTheDecEnough, setWasTheDecEnough] = useState<string>("");

    const [name, setName] = useState<string>("");
    const [title, setTitle] = useState<string>("");

    return (
        <Form className="bg-white text-dark p-3">
            <h1 className="text-center">FICHE D'ÉVALUATION DU STAGIAIRE</h1>
            <div className="border border-5 p-2 border-primary rounded">
                <h2>PRODUCTIVITÉ</h2>
                <p className="fw-bold">Capacité d'optimiser son rendement au travail</p>
                <RatingComponent value={productivityOne} label="Planifier et organiser son travail de façon efficace"
                                 setValue={setProductivityOne}/>
                <RatingComponent value={productivityTwo}
                                 label="Comprendre rapidement les directives relatives à son travail"
                                 setValue={setProductivityTwo}/>
                <RatingComponent value={productivityThree} label="Maintenir un rythme de travail soutenu"
                                 setValue={setProductivityThree}/>
                <RatingComponent value={productivityFour} label="Établir ses priorités"
                                 setValue={setProductivityFour}/>
                <RatingComponent value={productivityFive} label="Respecter ses échéanciers"
                                 setValue={setProductivityFive}/>
                <Form.Group className="mt-2">
                    <Form.Label>Commentaires</Form.Label>
                    <Form.Control value={productivityComments}
                                  onChange={event => setProductivityComments(event.target.value)}
                                  as="textarea"
                                  rows={3}/>
                </Form.Group>
            </div>

            <div className="border border-5 p-2 border-primary rounded mt-3">
                <h2>QUALITÉ DU TRAVAIL</h2>
                <p className="fw-bold">
                    Capacité de s’acquitter des tâches sous sa responsabilité
                    en s’imposant personnellement des normes de qualité
                </p>
                <RatingComponent value={workQualityOne} label="Respecter les mandats qui lui ont été confiés"
                                 setValue={setWorkQualityOne}/>
                <RatingComponent value={workQualityTwo}
                                 label="Porter attention aux détails dans la réalisation de ses tâches"
                                 setValue={setWorkQualityTwo}/>
                <RatingComponent value={workQualityThree}
                                 label="Vérifier son travail, s'assurer que rien n'a été oublié"
                                 setValue={setWorkQualityThree}/>
                <RatingComponent value={workQualityFour} label="Rechercher des occasions de se perfectionner"
                                 setValue={setWorkQualityFour}/>
                <RatingComponent value={workQualityFive} label="Faire une bonne analyse des problèmes rencontrés"
                                 setValue={setWorkQualityFive}/>
                <Form.Group className="mt-2">
                    <Form.Label>Commentaires</Form.Label>
                    <Form.Control value={workQualityComments}
                                  onChange={event => setWorkQualityComments(event.target.value)} as="textarea"
                                  rows={3}/>
                </Form.Group>
            </div>

            <div className="border border-5 p-2 border-primary rounded mt-3">
                <h2>QUALITÉ DES RELATIONS INTERPERSONNELLES</h2>
                <p className="fw-bold">
                    Capacité d’établir des interrelations harmonieuses
                    dans son milieu de travail
                </p>
                <RatingComponent value={workRelationshipsOne}
                                 label="Planifier et organiser son travail de façon efficace"
                                 setValue={setWorkRelationshipsOne}/>
                <RatingComponent value={workRelationshipsTwo} label="Établir facilement des contacts avec les gens"
                                 setValue={setWorkRelationshipsTwo}/>
                <RatingComponent value={workRelationshipsThree} label="Contribuer activement au travail d’équipe"
                                 setValue={setWorkRelationshipsThree}/>
                <RatingComponent value={workRelationshipsFour}
                                 label="S’adapter facilement à la culture de l’entreprise"
                                 setValue={setWorkRelationshipsFour}/>
                <RatingComponent value={workRelationshipsFive} label="Accepter les critiques constructives"
                                 setValue={setWorkRelationshipsFive}/>
                <RatingComponent value={workRelationshipsSix} label="Être respectueux envers les gens"
                                 setValue={setWorkRelationshipsSix}/>
                <RatingComponent value={workRelationshipsSeven}
                                 label=" Faire preuve d’écoute active en essayant
                                 de comprendre le point de vue de l’autre"
                                 setValue={setWorkRelationshipsSeven}/>
                <Form.Group className="mt-2">
                    <Form.Label>Commentaires</Form.Label>
                    <Form.Control value={workRelationshipsComments}
                                  onChange={event => setWorkRelationshipsComments(event.target.value)}
                                  as="textarea"
                                  rows={3}/>
                </Form.Group>
            </div>

            <div className="border border-5 p-2 border-primary rounded mt-3">
                <h2>HABILETÉS PERSONNELLES</h2>
                <p className="fw-bold">
                    Capacité de faire preuve d’attitudes ou de
                    comportements matures et responsables
                </p>
                <RatingComponent value={personalAbilitiesOne}
                                 label="Démontrer de l’intérêt et de la motivation au travail"
                                 setValue={setPersonalAbilitiesOne}/>
                <RatingComponent value={personalAbilitiesTwo} label="Exprimer clairement ses idées"
                                 setValue={setPersonalAbilitiesTwo}/>
                <RatingComponent value={personalAbilitiesThree} label="Faire preuve d’initiative"
                                 setValue={setPersonalAbilitiesThree}/>
                <RatingComponent value={personalAbilitiesFour} label="Travailler de façon sécuritaire"
                                 setValue={setPersonalAbilitiesFour}/>
                <RatingComponent value={personalAbilitiesFive} label="Démontrer un bon sens des responsabilités ne
                                    requérant qu’un minimum de supervision"
                                 setValue={setPersonalAbilitiesFive}/>
                <RatingComponent value={personalAbilitiesSix}
                                 label="Être ponctuel et assidu à son travail"
                                 setValue={setPersonalAbilitiesSix}/>
                <Form.Group className="mt-2">
                    <Form.Label>Commentaires</Form.Label>
                    <Form.Control value={personalAbilitiesComments}
                                  onChange={event => setPersonalAbilitiesComments(event.target.value)}
                                  as="textarea"
                                  rows={3}/>
                </Form.Group>
            </div>

            <div className="border border-5 p-2 border-primary rounded mt-3">
                <h2>APPRÉCIATION GLOBALE DU STAGIARE</h2>
                <Form.Group className="mt-2">
                    <Form.Select value={globalAppreciation} required
                                 onChange={event => setGlobalAppreciation(event.target.value)}>
                        <option value="" hidden disabled>Veuillez choisir une option</option>
                        <option value="Les habiletés démontrées dépassent de beaucoup les attentes">
                            Les habiletés démontrées dépassent de beaucoup les attentes
                        </option>
                        <option value="Les habiletés démontrées dépassent les attentes">
                            Les habiletés démontrées dépassent les attentes
                        </option>
                        <option value="Les habiletés démontrées répondent pleinement aux attentes">
                            Les habiletés démontrées répondent pleinement aux attentes
                        </option>
                        <option value="Les habiletés démontrées répondent partiellement aux attentes">
                            Les habiletés démontrées répondent partiellement aux attentes
                        </option>
                        <option value="Les habiletés démontrées ne répondent pas aux attentes">
                            Les habiletés démontrées ne répondent pas aux attentes
                        </option>
                    </Form.Select>
                </Form.Group>
                <Form.Group className="mt-2">
                    <Form.Label>Commentaires</Form.Label>
                    <Form.Control value={globalAppreciationComments}
                                  onChange={event => setGlobalAppreciationComments(event.target.value)}
                                  as="textarea"
                                  rows={3}/>
                </Form.Group>
                <Form.Group className="mt-2">
                    <Form.Label>Cette évaluation a été discutée avec le stagiaire</Form.Label>
                    <Form.Select value={evaluationSharedWithStudent} required
                                 onChange={event => setEvaluationSharedWithStudent(event.target.value)}>
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
                    <Form.Control value={weeklyMentoringHours}
                                  onChange={event => setWeeklyMentoringHours(event.target.value)} required
                                  type="number"/>
                </Form.Group>
            </div>

            <div className="border border-5 p-2 border-primary rounded mt-3">
                <h2>L'ENTREPRISE AIMERAIT ACCUEILLIR CET ÉLÈVE POUR SON PROCHAIN STAGE</h2>
                <Form.Group className="mt-2">
                    <Form.Select value={companyWouldHireAgain} required
                                 onChange={event => setCompanyWouldHireAgain(event.target.value)}>
                        <option value="" hidden disabled>Veuillez choisir une option</option>
                        <option value="oui">
                            Oui
                        </option>
                        <option value="non">
                            Non
                        </option>
                        <option value="peut-être">
                            Peut-être
                        </option>
                    </Form.Select>
                </Form.Group>
                <FormGroup className="mt-2">
                    <Form.Label>
                        La formation technique du stagiaire était-elle suffisante pour accomplir le mandat de stage?
                    </Form.Label>
                    <Form.Control value={wasTheDecEnough}
                                  onChange={event => setWasTheDecEnough(event.target.value)}
                                  as="textarea"
                                  rows={3}/>
                </FormGroup>
            </div>

            <div className="border border-5 p-2 border-primary rounded mt-3">
                <h2>Vos informations</h2>
                <FormGroup className="mt-2">
                    <Form.Label>Nom</Form.Label>
                    <Form.Control value={name} type="text"
                                  onChange={event => setName(event.target.value)} required/>
                </FormGroup>
                <FormGroup className="mt-2">
                    <Form.Label>Fonction</Form.Label>
                    <Form.Control required type="text" value={title}
                                  onChange={event => setTitle(event.target.value)}/>
                </FormGroup>
            </div>

            <Button className="mt-3" type="submit">Soumettre</Button>
        </Form>
    );
}

export default StudentEvaluationForm;