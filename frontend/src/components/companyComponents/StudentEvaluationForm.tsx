import {Form, FormGroup} from "react-bootstrap";

const StudentEvaluationForm = (): JSX.Element => {

    const RatingComponent = ({
                                 label,
                                 value,
                                 setValue
                             }: { label: string, value: any, setValue: Function }): JSX.Element => {
        return (
            <Form.Group className="mt-2">
                <Form.Label>{label}</Form.Label>
                <Form.Select value={value} required onChange={event => {
                    setValue(event.target.value)
                }}>
                    <option value="" hidden disabled>Veuillez choisir une option</option>
                    <option value="Totalement en accord">Totalement en accord</option>
                    <option value="Plutôt en accord">Plutôt en accord</option>
                    <option value="Plutôt en désaccord">Plutôt en désaccord</option>
                    <option value="Totalement en désaccord">Totalement en désaccord</option>
                    <option value="N/A">N/A</option>
                </Form.Select>
            </Form.Group>
        );
    }

    return (
        <Form className="bg-white text-dark p-3">
            <h1 className="text-center">FICHE D'ÉVALUATION DU STAGIAIRE</h1>
            <div className="border border-5 p-2 border-primary rounded">
                <h2>PRODUCTIVITÉ</h2>
                <p className="fw-bold">Capacité d'optimiser son rendement au travail</p>
                <RatingComponent value={""} label="Planifier et organiser son travail de façon efficace"
                                 setValue={() => {
                                 }}/>
                <RatingComponent value={""} label="Comprendre rapidement les directives relatives à son travail"
                                 setValue={() => {
                                 }}/>
                <RatingComponent value={""} label="Maintenir un rythme de travail soutenu" setValue={() => {
                }}/>
                <RatingComponent value={""} label="Établir ses priorités" setValue={() => {
                }}/>
                <RatingComponent value={""} label="Respecter ses échéanciers" setValue={() => {
                }}/>
                <Form.Group className="mt-2">
                    <Form.Label>Commentaires</Form.Label>
                    <Form.Control as="textarea" rows={3}/>
                </Form.Group>
            </div>

            <div className="border border-5 p-2 border-primary rounded mt-3">
                <h2>QUALITÉ DU TRAVAIL</h2>
                <p className="fw-bold">
                    Capacité de s’acquitter des tâches sous sa responsabilité
                    en s’imposant personnellement des normes de qualité
                </p>
                <RatingComponent value={""} label="Respecter les mandats qui lui ont été confiés" setValue={() => {
                }}/>
                <RatingComponent value={""} label="Porter attention aux détails dans la réalisation de ses tâches"
                                 setValue={() => {
                                 }}/>
                <RatingComponent value={""} label="Vérifier son travail, s'assurer que rien n'a été oublié"
                                 setValue={() => {
                                 }}/>
                <RatingComponent value={""} label="Rechercher des occasions de se perfectionner" setValue={() => {
                }}/>
                <RatingComponent value={""} label="Faire une bonne analyse des problèmes rencontrés" setValue={() => {
                }}/>
                <Form.Group className="mt-2">
                    <Form.Label>Commentaires</Form.Label>
                    <Form.Control as="textarea" rows={3}/>
                </Form.Group>
            </div>

            <div className="border border-5 p-2 border-primary rounded mt-3">
                <h2>QUALITÉ DES RELATIONS INTERPERSONNELLES</h2>
                <p className="fw-bold">
                    Capacité d’établir des interrelations harmonieuses
                    dans son milieu de travail
                </p>
                <RatingComponent value={""} label="Planifier et organiser son travail de façon efficace"
                                 setValue={() => {
                                 }}/>
                <RatingComponent value={""} label="Établir facilement des contacts avec les gens" setValue={() => {
                }}/>
                <RatingComponent value={""} label="Contribuer activement au travail d’équipe" setValue={() => {
                }}/>
                <RatingComponent value={""} label="S’adapter facilement à la culture de l’entreprise" setValue={() => {
                }}/>
                <RatingComponent value={""} label="Accepter les critiques constructives" setValue={() => {
                }}/>
                <RatingComponent value={""} label="Être respectueux envers les gens" setValue={() => {
                }}/>
                <RatingComponent value={""} label=" Faire preuve d’écoute active en essayant de
                    comprendre le point de vue de l’autre" setValue={() => {
                }}/>
                <Form.Group className="mt-2">
                    <Form.Label>Commentaires</Form.Label>
                    <Form.Control as="textarea" rows={3}/>
                </Form.Group>
            </div>

            <div className="border border-5 p-2 border-primary rounded mt-3">
                <h2>HABILETÉS PERSONNELLES</h2>
                <p className="fw-bold">
                    Capacité de faire preuve d’attitudes ou de
                    comportements matures et responsables
                </p>
                <RatingComponent value={""} label="Démontrer de l’intérêt et de la motivation au travail"
                                 setValue={() => {
                                 }}/>
                <RatingComponent value={""} label="Exprimer clairement ses idées" setValue={() => {
                }}/>
                <RatingComponent value={""} label="Faire preuve d’initiative" setValue={() => {
                }}/>
                <RatingComponent value={""} label="Travailler de façon sécuritaire" setValue={() => {
                }}/>
                <RatingComponent value={""} label="Démontrer un bon sens des responsabilités ne
                    requérant qu’un minimum de supervision" setValue={() => {
                }}/>
                <RatingComponent value={""} label="Être ponctuel et assidu à son travail" setValue={() => {
                }}/>
                <Form.Group className="mt-2">
                    <Form.Label>Commentaires</Form.Label>
                    <Form.Control as="textarea" rows={3}/>
                </Form.Group>
            </div>

            <div className="border border-5 p-2 border-primary rounded mt-3">
                <h2>APPRÉCIATION GLOBALE DU STAGIARE</h2>
                <Form.Group className="mt-2">
                    <Form.Select value={""} required onChange={event => {
                    }}>
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
                    <Form.Control as="textarea" rows={3}/>
                </Form.Group>
                <Form.Group className="mt-2">
                    <Form.Label>Cette évaluation a été discutée avec le stagiaire</Form.Label>
                    <Form.Select value={""} required onChange={event => {
                    }}>
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
                    <Form.Control required type="number"/>
                </Form.Group>
            </div>

            <div className="border border-5 p-2 border-primary rounded mt-3">
                <h2>L'ENTREPRISE AIMERAIT ACCUEILLIR CET ÉLÈVE POUR SON PROCHAIN STAGE</h2>
                <Form.Group className="mt-2">
                    <Form.Select value={""} required onChange={event => {
                    }}>
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
                    <Form.Control as="textarea" rows={3}/>
                </FormGroup>
            </div>

            <div className="border border-5 p-2 border-primary rounded mt-3">
                <h2>Vos informations</h2>
                <FormGroup className="mt-2">
                    <Form.Label>Nom</Form.Label>
                    <Form.Control required type="text"/>
                </FormGroup>
                <FormGroup className="mt-2">
                    <Form.Label>Fonction</Form.Label>
                    <Form.Control required type="text"/>
                </FormGroup>
            </div>
        </Form>
    );
}

export default StudentEvaluationForm;