import React, {useCallback, useEffect, useState} from "react"
import IUser from "../../models/IUser";
import {Button, Col, Container, Row, Table} from "react-bootstrap";
import PageHeader from "../../components/universalComponents/PageHeader";
import IOffer from "../../models/IOffer";
import {putCompanyOffers} from "../../services/companyServices/CompanyFetchService";
import OfferStudentApplicationsList from "../../components/companyComponents/OfferStudentApplicationsList";
import {universalFetch} from "../../services/universalServices/UniversalFetchService";

const CompanyOffersPage = ({connectedUser}: { connectedUser: IUser }): JSX.Element => {
    const [offers, setOffers] = useState<IOffer[]>([]);
    const [currentlySelectedOffer, setCurrentlySelectedOffer] = useState<string | null>(null);

    const fetchOffers = useCallback(async (): Promise<void> => {
        universalFetch(async () => await putCompanyOffers(connectedUser.id, connectedUser.token),
            async (response: Response) => {
                const data: IOffer[] = await response.json();
                setOffers(data);
            });
    }, [connectedUser]);

    useEffect(() => {
        fetchOffers()
    }, [connectedUser, fetchOffers]);

    return (
        <Container className="min-vh-100 p-0">
            <PageHeader title={"Mes offres"}/>
            <Row>
                <Col className="bg-light p-0 mx-2" style={{minHeight: 300}}>
                    <Table className="text-center" hover>
                        <thead className="bg-primary text-white">
                        <tr>
                            <th>Compagnie</th>
                            <th>Adresse</th>
                            <th>Position</th>
                            <th>Heures par semaine</th>
                            <th>Salaire</th>
                            <th>Date de début</th>
                            <th>Date de fin</th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        {offers.map((offer, index) => {
                            return (
                                <tr key={index}>
                                    <td>{offer.nomDeCompagnie}</td>
                                    <td>{offer.adresse}</td>
                                    <td>{offer.position}</td>
                                    <td>{offer.heureParSemaine}</td>
                                    <td>{offer.salaire}$/h</td>
                                    <td>{offer.dateStageDebut}</td>
                                    <td>{offer.dateStageFin}</td>
                                    <th><Button disabled={currentlySelectedOffer === offer.id} variant="warning"
                                                onClick={async () => {
                                                    setCurrentlySelectedOffer(offer.id)
                                                }}>Applicants</Button></th>
                                </tr>
                            );
                        })}
                        </tbody>
                    </Table>
                </Col>
                {currentlySelectedOffer !== null &&
                    <Col className="bg-light p-0 mx-2" style={{minHeight: 300}}>
                        <OfferStudentApplicationsList connectedUser={connectedUser} offerId={currentlySelectedOffer!}/>
                    </Col>
                }
            </Row>
        </Container>
    );
}

export default CompanyOffersPage;