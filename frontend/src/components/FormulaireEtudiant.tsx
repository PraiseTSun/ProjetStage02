import React from "react";
import { Button, Container, Form, FormGroup, Row } from "react-bootstrap";
import { useState } from "react";

const FormulaireEtudiant = ({ onInscrire }: { onInscrire: Function }) => {

    const [nom, setNom] = useState("")
    const [prenom, setPrenom] = useState("")
    const [departement, setDepartement] = useState('')
    const [courriel, setCourriel] = useState('')
    const [motDePasse, setMotDePasse] = useState('')
    const [validationMotDePasse, setValidationMotDePasse] = useState('')

    const validEmail = new RegExp(
        '^[a-zA-Z0-9._:$!%-]+@[a-zA-Z0-9.-]+.[a-zA-Z]$'
    );

    const onSubmit = (e: React.FormEvent<HTMLFormElement>) => {

        e.preventDefault();

        if (motDePasse !== validationMotDePasse) {
            alert("Vérifier le mot de passe et le mot de passe ne correspondent pas")
            return
        }

        if (!validEmail.test(courriel)) {
            return;
        }

        onInscrire({
            lastName: nom, firstName: prenom, department: departement,
            email: courriel, password: motDePasse
        }, 'Student')

        setNom('')
        setPrenom('')
        setDepartement('')
        setCourriel('')
        setMotDePasse('')
        setValidationMotDePasse('')

    }
    return (
        <Container>

            <Form onSubmit={onSubmit}>
                <Row>
                    <FormGroup className="mb-3 col-6">
                        <Form.Label>Nom</Form.Label>
                        <Form.Control type='text' required placeholder='Nom'
                            value={nom} minLength={2}
                            onChange={(e) => setNom(e.target.value)} />
                    </FormGroup>

                    <FormGroup className="mb-3 col-6">
                        <Form.Label>Prénom</Form.Label>
                        <Form.Control type='text' required placeholder='Prénom'
                            value={prenom} minLength={2}
                            onChange={(e) => setPrenom(e.target.value)} />
                    </FormGroup>
                </Row>
                <FormGroup className="mb-3">
                    <Form.Label>Courriel</Form.Label>
                    <Form.Control type='email' placeholder='Courriel'
                        value={courriel}
                        onChange={(e) => setCourriel(e.target.value)}
                        required />
                </FormGroup>

                <Row>
                    <FormGroup className="mb-3 col-6">
                        <Form.Label>Mot de passe</Form.Label>
                        <Form.Control type='password' required placeholder='Mot de passe'
                            value={motDePasse} minLength={8}
                            onChange={(e) => setMotDePasse(e.target.value)} />
                    </FormGroup>

                    <FormGroup className="mb-3 col-6">
                        <Form.Label>Vérifier votre mot de passe</Form.Label>
                        <Form.Control type='password' placeholder='Confirmation mot de passe'
                            value={validationMotDePasse}
                            onChange={(e) => setValidationMotDePasse(e.target.value)} />
                    </FormGroup>
                </Row>

                <FormGroup className="">
                    <Form.Label className="mb-3">
                        Département
                        <Form.Select required
                            value={departement} onChange={(e) => setDepartement(e.target.value)}>
                            <option hidden value="" disabled>Choix d'un département</option>
                            <option value="Techniques de linformatique">Techniques de l'informatique</option>
                            <option value="Techniques de la logistique du transport">Techniques de la logistique du
                                transport
                            </option>
                        </Form.Select>
                    </Form.Label>

                </FormGroup>

                <Button type='submit' className="mt-3 btn btn-success col-12" >S'inscrire</Button>
            </Form>

        </Container>

    )
}


export default FormulaireEtudiant

