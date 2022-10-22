import React, {useState} from "react";
import {Button, Container, Form, FormGroup, Row} from "react-bootstrap";
import {BeatLoader} from "react-spinners";

const FormulaireInscriptionEntreprise = ({onInscrire}: { onInscrire: Function }) => {
    const [waiting, setWaiting] = useState(false);
    const [validated, setValidated] = useState(false);
    const [nom, setNom] = useState("")
    const [prenom, setPrenom] = useState("")
    const [entreprise, setEntreprise] = useState("")
    const [departement, setDepartement] = useState('')
    const [courriel, setCourriel] = useState('')
    const [motDePasse, setMotDePasse] = useState('')
    const [validationMotDePasse, setValidationMotDePasse] = useState('')

    const validEmail = new RegExp(
        '^[a-zA-Z0-9._:$!%-]+@[a-zA-Z0-9.-]+.[a-zA-Z]$'
    );

    const onSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        const form: any = e.currentTarget;
        e.preventDefault();
        if (form.checkValidity()) {
            setWaiting(true)
            if (departement === '' || departement === 'Choix un département') {
                alert("Dois choisir departement")
                return
            }


            if (motDePasse !== validationMotDePasse) {
                alert("Vérifier le mot de passe et le mot de passe ne correspondent pas")
                return
            }

            if (!validEmail.test(courriel)) {
                return;
            }

            onInscrire({
                lastName: nom, firstName: prenom, companyName: entreprise, department: departement,
                email: courriel, password: motDePasse
            }, 'Company')
        }
        setValidated(true)

    }

    if (waiting) {
        return <BeatLoader className="text-center" color="#292b2c" size={100}/>
    }

    return (
        <Container>

            <Form onSubmit={onSubmit} validated={validated} noValidate>

                <Row>
                    <FormGroup className="mb-3 col-6">
                        <Form.Label data-testid="nomLabelFormulaireEntreprise">Nom</Form.Label>
                        <Form.Control data-testid="nomInputFormulaireEntreprise" type='text' required placeholder='Nom'
                                      value={nom} minLength={2}
                                      onChange={(e) => setNom(e.target.value)}/>
                        <Form.Control.Feedback data-testid="errorNomFormulaireEntreprise" type="invalid">Minimum de deux
                            charactères</Form.Control.Feedback>
                    </FormGroup>

                    <FormGroup className="mb-3 col-6">
                        <Form.Label data-testid="prenomLabelFormulaireEntreprise">Prénom</Form.Label>
                        <Form.Control data-testid="prenomInputFormulaireEntreprise" type='text' required
                                      placeholder='Prénom'
                                      value={prenom} minLength={2}
                                      onChange={(e) => setPrenom(e.target.value)}/>
                        <Form.Control.Feedback data-testid="errorPrenomFormulaireEntreprise" type="invalid">Minimum de
                            deux charactères</Form.Control.Feedback>
                    </FormGroup>
                </Row>
                <FormGroup className="mb-3">
                    <Form.Label data-testid="labelEntrepriseFormulaireEntreprise">Entreprise</Form.Label>
                    <Form.Control data-testid="inputEntrepriseFormulaireEntreprise" type='text' required
                                  placeholder="Entreprise"
                                  value={entreprise} minLength={2}
                                  onChange={(e) => setEntreprise(e.target.value)}/>
                    <Form.Control.Feedback data-testid="errorEntrepriseFormulaireEntreprise" type="invalid">Minimum de
                        deux charactères</Form.Control.Feedback>
                </FormGroup>

                <FormGroup className="mb-3">
                    <Form.Label data-testid="labelCourrielFormulaireEntreprise">Courriel</Form.Label>
                    <Form.Control data-testid="inputCourrielFormulaireEntreprise" type='email' placeholder='Courriel'
                                  value={courriel}
                                  onChange={(e) => setCourriel(e.target.value)}
                                  required/>
                    <Form.Control.Feedback data-testid="errorCourrielFormulaireEntreprise" type="invalid">Courriel
                        invalide</Form.Control.Feedback>
                </FormGroup>

                <Row>
                    <FormGroup className="mb-3">
                        <Form.Label data-testid="labelMotDePasseFormulaireEntreprise">Mot de passe</Form.Label>
                        <Form.Control data-testid="inputMotDePasseFormulaireEntreprise" type='password' required
                                      placeholder='Mot de passe'
                                      value={motDePasse} minLength={8}
                                      onChange={(e) => setMotDePasse(e.target.value)}/>
                        <Form.Control.Feedback data-testid="errorMotDePasseFormulaireEntreprise" type="invalid">Minimum
                            de huit charactères</Form.Control.Feedback>
                    </FormGroup>
                </Row>

                <Row>
                    <FormGroup className="mb-3">
                        <Form.Label data-testid="labelVerifierMotDePasseFormulaireEntreprise">Vérifier votre mot de
                            passe</Form.Label>
                        <Form.Control data-testid="inputVerifierMotDePasseFormulaireEntreprise" type='password'
                                      placeholder='Confirmation mot de passe'
                                      value={validationMotDePasse}
                                      onChange={(e) => setValidationMotDePasse(e.target.value)}/>
                    </FormGroup>
                </Row>

                <FormGroup className="">
                    <Form.Label data-testid="labelDepartementFormulaireEntreprise" className="mb-3">
                        Département
                        <Form.Select data-testid="selelctDepartementFormulaireEntreprise" required
                                     value={departement} onChange={(e) => setDepartement(e.target.value)}>
                            <option data-testid="selelct-option-FormulaireInscriptionEntreprise" hidden value=""
                                    disabled>Choix d'un département
                            </option>
                            <option data-testid="selelct-option-FormulaireInscriptionEntreprise"
                                    value="Techniques de linformatique">Techniques de l'informatique
                            </option>
                            <option data-testid="selelct-option-FormulaireInscriptionEntreprise"
                                    value="Techniques de la logistique du transport">Techniques de la logistique du
                                transport
                            </option>
                        </Form.Select>
                    </Form.Label>
                    <Form.Control.Feedback type="invalid">Champ invalide</Form.Control.Feedback>

                </FormGroup>

                <Button data-testid="button_inscrire" type='submit'
                        className="mt-3 btn btn-success col-12">S'inscrire</Button>
            </Form>

        </Container>

    )
}


export default FormulaireInscriptionEntreprise

