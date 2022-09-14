import PropTypes from 'prop-types'
import {Button, Container, Form, FormGroup, Row} from "react-bootstrap";
import {useState} from "react";

const FormulaireEntreprise = ({ onInscrire }:
                                { onInscrire: Function }) => {

    const [nom, setNom] = useState("")
    const [prenom, setPrenom] = useState("")
    const [entreprise, setEntreprise] = useState("")
    const [departement, setDepartement] = useState('')
    const [courriel, setCourriel] = useState('')
    const [motDePasse, setMotDePasse] = useState('')
    const [validationMotDePasse, setValidationMotDePasse] = useState('')

    const [erreurPourNom, setErreurPourNom] = useState(false)
    const [erreurPourPrenom, setErreurPourPrenom] = useState(false)
    const [erreurPourEtreprise, setErreurPourErreurPourEtreprise] = useState(false)
    const [erreurPourDepartement, setErreurPourDepartement] = useState(false)
    const [erreurPourCourriel, setErreurPourCourriel] = useState(false)
    const [erreurPourMotDePasse, setErreurPourMotDePasse] = useState(false);
    const [erreurPourMotDePasseVerifier, setErreurPourMotDePasseVerifier] = useState(false);

    const validEmail = new RegExp(
        '^[a-zA-Z0-9._:$!%-]+@[a-zA-Z0-9.-]+.[a-zA-Z]$'
    );


    const onSubmit = (e: React.FormEvent<HTMLFormElement>) => {

        e.preventDefault();

        if (departement === '' || departement === 'Choix un département') {
            alert("Dois choisir departement")
            return
        }


        if (motDePasse !== validationMotDePasse) {
            setErreurPourMotDePasseVerifier(true)
            return
        }

        if (!validEmail.test(courriel)) {
            setErreurPourCourriel(true)
            return;
        }


        onInscrire({lastName : nom, firstName : prenom, name: entreprise, departement : departement,
                email : courriel, password : motDePasse}, 'Company')

        setNom('')
        setPrenom('')
        setEntreprise('')
        setDepartement('')
        setCourriel('')
        setMotDePasse('')
        setValidationMotDePasse('')
        setErreurPourDepartement(false)
        setErreurPourCourriel(false)
        setErreurPourMotDePasse(false)
        setErreurPourMotDePasseVerifier(false)

    }
    return (
        <Container>

            <Form onSubmit={onSubmit}>
                <FormGroup className="mb-3">
                    <Form.Label>Nom</Form.Label>
                    <Form.Control type='text' required={true} placeholder='Nom'
                                  value={nom} minLength={2}
                                  onChange={(e) => setNom(e.target.value)}/>

                </FormGroup>

                <FormGroup className="mb-3">
                    <Form.Label>Prenom</Form.Label>
                    <Form.Control type='text' required={true} placeholder='Nom'
                                  value={prenom} minLength={2}
                                  onChange={(e) => setPrenom(e.target.value)}/>

                </FormGroup>

                <FormGroup className="mb-3">
                    <Form.Label>Entreprise</Form.Label>
                    <Form.Control type='text' required={true} placeholder='Nom'
                                  value={entreprise} minLength={2}
                                  onChange={(e) => setDepartement(e.target.value)}/>

                </FormGroup>

                <FormGroup className="mb-3">
                    <Form.Label>Courriel</Form.Label>
                    <Form.Control type='email' placeholder='Courriel'
                                  value={courriel}
                                  onChange={(e) => setCourriel(e.target.value)}
                                  required/>

                </FormGroup>

                <FormGroup className="mb-3">
                    <Form.Label>Mot de passe</Form.Label>
                    <Form.Control type='password' required placeholder='Mot de passe'
                                  value={motDePasse} minLength={8}
                                  onChange={(e) => setMotDePasse(e.target.value)}/>

                </FormGroup>

                <FormGroup className="mb-3">
                    <Form.Label>Vérifier votre mot de passe</Form.Label>
                    <Form.Control type='password' placeholder='Confirmation mot de passe'
                                  value={validationMotDePasse}
                                  onChange={(e) => setValidationMotDePasse(e.target.value)}/>

                </FormGroup>

                <FormGroup className="">
                    <Form.Label className="mb-3">
                        Département
                        <Form.Select required
                                     value={departement} onChange={(e) => setDepartement(e.target.value)}>
                            <option hidden value="" disabled>Choix un département</option>
                            <option value="Techniques de l’informatique">Techniques de l’informatique</option>
                            <option value="Techniques de la logistique du transport">Techniques de la logistique du
                                transport
                            </option>
                        </Form.Select>
                    </Form.Label>

                </FormGroup>

                <Button type='submit' className="mt-3" style={{
                    marginLeft: "70px",
                    width: "250px",
                    backgroundColor: "green",
                    color: "white",
                    borderRadius: "15px",
                    fontSize: "30px"
                }}>Inscrire</Button>
            </Form>

        </Container>

    )
}


export default FormulaireEntreprise

