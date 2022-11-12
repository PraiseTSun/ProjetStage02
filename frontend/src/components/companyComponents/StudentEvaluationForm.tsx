import {Form} from "react-bootstrap";
import {useState} from "react";
import RatingComponent from "./RatingComponent";
import IStudentEvaluationFormFields from "../../models/IStudentEvaluationFormFields";

const StudentEvaluationForm = (): JSX.Element => {
    const [validated, setValidated] = useState<boolean>(false);
    const [waiting, setWaiting] = useState<boolean>(false);
    const [formFields, setformFields] = useState<IStudentEvaluationFormFields>(new IStudentEvaluationFormFields())

    return (
        <Form className="bg-white text-dark p-3">
            <h1 className="text-center">FICHE D'ÉVALUATION DU STAGIAIRE</h1>
            <div className="bg-primary p-2 rounded">
                <h2>PRODUCTIVITÉ</h2>
                <p className="fw-bold">Capacité d'optimiser son rendement au travail</p>
                <RatingComponent value={formFields.productivityOne}
                                 label="Planifier et organiser son travail de façon efficace"
                                 setValue={(value: string) => {
                                     setformFields({...formFields, productivityOne: value})
                                 }}/>
                <RatingComponent value={formFields.productivityTwo}
                                 label="Comprendre rapidement les directives relatives à son travail"
                                 setValue={(value: string) => {
                                     setformFields({...formFields, productivityTwo: value})
                                 }}/>
                <RatingComponent value={formFields.productivityThree}
                                 label="Maintenir un rythme de travail soutenu"
                                 setValue={(value: string) => {
                                     setformFields({...formFields, productivityThree: value})
                                 }}/>
                <RatingComponent value={formFields.productivityFour} label="Établir ses priorités"
                                 setValue={(value: string) => {
                                     setformFields({...formFields, productivityFour: value})
                                 }}/>
                <RatingComponent value={formFields.productivityFive} label="Respecter ses échéanciers"
                                 setValue={(value: string) => {
                                     setformFields({...formFields, productivityFive: value})
                                 }}/>
                <Form.Group className="mt-2">
                    <Form.Label>Commentaires</Form.Label>
                    <Form.Control value={formFields.productivityComments}
                                  onChange={event =>
                                      setformFields({...formFields, productivityComments: event.target.value})}
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
                <RatingComponent value={formFields.workQualityOne} label="Respecter les mandats qui lui ont été confiés"
                                 setValue={(value: string) => {
                                     setformFields({...formFields, workQualityOne: value})
                                 }}/>
                <RatingComponent value={formFields.workQualityTwo}
                                 label="Porter attention aux détails dans la réalisation de ses tâches"
                                 setValue={(value: string) => {
                                     setformFields({...formFields, workQualityTwo: value})
                                 }}/>
                <RatingComponent value={formFields.workQualityThree}
                                 label="Vérifier son travail, s'assurer que rien n'a été oublié"
                                 setValue={(value: string) => {
                                     setformFields({...formFields, workQualityThree: value})
                                 }}/>
                <RatingComponent value={formFields.workQualityFour} label="Rechercher des occasions de se perfectionner"
                                 setValue={(value: string) => {
                                     setformFields({...formFields, workQualityFour: value})
                                 }}/>
                <RatingComponent value={formFields.workQualityFive}
                                 label="Faire une bonne analyse des problèmes rencontrés"
                                 setValue={(value: string) => {
                                     setformFields({...formFields, workQualityFive: value})
                                 }}/>
                <Form.Group className="mt-2">
                    <Form.Label>Commentaires</Form.Label>
                    <Form.Control value={formFields.workQualityComments}
                                  onChange={event => setformFields({
                                      ...formFields,
                                      workQualityComments: event.target.value
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
                <RatingComponent value={formFields.workRelationshipsOne}
                                 label="Planifier et organiser son travail de façon efficace"
                                 setValue={(value: string) => setformFields({
                                     ...formFields,
                                     workRelationshipsOne: value
                                 })}/>
                <RatingComponent value={formFields.workRelationshipsTwo}
                                 label="Établir facilement des contacts avec les gens"
                                 setValue={
                                     (value: string) => setformFields({
                                         ...formFields,
                                         workRelationshipsTwo: value
                                     })}/>
                <RatingComponent value={formFields.workRelationshipsThree}
                                 label="Contribuer activement au travail d’équipe"
                                 setValue={
                                     (value: string) => setformFields({
                                         ...formFields,
                                         workRelationshipsThree: value
                                     })}/>
                <RatingComponent value={formFields.workRelationshipsFour}
                                 label="S’adapter facilement à la culture de l’entreprise"
                                 setValue={
                                     (value: string) => setformFields({
                                         ...formFields,
                                         workRelationshipsFour: value
                                     })}/>
                <RatingComponent value={formFields.workRelationshipsFive} label="Accepter les critiques constructives"
                                 setValue={
                                     (value: string) => setformFields({
                                         ...formFields,
                                         workRelationshipsFive: value
                                     })}/>
                <RatingComponent value={formFields.workRelationshipsSix} label="Être respectueux envers les gens"
                                 setValue={
                                     (value: string) => setformFields({
                                         ...formFields,
                                         workRelationshipsSix: value
                                     })}/>
                <RatingComponent value={formFields.workRelationshipsSeven}
                                 label=" Faire preuve d’écoute active en essayant
                                 de comprendre le point de vue de l’autre"
                                 setValue={
                                     (value: string) => setformFields({
                                         ...formFields,
                                         workRelationshipsSeven: value
                                     })}/>
                <Form.Group className="mt-2">
                    <Form.Label>Commentaires</Form.Label>
                    <Form.Control value={formFields.workRelationshipsComments}
                                  onChange={event =>
                                      setformFields({
                                          ...formFields,
                                          workRelationshipsComments: event.target.value
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
                <RatingComponent value={formFields.personalAbilitiesOne}
                                 label="Démontrer de l’intérêt et de la motivation au travail"
                                 setValue={
                                     (value: string) => setformFields({
                                         ...formFields,
                                         personalAbilitiesOne: value
                                     })}/>
                <RatingComponent value={formFields.personalAbilitiesTwo}
                                 label="Exprimer clairement ses idées"
                                 setValue={
                                     (value: string) => setformFields({
                                         ...formFields,
                                         personalAbilitiesTwo: value
                                     })}/>
                <RatingComponent value={formFields.personalAbilitiesThree} label="Faire preuve d’initiative"
                                 setValue={
                                     (value: string) => setformFields({
                                         ...formFields,
                                         personalAbilitiesThree: value
                                     })}/>
                <RatingComponent value={formFields.personalAbilitiesFour} label="Travailler de façon sécuritaire"
                                 setValue={
                                     (value: string) => setformFields({
                                         ...formFields,
                                         personalAbilitiesFour: value
                                     })}/>
                <RatingComponent value={formFields.personalAbilitiesFive} label="Démontrer un bon sens des responsabilités ne
                                    requérant qu’un minimum de supervision"
                                 setValue={
                                     (value: string) => setformFields({
                                         ...formFields,
                                         personalAbilitiesFive: value
                                     })}/>
                <RatingComponent value={formFields.personalAbilitiesSix}
                                 label="Être ponctuel et assidu à son travail"
                                 setValue={
                                     (value: string) => setformFields({
                                         ...formFields,
                                         personalAbilitiesSix: value
                                     })}/>
                <Form.Group className="mt-2">
                    <Form.Label>Commentaires</Form.Label>
                    <Form.Control value={formFields.personalAbilitiesComments}
                                  onChange={event =>
                                      setformFields({
                                          ...formFields,
                                          personalAbilitiesComments: event.target.value
                                      })}
                                  as="textarea"
                                  rows={3}/>
                </Form.Group>
            </div>

            <div className="bg-primary p-2 rounded mt-3">
                <h2>APPRÉCIATION GLOBALE DU STAGIARE</h2>
                <Form.Group className="mt-2">
                    <Form.Select value={formFields.globalAppreciation} required
                                 onChange={event => setformFields({
                                     ...formFields,
                                     globalAppreciation: event.target.value
                                 })}>
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
                    <Form.Control value={formFields.globalAppreciationComments}
                                  onChange={event =>
                                      setformFields({
                                          ...formFields,
                                          globalAppreciationComments: event.target.value
                                      })}
                                  as="textarea"
                                  rows={3}/>
                </Form.Group>
                <Form.Group className="mt-2">
                    <Form.Label>Cette évaluation a été discutée avec le stagiaire</Form.Label>
                    <Form.Select value={formFields.evaluationSharedWithStudent} required
                                 onChange={event => setformFields({
                                     ...formFields,
                                     evaluationSharedWithStudent: event.target.value
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
                    <Form.Control value={formFields.weeklyMentoringHours}
                                  onChange={event => setformFields({
                                      ...formFields,
                                      weeklyMentoringHours: event.target.value
                                  })}
                                  required
                                  type="number"/>
                </Form.Group>
            </div>

            <div className="bg-primary p-2 rounded mt-3">
                <h2>L'ENTREPRISE AIMERAIT ACCUEILLIR CET ÉLÈVE POUR SON PROCHAIN STAGE</h2>
                <Form.Group className="mt-2">
                    <Form.Select value={formFields.companyWouldHireAgain} required
                                 onChange={event => setformFields({
                                     ...formFields,
                                     companyWouldHireAgain: event.target.value
                                 })}>
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
                <Form.Group className="mt-2">
                    <Form.Label>
                        La formation technique du stagiaire était-elle suffisante pour accomplir le mandat de stage?
                    </Form.Label>
                    <Form.Control value={formFields.wasTheDecEnough}
                                  onChange={event => setformFields({
                                      ...formFields,
                                      wasTheDecEnough: event.target.value
                                  })}
                                  as="textarea"
                                  rows={3}/>
                </Form.Group>
            </div>
        </Form>
    );
}

export default StudentEvaluationForm;