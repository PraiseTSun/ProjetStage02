import React, {useEffect, useState} from "react";
import {Button, Col, Container, Row, Table, ToggleButton, ToggleButtonGroup} from "react-bootstrap";
import IUser from "../../models/IUser";
import {putOfferPdf, putValidatedOffersByYear} from "../../services/gestionnaireServices/GestionnaireFetchService";
import {generateAlert} from "../../services/universalServices/UniversalUtilService";
import IOffer from "../../models/IOffer";
import PdfComponent from "../../components/universalComponents/PdfComponent";
import PageHeader from "../../components/universalComponents/PageHeader";

const OfferHistoryPage = ({connectedUser}:
                              { connectedUser: IUser }): JSX.Element => {
    const nextYear: number = new Date().getFullYear() + 1
    const [offers, setOffers] = useState<any[]>([]);
    const [pdf, setpdf] = useState<Uint8Array>(new Uint8Array([]));
    const [showPDF, setShowPDF] = useState<boolean>(false);
    const [year, setYear] = useState<number>(nextYear);

    const fetchValidatedOffersByYear = React.useCallback(async (year: string): Promise<void> => {
        try {
            const response: Response = await putValidatedOffersByYear(year, connectedUser.token);

            if (response.ok) {
                const data: IOffer[] = await response.json()
                setOffers(data);
            } else {
                generateAlert()
            }
        } catch {
            generateAlert()
        }
    }, [connectedUser]);

    useEffect(() => {
        fetchValidatedOffersByYear(nextYear.toString())
    }, [nextYear, fetchValidatedOffersByYear]);

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
        <Container className="min-vh-100 pb-5">
            <PageHeader title={"Historique des offres"}/>
            <Row>
                <Col className="bg-light p-0" style={{minHeight: 400}}>
                    <Table className="text-center" hover responsive>
                        <thead className="bg-primary text-white">
                        <tr>
                            <th>Compagnie</th>
                            <th>Adresse</th>
                            <th>Position</th>
                            <th>Heures par semaine</th>
                            <th>Salaire</th>
                            <th>Date de d??but</th>
                            <th>Date de fin</th>
                            <th>Offre</th>
                        </tr>
                        </thead>
                        <tbody className="bg-light">
                        {offers.length === 0
                            ? <tr>
                                <td colSpan={10}>
                                    <p className="h1">Aucune offre</p>
                                </td>
                            </tr>
                            : offers.map((offer, index) => {
                                return (
                                    <tr key={index}>
                                        <td>{offer.nomDeCompagnie}</td>
                                        <td>{offer.adresse}</td>
                                        <td>{offer.position}</td>
                                        <td>{offer.heureParSemaine}</td>
                                        <td>{offer.salaire}$/h</td>
                                        <td>{offer.dateStageDebut}</td>
                                        <td>{offer.dateStageFin}</td>
                                        <td><Button className="btn btn-warning"
                                                    onClick={async () => await getPDF(offer.id)}>pdf</Button></td>
                                    </tr>
                                );
                            })}
                        </tbody>
                    </Table>
                </Col>
            </Row>
            <Row className="mt-3">
                <ToggleButtonGroup className="d-flex p-0" name="year" type="radio" value={year} onChange={field => {
                    setYear(field)
                    fetchValidatedOffersByYear(field)
                }}>
                    <ToggleButton className="w-100" id="1" variant="primary"
                                  value={nextYear - 3}>{nextYear - 3}</ToggleButton>
                    <ToggleButton className="w-100" id="2" variant="primary"
                                  value={nextYear - 2}>{nextYear - 2}</ToggleButton>
                    <ToggleButton className="w-100" id="3" variant="primary" data-testid="2022"
                                  value={nextYear - 1}>{nextYear - 1}</ToggleButton>
                    <ToggleButton className="w-100" id="4" variant="primary"
                                  value={nextYear}>{nextYear}</ToggleButton>
                </ToggleButtonGroup>
            </Row>
        </Container>
    );
}

export default OfferHistoryPage;

