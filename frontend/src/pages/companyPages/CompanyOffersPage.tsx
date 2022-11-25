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
        <Container className="min-vh-100">
            <PageHeader title={"Mes offres"}/>
            <Row>
                <Col className="bg-light p-0 me-lg-0 me-xl-3" style={{minHeight: 400}}>
                    <Table className="text-center" hover responsive>
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
                        {offers.length === 0
                            ? <tr>
                                <td colSpan={8}>
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
                    <Col className="bg-light p-0 mt-sm-3 mt-lg-3 mt-xl-0" style={{minHeight: 400}}>
                        <OfferStudentApplicationsList connectedUser={connectedUser} offerId={currentlySelectedOffer!}/>
                    </Col>
                }
            </Row>
        </Container>
    );
}

export default CompanyOffersPage;