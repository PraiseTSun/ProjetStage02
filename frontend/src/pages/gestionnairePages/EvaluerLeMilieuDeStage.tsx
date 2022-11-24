import React, {useEffect, useState} from "react";
import IUser from "../../models/IUser";
import {Button, Col, Container, Row, Table} from "react-bootstrap";
import PageHeader from "../../components/universalComponents/PageHeader";
import {generateAlert} from "../../services/universalServices/UniversalUtilService";
import InfoDuContrat from "../../models/InfoDuContrat";
import {
    putGetContrats,
    putInfoContratPourEvaluateStage
} from "../../services/gestionnaireServices/GestionnaireFetchService";
import IContrat from "../../models/IContrat";
import EvaluationLeMilleuDeStageForm from "../../components/gestionnaireComponents/EvaluationLeMilleuDeStageForm";

const EvaluerLeMilieuDeStage = ({user}: { user: IUser }): JSX.Element => {
    const [contrats, setContrats] = useState<IContrat[]>([]);
    const [afficheFormulaire, setAfficheFormuaire] = useState<boolean>(false)
    const [infosContrat, setInfosContrat] = useState<InfoDuContrat>({
        adresse: "",
        dateStageDebut: "",
        dateStageFin: "",
        departement: "",
        emailCompagnie: "",
        emailEtudiant: "",
        heuresParSemaine: 0,
        nomCompagnie: "",
        nomContact: "",
        nomEtudiant: "",
        poste: "",
        prenomContact: "",
        prenomEtudiant: "",
        salaire: 0,
        session: ""
    });
    const [contratId, setContratId] = useState<number>(0)

    useEffect(() => {
        const fetchContracts = async (): Promise<void> => {
            try {
                const response: Response = await putGetContrats(user.token);
                if (response.ok) {
                    const data = await response.json()
                    setContrats(data.contracts)
                } else {
                    generateAlert()
                }
            } catch {
                generateAlert()
            }
        }
        fetchContracts()
    }, [user])

    const fetchContractParId = async (contractId: number): Promise<void> => {
        try {
            const response: Response = await putInfoContratPourEvaluateStage(contractId, user.token);
            if (response.ok) {
                const data = await response.json()
                setInfosContrat(data)
            } else {
                generateAlert()
            }
        } catch {
            generateAlert()
        }
    }

    const showFormulaires = (id: number) => {
        setContratId(id)
        fetchContractParId(id)
        setAfficheFormuaire(true)
    }
    const hideFormulaires = () => {
        setAfficheFormuaire(false)
    }

    if (afficheFormulaire) {
        return (
            <EvaluationLeMilleuDeStageForm user={user} hideFormulaires={hideFormulaires}
                                           setAfficheFormuaire={setAfficheFormuaire}
                                           infosContrat={infosContrat}
                                           contratId={contratId}></EvaluationLeMilleuDeStageForm>
        )
    }

    return (
        <Container className="min-vh-100">
            <PageHeader title={"Évaluation des contrats"}></PageHeader>
            <Row>
                <Col className="bg-light p-0 mb-5" style={{minHeight: 400}}>
                    <Table className="text-center" hover responsive>
                        <thead className="bg-primary text-white">
                        <tr>
                            <th>Compagnie</th>
                            <th>Position</th>
                            <th>Étudiant</th>
                            <th>Description</th>
                            <th>Évalation</th>
                        </tr>
                        </thead>
                        <tbody className="bg-light">
                        {contrats.length === 0
                            ? <tr>
                                <td colSpan={5}>
                                    <p className="h1">Aucune entente a évaluer</p>
                                </td>
                            </tr>
                            : contrats.map((contrat, index) => {
                                return (
                                    <tr key={index}>
                                        <td>{contrat.companyName}</td>
                                        <td>{contrat.position}</td>
                                        <td>{contrat.studentFullName}</td>
                                        <td>{contrat.description}</td>
                                        <td><Button className="btn btn-warning" onClick={() => {
                                            showFormulaires(Number(contrat.contractId))
                                        }}>Évaluer</Button></td>
                                    </tr>
                                );
                            })}
                        </tbody>
                    </Table>
                </Col>
            </Row>
        </Container>
    )
}
export default EvaluerLeMilieuDeStage