import PageHeader from "../../components/universalComponents/PageHeader";
import React, {useState} from "react";
import {postReport} from "../../services/universalServices/UniversalFetchService";
import LocationChangeConfirmationPopup from "../../components/universalComponents/LocationChangeConfirmationPopup";
import {BeatLoader} from "react-spinners";
import {Button, Col, Container, Form, Row} from "react-bootstrap";

const ReportProblemPage = () => {
    const [formFields, setFormFields] = useState<{ problemCategory: string, problemDetails: string, email: string }>({
        problemCategory: "",
        problemDetails: "",
        email: "",
    })
    const [loading, setLoading] = useState<boolean>(false)
    const [problemReported, setProblemReported] = useState<boolean>(false)
    const [validated, setValidated] = useState<boolean>(false)
    const [error, setError] = useState<string>("")
    const changeProblemCategory = (newCategory: string) => {
        setFormFields({...formFields, problemCategory: newCategory})
    }
    const changeProblemDetails = (newDetails: string) => {
        setFormFields({...formFields, problemDetails: newDetails})
    }
    const changeEmail = (newEmail: string) => {
        setFormFields({...formFields, email: newEmail})
    }
    const validateEmail = (email: string): boolean => {
        const regex = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/
        return regex.test(email)
    }
    const onSubmit = async (event: React.SyntheticEvent): Promise<void> => {
        event.preventDefault();
        if (!validateEmail(formFields.email)) {
            setError("L'adresse email n'est pas valide.")
            setValidated(true)
            return
        }
        const form: any = event.currentTarget;

        if (form.checkValidity()) {
            setLoading(true)
            await postReport(formFields);
            setProblemReported(true)
            setLoading(false)
        }
        setValidated(true)

    }
    const pageBody = () => {
        if (problemReported) {
            return <LocationChangeConfirmationPopup message={"Rapport envoyé. Merci!"} locationOnConfirm={"/"}/>
        }
        return form()
    }
    const form = () => {
        return <>
            <Row className="card">
                <h3 className="card-header text-center">Rapport d'erreur</h3>
                {error.length > 0 ?
                    <Row>
                        <Col>
                            <h5 className="text-danger text-center">{error}</h5>
                        </Col>
                    </Row> : null
                }
                <Form className="card-body p-3" onSubmit={onSubmit} validated={validated} noValidate>
                    <Form.Group>
                        <Form.Label className="fw-bold mt-2 h5 mt-3">Type de problème</Form.Label>
                        <Form.Select data-testid="departmentFormulaireSoumission" required
                                     value={formFields.problemCategory}
                                     onChange={(e) => changeProblemCategory(e.target.value)}>
                            <option hidden value="" disabled>Type de problème</option>
                            <option value="Autre" selected>
                                Autre
                            </option>
                            <option value="Connexion">
                                Connexion
                            </option>
                            <option value="Inscription">
                                Inscription
                            </option>
                            <option value="Offres de stage">
                                Offres de stage
                            </option>
                            <option value="Contrats">
                                Contrats
                            </option>
                        </Form.Select>
                        <Form.Control.Feedback type="invalid">Champ requis</Form.Control.Feedback>
                    </Form.Group>
                    <Form.Group>
                        <Form.Label className="fw-bold h5 mt-3">Addresse email</Form.Label>
                        <Form.Control data-testid="nomCompanyFormulaireSoumission" type="text"
                                      required
                                      value={formFields.email}
                                      onChange={(e) => changeEmail(e.target.value)}/>
                        <Form.Control.Feedback type="invalid">Champ requis</Form.Control.Feedback>
                    </Form.Group>
                    <Form.Group>
                        <Form.Label className="fw-bold h5 mt-3">Description du problème</Form.Label>
                        <Form.Control data-testid="nomCompanyFormulaireSoumission" type="text" as="textarea" rows={5}
                                      required
                                      value={formFields.problemDetails}
                                      onChange={(e) => changeProblemDetails(e.target.value)}/>
                        <Form.Control.Feedback type="invalid">Champ requis</Form.Control.Feedback>
                    </Form.Group>
                    <Row className="mt-3">
                        <Col className="text-center">
                            {loading
                                ? <BeatLoader color="#0275d8 " size={25}/>
                                : <Button data-testid="envoyerFormulaireSoumission" type="submit"
                                          className="btn btn-success mx-auto w-75">Envoyer</Button>
                            }
                        </Col>
                    </Row>
                </Form>
            </Row>
        </>
    }
    return (
        <Container className="min-vh-100 pb-5">
            <PageHeader title="Signaler un problème"/>
            {pageBody()}
        </Container>)
}
export default ReportProblemPage