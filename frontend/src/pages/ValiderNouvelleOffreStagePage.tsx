import React, {useEffect, useState} from "react";
import {Button, Col, Container, Row, Table} from "react-bootstrap";
import {Link} from "react-router-dom";
import IUser from "../models/IUser";
import {Viewer} from '@react-pdf-viewer/core';

const ValiderNouvelleOffreStagePage = ({connectedUser, deconnexion}:
                                           { connectedUser: IUser, deconnexion: Function }): JSX.Element => {
    const [offers, setOffers] = useState<any[]>([]);
    const [pdf, setpdf] = useState<Uint8Array>(new Uint8Array([]))
    const [showPDF, setShowPDF] = useState<boolean>(false)

    useEffect(() => {
        const fetchOffresAttendreValide = async () => {
            try {
                const response = await fetch("http://localhost:8080/unvalidatedOffers", {
                    method: "PUT",
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({"token": connectedUser.token})
                });
                if (response.ok) {
                    const data = await response.json();
                    setOffers(data);
                } else if (response.status === 403) {
                    alert("Session expiré");
                    deconnexion();
                } else {
                    throw new Error("Error code not handled");
                }
            } catch {
                alert("Une erreur est survenue, ressayez.");
                window.location.href = "/"
            }
        }
        fetchOffresAttendreValide()
    }, [connectedUser, deconnexion]);

    async function valideOffre(offerId: number, valid: boolean): Promise<void> {
        try {
            const url: String = valid ? "http://localhost:8080/validateOffer/" : "http://localhost:8080/removeOffer/";
            const response: Response = valid ? await fetch(url + offerId.toString(), {
                method: "PUT",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({"token": connectedUser.token})
            }) : await fetch(url + offerId.toString(), {
                method: "DELETE",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({"token": connectedUser.token})
            });

            if (response.ok) {
                setOffers(offers.filter(offer => offer.id !== offerId));
            } else if (response.status === 403) {
                alert("Session expiré");
                deconnexion();
            } else {
                throw new Error("Error code not handled");
            }
        } catch (exception) {
            alert("Une erreur est survenue, ressayez.");
        }
    }

    async function getPDF(offerId: number): Promise<void> {
        try {
            const response = await fetch("http://localhost:8080/offerPdf/" + offerId.toString(), {
                method: "PUT",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({"token": connectedUser.token})
            });

            if (response.ok) {
                const data = await response.json();
                setpdf(new Uint8Array(JSON.parse(data.pdf)))
                setShowPDF(true);
            } else if (response.status === 403) {
                alert("Session expiré");
                deconnexion();
            } else {
                throw new Error("Error code not handled");
            }
        } catch (exception) {
            alert("Une erreur est survenue, ressayez.");
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
                    <h1 className="fw-bold text-white display-3 pb-2">Validation des offres</h1>
                </Col>
                <Col sm={2}></Col>
            </Row>
            <Row>
                <Col>
                    <Table data-testid="tableValiderNouvelleOffreStage" className="text-center" hover>
                        <thead className="bg-primary">
                        <tr>
                            <th>Nom De Compagnie / Adresse</th>
                            <th>Départment / Position</th>
                            <th>Heure Par Semaine / Salaire</th>
                            <th>Pdf</th>
                            <th>Valide</th>
                            <th>Non Valide</th>
                        </tr>
                        </thead>
                        <tbody className="bg-light" data-testid="offre-container">
                        {offers.map((offer, index) => {
                            return (
                                <tr key={index}>
                                    <td>{offer.nomDeCompagnie} <br/> {offer.adresse}</td>
                                    <td>{offer.department} <br/> {offer.position}</td>
                                    <td>{offer.heureParSemaine} <br/> {offer.salaire}$/h</td>
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

