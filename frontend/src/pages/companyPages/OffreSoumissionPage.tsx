import React, {useState} from "react";
import {Button, Col, Container, Form, Row} from "react-bootstrap";
import {BeatLoader} from "react-spinners";
import IUser from "../../models/IUser";
import {postCreateOffre} from "../../services/companyServices/CompanyFetchService";
import {generateAlert} from "../../services/universalServices/UniversalUtilService";
import PageHeader from "../../components/universalComponents/PageHeader";
import LocationChangeConfirmationPopup from "../../components/universalComponents/LocationChangeConfirmationPopup";

declare type FormControlElement = HTMLInputElement | HTMLTextAreaElement;

const OffreSoumissionPage = ({user}: { user: IUser }): JSX.Element => {
    const CURRENT_DATE = new Date().toISOString().split("T")[0];
    const [waiting, setWaiting] = useState(false);
    const [validated, setValidated] = useState(false);
    const [companyName, setCompanyName] = useState("")
    const [department, setDepartment] = useState("")
    const [dateStageDebut, setDateStageDebut] = useState("")
    const [dateStageFin, setDateStageFin] = useState("")
    const [poste, setPoste] = useState("")
    const [hoursPerWeek, setHoursPerWeek] = useState(40)
    const [salary, setSalary] = useState(20)
    const [address, setAddress] = useState("")
    const [pdf, setPdf] = useState<number[]>([])
    const [validDates, setValidDates] = useState<boolean>(true)
    const [formSent, setFormSent] = useState<boolean>(false)


    const onSubmit = async (event: React.SyntheticEvent): Promise<void> => {
        const form: any = event.currentTarget;
        event.preventDefault();

        if (dateStageFin < dateStageDebut) {
            setValidDates(false)
            return;
        }


        if (form.checkValidity()) {
            setWaiting(true);

            const response = await postCreateOffre({
                id: "",
                adresse: address,
                salaire: salary.toString(),
                position: poste,
                pdf: pdf,
                department: department,
                heureParSemaine: hoursPerWeek.toString(),
                nomDeCompagnie: companyName,
                token: user.token,
                companyId: user.id,
                dateStageDebut: dateStageDebut,
                dateStageFin: dateStageFin,
            })

            if (response.ok) {
                setFormSent(true)
            } else {
                generateAlert()
            }

            setWaiting(false);
        }
        setValidated(true);
    }

    const numbersRegEx = new RegExp(
        '^[0-9]{0,3}$'
    );
    const setHoursPerWeekFromField = (event: React.ChangeEvent<FormControlElement>) => {
        if (numbersRegEx.test(event.target.value)) {
            setHoursPerWeek(Number.parseInt(event.target.value))
        } else {
            setHoursPerWeek(hoursPerWeek)
        }
    }
    const setSalaryFromField = (event: React.ChangeEvent<FormControlElement>) => {
        if (numbersRegEx.test(event.target.value)) {
            setSalary(Number.parseInt(event.target.value))
        } else {
            setSalary(hoursPerWeek)
        }
    }

    const intToByteArray = (array: Uint8Array): number[] => {
        let bytes: number[] = []
        array.forEach(value => {
            bytes.push(value)
        })
        return bytes;
    };

    const uploadFile = async (file: File) => {
        const fileText = await file.arrayBuffer()
        const view = new Uint8Array(fileText)
        const array = intToByteArray(view)
        setPdf(array)
    }

    if (formSent) {
        return (
            <LocationChangeConfirmationPopup message={"Formulaire envoyé"} locationOnConfirm={"/"}/>
        );
    }

    return (
        <Container className="min-vh-100">
            <PageHeader title={"Soumettre une offre"}/>
            <Row className="card">
                <h3 className="card-header text-center">Formulaire de soumission de stage</h3>
                <Form className="card-body p-3" onSubmit={onSubmit} validated={validated} noValidate>
                    <Form.Group>
                        <Form.Label className="fw-bold h5">Nom de la compagnie</Form.Label>
                        <Form.Control data-testid="nomCompanyFormulaireSoumission" type="text" required
                                      value={companyName}
                                      onChange={field => setCompanyName(field.target.value)}></Form.Control>
                        <Form.Control.Feedback type="invalid">Champ requis</Form.Control.Feedback>
                    </Form.Group>
                    <Form.Group>
                        <Form.Label className="fw-bold mt-2 h5">Département</Form.Label>
                        <Form.Select data-testid="departmentFormulaireSoumission" required
                                     value={department} onChange={(e) => setDepartment(e.target.value)}>
                            <option hidden value="" disabled>Choix d'un département</option>
                            <option value="Techniques de linformatique">
                                Technique de l'informatique
                            </option>
                            <option value="Techniques de la logistique du transport">
                                Technique de la logistique du transport
                            </option>
                        </Form.Select>
                        <Form.Control.Feedback type="invalid">Champ requis</Form.Control.Feedback>
                    </Form.Group>
                    <Form.Group>
                        <Form.Label className="fw-bold h5">Poste</Form.Label>
                        <Form.Control data-testid="posteFormulaireSoumission" type="text" required value={poste}
                                      onChange={field => setPoste(field.target.value)}></Form.Control>
                        <Form.Control.Feedback type="invalid">Champ requis</Form.Control.Feedback>
                    </Form.Group>
                    <Form.Group>
                        <Form.Label className="fw-bold h5">Heures par semaine</Form.Label>
                        <Form.Control data-testid="hoursFormulaireSoumission" id={"number"} type="number" min="1"
                                      max="40" required value={hoursPerWeek}
                                      onChange={field => setHoursPerWeekFromField(field)}></Form.Control>
                        <Form.Control.Feedback type="invalid">Nombre d'heures entre 0 et 40</Form.Control.Feedback>
                    </Form.Group>
                    <Form.Group>
                        <Form.Label className="fw-bold h5">Salaire horaire</Form.Label>
                        <Form.Control data-testid="salaryFormulaireSoumission" id={"number"} type="number" min="15"
                                      max="1000" required value={salary}
                                      onChange={field => setSalaryFromField(field)}></Form.Control>
                        <Form.Control.Feedback type="invalid">Salaire doit etre plus haut que
                            15</Form.Control.Feedback>
                    </Form.Group>
                    <Form.Group>
                        <Form.Label className="fw-bold h5">Adresse</Form.Label>
                        <Form.Control data-testid="addressFormulaireSoumission" type="text" required value={address}
                                      onChange={field => setAddress(field.target.value)}></Form.Control>
                        <Form.Control.Feedback type="invalid">Champ requis</Form.Control.Feedback>
                    </Form.Group>
                    <Row>
                        <Form.Group>
                            <Form.Label className="fw-bold h5">Date de début</Form.Label>
                            <Form.Control type="date" required min={CURRENT_DATE}
                                          onChange={event => setDateStageDebut(event.target.value)}/>
                        </Form.Group>
                        <Form.Group>
                            <Form.Label className="fw-bold h5">Date de fin</Form.Label>
                            <Form.Control type="date" required min={CURRENT_DATE}
                                          onChange={event => setDateStageFin(event.target.value)}/>
                        </Form.Group>
                    </Row>
                    <Form.Group>
                        <Form.Label className="fw-bold h5">Document PDF</Form.Label>
                        <input data-testid="pdfFormulaireSoumission" id={"file"} className="form-control"
                               accept=".pdf"
                               required type="file" onChange={(e) => {
                            uploadFile(e.target.files![0]);
                        }}/>
                        <Form.Control.Feedback type="invalid">Champ requis</Form.Control.Feedback>
                    </Form.Group>
                    <Row className="mt-3">
                        <Col className="text-center">
                            {!validDates &&
                                <h3 className="text-danger">La date de début doit être avant celle de fin</h3>}
                            {waiting
                                ? <BeatLoader color="#0275d8 " size={25}/>
                                : <Button data-testid="envoyerFormulaireSoumission" type="submit"
                                          className="btn btn-success mx-auto w-75">Envoyer</Button>
                            }
                        </Col>
                    </Row>
                </Form>
            </Row>
        </Container>
    );
}
export default OffreSoumissionPage


