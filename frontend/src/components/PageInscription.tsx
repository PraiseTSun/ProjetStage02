import {useState} from "react";
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import {Container, FormGroup, Row} from "react-bootstrap";
const PageInscription = ({onInscrire} : { onInscrire : Function}  ) => {

    const [typePersonne, setTypePersonne] = useState('')
    const [departement, setDepartement] = useState('')
    const [courriel, setCourriel] = useState('')
    const [motDePasse, setMotDePasse] = useState('')
    const [validationMotDePasse, setValidationMotDePasse] = useState('')

    const [erreurPourType, setErreurPourType] = useState(false)
    const [erreurPourDepartement, setErreurPourDepartement] = useState(false)
    const [erreurPourCourriel, setErreurPourCourriel] = useState(false)
    const [erreurPourMotDePasse, setErreurPourMotDePasse] = useState(false);
    const [erreurPourMotDePasseVerifier, setErreurPourMotDePasseVerifier] = useState(false);

    const validEmail = new RegExp(
        '^[a-zA-Z0-9._:$!%-]+@[a-zA-Z0-9.-]+.[a-zA-Z]$'
    );

    const onSubmit = (e:React.FormEvent<HTMLFormElement>) => {

            e.preventDefault();


        if (typePersonne == '') {
           setErreurPourType(true)
            return
        }

        if (departement == '') {
            setErreurPourDepartement(true)
            return
        }

        if (motDePasse !== validationMotDePasse) {
            setErreurPourMotDePasseVerifier(true)
            return
        }
        if(motDePasse.length < 8){
            setErreurPourMotDePasse(true)
            return
        }
        if(!validEmail.test(courriel)){
            setErreurPourCourriel(true)
            return;
        }

        onInscrire({typePersonne : typePersonne, departement : departement, courriel : courriel, motDePasse : motDePasse})
        setTypePersonne('')
        setDepartement('')
        setCourriel('')
        setMotDePasse('')
        setValidationMotDePasse('')
        setErreurPourType(false)
        setErreurPourDepartement(false)
        setErreurPourCourriel(false)
        setErreurPourMotDePasse(false)
        setErreurPourMotDePasseVerifier(false)
    }
    return(
        <Container >
            <Row style={{ width : "500px", height : "300px", margin : "auto", marginTop : "40px", borderRadius:"15px", padding : "20px"}}>
                <Form onSubmit={onSubmit}>
                    <FormGroup className="mb-3" controlId="formBasicSelectType" style={{margin : "20px"}}>
                        <Form.Label htmlFor="formBasicSelectType">
                            Type de personne
                            <Form.Select  id="formBasicSelectType"  style={{marginLeft : "20px"}} defaultValue={'Choix un type'} onChange={(e) => setTypePersonne(e.target.value)}>
                                <option value="Choix un type" disabled>Choix un type</option>
                                <option value="Etudiant">Etudiant</option>
                                <option value="Entreprise">Entreprise</option>
                            </Form.Select>
                        </Form.Label>
                        {
                            erreurPourType ?
                                <Form.Control.Feedback type="invalid" style={{color : "red"}}>
                                    Veuillez sélectionner votre type
                                </Form.Control.Feedback>
                                :
                                <Form.Control.Feedback type="invalid">
                                </Form.Control.Feedback>
                        }
                    </FormGroup>
                    <FormGroup className="mb-3" controlId="formBasicSelectDepartement" style={{margin : "20px"}}>
                        <Form.Label className="mb-3" htmlFor="formBasicSelectDepartement">
                            Département
                            <Form.Select  id="formBasicSelectDepartement" style={{marginLeft : "20px"}} defaultValue={'Choix un type'} onChange={(e) => setDepartement(e.target.value)}>
                                <option value="Choix un type" disabled>Choix un département</option>
                                <option value="Techniques de l’informatique">Techniques de l’informatique</option>
                                <option value="Techniques de la logistique du transport">Techniques de la logistique du transport</option>
                            </Form.Select>
                        </Form.Label>
                        {
                            erreurPourDepartement ?
                                <Form.Control.Feedback type="invalid" style={{color : "red"}}>
                                    Veuillez sélectionner votre departement
                                </Form.Control.Feedback>
                                :
                                <Form.Control.Feedback type="invalid">
                                </Form.Control.Feedback>
                        }
                    </FormGroup>

                    <FormGroup className="mb-3" controlId="formBasicEmail" style={{margin : "20px"}}>
                        <Form.Label>Courriel</Form.Label>
                        <Form.Control type='email' placeholder='Courriel'
                                      value={courriel}
                                      style={{marginLeft : "20px", width : "200px"}}
                                      onChange={(e) => setCourriel(e.target.value)}
                                      required/>
                        {
                            erreurPourCourriel ?
                                <Form.Control.Feedback type="invalid" style={{color : "red"}}>
                                   Courriel invalide
                                </Form.Control.Feedback>
                                :
                                <Form.Control.Feedback type="invalid">
                                </Form.Control.Feedback>
                        }
                    </FormGroup>

                    <FormGroup className="mb-3" controlId="formBasicPassword" style={{margin : "20px"}}>
                        <Form.Label>Mot de pass</Form.Label>
                        <Form.Control type='password' required={true} placeholder='Mot de passe'
                                      value={motDePasse}
                                      style={{marginLeft : "20px", width : "200px"}}
                                      onChange={(e) => setMotDePasse(e.target.value)}/>
                        {
                            erreurPourMotDePasse ?
                            <Form.Control.Feedback type="invalid" style={{color : "red"}}>
                                La longueur du mot de passe est supérieure à 7
                            </Form.Control.Feedback>
                            :
                            <Form.Control.Feedback type="invalid">
                            </Form.Control.Feedback>
                        }
                    </FormGroup>

                    <FormGroup className="mb-3" controlId="formBasicPasswordVerifier" style={{margin : "20px"}}>
                        <label>Vérifier votre mot de passe</label>
                        <Form.Control type='password' placeholder='Confirmation mot de passe'
                                      value={validationMotDePasse}
                                      style={{marginLeft : "20px", width : "200px"}}
                                      onChange={(e) => setValidationMotDePasse(e.target.value)}/>
                        {
                            erreurPourMotDePasseVerifier ?
                            <Form.Control.Feedback type="invalid" style={{color : "red"}}>
                                Vérifier le mot de passe et le mot de passe ne correspondent pas
                            </Form.Control.Feedback>
                            :
                            <Form.Control.Feedback type="invalid">
                            </Form.Control.Feedback>
                        }
                    </FormGroup>
                    <Button type='submit' style={{marginLeft : "100px", width : "250px",backgroundColor : "green", color : "white", borderRadius:"15px", fontSize : "30px"}}>Inscrire</Button>
                </Form>
            </Row>
        </Container>

    )
}
export default PageInscription