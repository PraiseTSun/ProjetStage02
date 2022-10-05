import React, { useEffect, useState } from "react";
import { Container, Row, Col, Table, Button } from "react-bootstrap";
import { Link } from "react-router-dom";
import IUser from "../models/IUser";
import { Viewer } from '@react-pdf-viewer/core';

const ValiderNouvelleOffreStagePage = ({ connectedUser, deconnexion }: { connectedUser: IUser, deconnexion: Function }): JSX.Element => {
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
                    console.log(response.status)
                    throw new Error("Error code not handled");
                }
            }
            catch {
                alert("Une erreur est survenue, ressayez.");
                window.location.href = "/"
            }
        }
        fetchOffresAttendreValide()
        setShowPDF(pdf.length > 0)
    }, [connectedUser, pdf]);

    async function valideOffre(offerId: number, index: number, valid: boolean): Promise<void> {
        try {
            const url: String = valid ? "http://localhost:8080/validateOffer/" : "http://localhost:8080/removeOffer/";
            const response: Response = await fetch(url + offerId.toString(), {
                method: "PUT",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ "token": connectedUser.token })
            });

            if (response.ok) {
                setOffers(offers.splice(index + 1, 1));
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

    async function getPDF(offerId: number): Promise<void> {
        try {
            const response = await fetch("http://localhost:8080/offerPdf/" + offerId.toString(), {
                method: "PUT",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ "token": connectedUser.token })
            });

            if (response.ok) {
                const data = await response.json();
                setpdf(new Uint8Array(JSON.parse(data.pdf)));
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

    if (showPDF) {
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
                    <h1 className="fw-bold">Validation de CV</h1>
                </Col>
                <Col sm={2}></Col>
            </Row>
            <Row>
                <Col>
                    <Table className="text-center" hover>
                        <thead className="bg-primary">
                        <tr>
                            <th>Nom De Compagnie</th>
                            <th>Départment / Position</th>
                            <th>Heure Par Semaine / Adresse</th>
                            <th>Pdf</th>
                            <th>Valide</th>
                            <th>Non Valide</th>
                        </tr>

                        </thead>
                        <tbody className="bg-light">
                        {offers.map((offer, index) => {
                            return (
                                <tr key={index}>
                                    <td>{offer.nomDeCompagnie}</td>
                                    <td>{offer.department} <br/> {offer.position}</td>
                                    <td>{offer.heureParSemaine} <br/> {offer.adresse}</td>
                                    <td><Button className="btn btn-warning" onClick={() => getPDF(offer.id)}>pdf</Button></td>
                                    <td>
                                        <Button className="btn btn-success mx-2" onClick={() => valideOffre(offer.id, index, true)}>O</Button>
                                        <Button className="btn btn-danger" onClick={() => valideOffre(offer.id, index, false)}>X</Button>
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

