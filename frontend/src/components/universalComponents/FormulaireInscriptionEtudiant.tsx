import React, {useState} from "react";
import {Button, Container, Form, FormGroup, Row} from "react-bootstrap";
import {BeatLoader} from "react-spinners";
import {postUserType} from "../../services/universalServices/UniversalFetchService";

const FormulaireInscriptionEtudiant = ({onInscrire}: { onInscrire: Function }) => {
    const [waiting, setWaiting] = useState(false);
    const [validated, setValidated] = useState(false);
    const [nom, setNom] = useState("")
    const [prenom, setPrenom] = useState("")
    const [departement, setDepartement] = useState('')
    const [courriel, setCourriel] = useState('')
    const [motDePasse, setMotDePasse] = useState('')
    const [passwordsMatch, setPasswordsMatch] = useState(true)
    const [emailValid, setEmailValid] = useState(true)
    const [validationMotDePasse, setValidationMotDePasse] = useState('')
    const [accountCreationError, setAccountCreationError] = useState<string>("")

    const validEmail = new RegExp(
        '^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$'
    );

    const onSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        const form: any = e.currentTarget;
        e.preventDefault();

        setValidated(true)
        setPasswordsMatch(true)
        setEmailValid(true)
        setAccountCreationError("")
        if (form.checkValidity()) {

            if (motDePasse !== validationMotDePasse) {
                setPasswordsMatch(false)
                return
            }

            if (!validEmail.test(courriel)) {
                setEmailValid(false)
                return;
            }

            setWaiting(true)
            const res = await postUserType('Student', {
                lastName: nom, firstName: prenom, department: departement,
                email: courriel, password: motDePasse
            });

            if (!res.ok) {
                const data = await res.json();
                setAccountCreationError(data.error)
            } else {
                onInscrire(true);
            }
            setWaiting(false)
        }
    }

    return (
        <Container>
            <Form onSubmit={onSubmit} validated={validated} noValidate>
                <Row>
                    <FormGroup className="mb-3 col-6">
                        <Form.Label data-testid="labelNomFormulaireEtudiant">Nom</Form.Label>
                        <Form.Control data-testid="intputNomFormulaireEtudiant" type='text' required placeholder='Nom'
                                      value={nom} minLength={2}
                                      onChange={(e) => setNom(e.target.value)}/>
                        <Form.Control.Feedback data-testid="errorNomFormulaireEtudiant" type="invalid">Minimum de deux
                            charactères</Form.Control.Feedback>
                    </FormGroup>

                    <FormGroup className="mb-3 col-6">
                        <Form.Label data-testid="labelPrenomFormulaireEtudiant">Prénom</Form.Label>
                        <Form.Control data-testid="inputPrenomFormulaireEtudiant" type='text' required
                                      placeholder='Prénom'
                                      value={prenom} minLength={2}
                                      onChange={(e) => setPrenom(e.target.value)}/>
                        <Form.Control.Feedback data-testid="errorPrenomFormulaireEtudiant" type="invalid">Minimum de
                            deux charactères</Form.Control.Feedback>
                    </FormGroup>
                </Row>
                <FormGroup className="mb-3">
                    <Form.Label data-testid="labelCourrielFormulaireEtudiant">Courriel</Form.Label>
                    <Form.Control data-testid="inputCourrielFormulaireEtudiant" type='email' placeholder='Courriel'
                                  value={courriel}
                                  onChange={(e) => setCourriel(e.target.value)}
                                  required/>
                    <Form.Control.Feedback data-testid="errorCourrielFormulaireEtudiant" type="invalid">Courriel
                        invalide</Form.Control.Feedback>
                </FormGroup>

                <Row>
                    <FormGroup className="mb-3">
                        <Form.Label data-testid="labelMotDePasseFormulaireEtudiant">Mot de passe</Form.Label>
                        <Form.Control data-testid="inputMotDePasseFormulaireEtudiant" type='password' required
                                      placeholder='Mot de passe'
                                      value={motDePasse} minLength={8}
                                      onChange={(e) => setMotDePasse(e.target.value)}/>
                        <Form.Control.Feedback data-testid="errorMotDePasseFormulaireEtudiant" type="invalid">Minimum de
                            huit charactères</Form.Control.Feedback>
                    </FormGroup>
                </Row>
                <Row>

                    <FormGroup className="mb-3">
                        <Form.Label data-testid="labelVerifierMotDePasseFormulaireEtudiant">Vérifier votre mot de
                            passe</Form.Label>
                        <Form.Control data-testid="inputVerifierMotDePasseFormulaireEtudiant" type='password'
                                      minLength={8} placeholder='Confirmation mot de passe'
                                      value={validationMotDePasse}
                                      onChange={(e) => setValidationMotDePasse(e.target.value)}/>
                    </FormGroup>
                </Row>

                <FormGroup className="">
                    <Form.Label data-testid="labelDePartementFormulaireEtudiant" className="mb-3">
                        Département
                        <Form.Select required
                                     value={departement} onChange={(e) => setDepartement(e.target.value)}>
                            <option data-testid="select-option-DePartementFormulaireEtudiant" hidden value=""
                                    disabled>Choix d'un département
                            </option>
                            <option data-testid="select-option-DePartementFormulaireEtudiant"
                                    value="Techniques de linformatique">Techniques de l'informatique
                            </option>
                            <option data-testid="select-option-DePartementFormulaireEtudiant"
                                    value="Techniques de la logistique du transport">Techniques de la logistique du
                                transport
                            </option>
                        </Form.Select>
                    </Form.Label>
                    <Form.Control.Feedback type="invalid">Champ invalide</Form.Control.Feedback>

                </FormGroup>
                {accountCreationError !== "" &&
                    <p className="text-danger fw-bold text-center h3">{accountCreationError}</p>}
                {!emailValid &&
                    <p className="text-danger fw-bold text-center h3">Le courriel est invalide</p>}
                {!passwordsMatch &&
                    <p className="text-danger fw-bold text-center h3">Les mots de passe ne sont pas pareils</p>}
                {
                    waiting
                        ? <BeatLoader color="#0275d8" className="text-center" size={25}/>
                        : <Row className="mt-3">
                            <Button type="submit" className="btn btn-success mx-auto">Inscrire</Button>
                        </Row>
                }
            </Form>
        </Container>

    )
}


export default FormulaireInscriptionEtudiant

