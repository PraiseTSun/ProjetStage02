import React, {useEffect, useState} from "react";
import {Button, Col, Container, Row, Table} from "react-bootstrap";
import IUser from "../../models/IUser";
import {
    deleteRemoveOffer,
    putOfferPdf,
    putUnvalidatedOffers,
    putValidateOffer
} from "../../services/gestionnaireServices/GestionnaireFetchService";
import {generateAlert} from "../../services/universalServices/UniversalUtilService";
import PageHeader from "../../components/universalComponents/PageHeader";
import PdfComponent from "../../components/universalComponents/PdfComponent";

const ValiderNouvelleOffreStagePage = ({connectedUser}:
                                           { connectedUser: IUser }): JSX.Element => {
    const [offers, setOffers] = useState<any[]>([]);
    const [pdf, setpdf] = useState<Uint8Array>(new Uint8Array([]));
    const [showPDF, setShowPDF] = useState<boolean>(false);


    useEffect(() => {
        const fetchOffresAttendreValide = async () => {
            try {
                const response = await putUnvalidatedOffers(connectedUser.token)
                if (response.ok) {
                    const data = await response.json();
                    setOffers(data);
                } else {
                    generateAlert()
                }
            } catch {
                generateAlert()
            }
        }
        fetchOffresAttendreValide()
    }, [connectedUser]);

    async function valideOffre(offerId: number, valid: boolean): Promise<void> {
        try {
            const response: Response = valid
                ? await putValidateOffer(offerId.toString(), connectedUser.token)
                : await deleteRemoveOffer(offerId.toString(), connectedUser.token)

            if (response.ok) {
                setOffers(offers.filter(offer => offer.id !== offerId));
            } else {
                generateAlert()
            }
        } catch (exception) {
            generateAlert()
        }
    }

    async function getPDF(offerId: number): Promise<void> {
        try {
            const response = await putOfferPdf(offerId.toString(), connectedUser.token)
            if (response.ok) {
                const data = await response.json();
                setpdf(new Uint8Array(JSON.parse(data.pdf)))
                setShowPDF(true);
            } else {
                generateAlert()
            }
        } catch (exception) {
            generateAlert()
        }
    }

    if (showPDF) {
        return (
            <PdfComponent pdf={pdf} setShowPdf={setShowPDF}/>
        );
    }

    return (
        <Container className="min-vh-100">
            <PageHeader title={"Offres non validées"}/>
            <Row>
                <Col className="bg-light p-0 mb-5" style={{minHeight: 400}}>
                    <Table className="text-center" hover responsive>
                        <thead className="bg-primary text-white">
                        <tr>
                            <th>Compagnie</th>
                            <th>Adresse</th>
                            <th>Départment</th>
                            <th>Position</th>
                            <th>Heures Par Semaine</th>
                            <th>Salaire</th>
                            <th>Date de début</th>
                            <th>Date de fin</th>
                            <th>Pdf</th>
                            <th>Valide</th>
                            <th>Non Valide</th>
                        </tr>
                        </thead>
                        <tbody className="bg-light">
                        {offers.length === 0
                            ? <tr>
                                <td colSpan={11}>
                                    <p className="h1">Aucune offre</p>
                                </td>
                            </tr>
                            : offers.map((offer, index) => {
                                return (
                                    <tr key={index}>
                                        <td>{offer.nomDeCompagnie}</td>
                                        <td>{offer.adresse}</td>
                                        <td>{offer.department}</td>
                                        <td>{offer.position}</td>
                                        <td>{offer.heureParSemaine}h</td>
                                        <td>{offer.salaire}$/h</td>
                                        <td>{offer.dateStageDebut}</td>
                                        <td>{offer.dateStageFin}</td>
                                        <td><Button className="btn btn-warning"
                                                    onClick={async () => await getPDF(offer.id)}>pdf</Button></td>
                                        <td>
                                            <Button className="btn btn-success mx-5"
                                                    onClick={async () => await valideOffre(offer.id, true)}>O</Button>
                                        </td>
                                        <td>
                                            <Button className="btn btn-danger"
                                                    onClick={async () => await valideOffre(offer.id, false)}>X</Button>
                                        </td>
                                    </tr>
                                );
                            })}
                        </tbody>
                    </Table>
                </Col>
            </Row>
        </Container>
    );
}

export default ValiderNouvelleOffreStagePage;

