import { Viewer } from "@react-pdf-viewer/core";
import React, { useEffect, useState } from "react";
import { Button, Col, Container, Row, Table } from "react-bootstrap";
import { Link } from "react-router-dom";
import IUser from "../models/IUser";
import IOffer from "../models/IOffer";

const OffersListPage = ({ connectedUser, deconnexion }: { connectedUser: IUser, deconnexion: Function }): JSX.Element => {
    const [offers, setOffers] = useState<IOffer[]>([]);
    const [pdf, setPDF] = useState<Uint8Array>(new Uint8Array([]))
    const [showPdf, setShowPDF] = useState<boolean>(false)

    useEffect(() => {
        const fetchOffers = async () => {
            try {
                const response = await fetch("http://localhost:8080/getOffers/" + connectedUser.id, {
                    method: "PUT",
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({ "token": connectedUser.token })
                });
                if (response.ok) {
                    const data = await response.json();
                    setOffers(data);
                }
                else if (response.status === 403) {
                    alert("Session expiré");
                    deconnexion();
                }
                else {
                    throw new Error("Error code not handled");
                }
            }
            catch {
                alert("Une erreur est survenue, ressayez.");
                window.location.href = "/"
            }
        }
        fetchOffers()
    }, [connectedUser, deconnexion]);

    async function getPDF(offerID: string): Promise<void> {
        try {
            const response = await fetch("http://localhost:8080/getOfferStudent/" + offerID, {
                method: "PUT",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ "token": connectedUser.token })
            });

            if (response.ok) {
                const data = await response.json();
                setPDF(new Uint8Array(JSON.parse(data.pdf)));
                setShowPDF(true);
            }
            else if (response.status === 403) {
                alert("Session expiré");
                deconnexion();
            }
            else {
                throw new Error("Error code not handled");
            }
        } catch (exception) {
            alert("Une erreur est survenue, ressayez.");
        }
    }

    if (showPdf) {
        return (
            <Container>
                <div className="bg-dark p-2">
                    <Button className="Btn btn-primary" onClick={() => setShowPDF(false)}>
                        Fermer
                    </Button>
                </div>
                <div>
                    <Viewer fileUrl={pdf} />
                </div>
            </Container>
        );
    }

    return (
        <Container className="vh-100">
            <Row>
                <Col sm={2}>
                    <Link to="/" className="btn btn-primary my-3">Home</Link>
                </Col>
                <Col sm={8} className="text-center pt-2">
                    <h1 className="fw-bold text-white display-3 pb-2">Liste de stages</h1>
                </Col>
                <Col sm={2}></Col>
            </Row>
            <Row>
                <Col>
                    <Table className="text-center" hover>
                        <thead className="bg-primary text-white">
                            <tr>
                                <th>Compagnie</th>
                                <th>Position</th>
                                <th>Heures par semaine</th>
                                <th>Adresse</th>
                                <th>Offre</th>
                            </tr>
                        </thead>
                        <tbody className="bg-light">
                            {offers.map((offer, index) => {
                                return (
                                    <tr key={index}>
                                        <td>{offer.nomDeCompagnie}</td>
                                        <td>{offer.position}</td>
                                        <td>{offer.heureParSemaine}</td>
                                        <td>{offer.adresse}</td>
                                        <td><Button className="btn btn-warning" onClick={async () => await getPDF(offer.id)}>PDF</Button></td>
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

export default OffersListPage;