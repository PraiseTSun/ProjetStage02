import React, {useEffect, useState} from "react";
import {Button, Col, Container, Row, Table, ToggleButton, ToggleButtonGroup} from "react-bootstrap";
import {Link} from "react-router-dom";
import IUser from "../../models/IUser";
import {Viewer} from '@react-pdf-viewer/core';
import {putOfferPdf, putValidatedOffersByYear} from "../../services/gestionnaireServices/GestionnaireFetchService";
import {generateAlert} from "../../services/universalServices/UniversalUtilService";
import IOffer from "../../models/IOffer";

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
            <Container>
                <Container className="min-vh-100 bg-white p-0">
                    <div className="bg-dark p-2">
                        <Button className="Btn btn-primary" onClick={() => setShowPDF(false)}>
                            Fermer
                        </Button>
                    </div>
                    <div>
                        <Viewer fileUrl={pdf}/>
                    </div>
                </Container>
            </Container>
        );
    }

    return (
        <Container className="min-vh-100">
            <Row>
                <Col sm={2}>
                    <Link to="/" className="btn btn-primary my-3">Home</Link>
                </Col>
                <Col sm={8} className="text-center pt-2">
                    <h1 className="fw-bold text-white display-3 pb-2">Historique des offres</h1>
                </Col>
                <Col sm={2}></Col>
            </Row>
            <Row>
                <Col className="bg-light p-0" style={{height: 400}}>
                    <Table className="text-center" hover>
                        <thead className="bg-primary">
                        <tr>
                            <th>Nom De Compagnie / Adresse</th>
                            <th>Départment / Position</th>
                            <th>Heure Par Semaine / Salaire</th>
                            <th>Pdf</th>
                        </tr>
                        </thead>
                        <tbody className="bg-light">
                        {offers.map((offer, index) => {
                            return (
                                <tr key={index}>
                                    <td>{offer.nomDeCompagnie} <br/> {offer.adresse}</td>
                                    <td>{offer.department} <br/> {offer.position}</td>
                                    <td>{offer.heureParSemaine} <br/> {offer.salaire}$/h</td>
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

