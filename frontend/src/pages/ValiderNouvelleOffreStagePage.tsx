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
            const response: Response = valid ? await fetch(url + offerId.toString(), {
                method: "PUT",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ "token": connectedUser.token })
            }) : await fetch(url + offerId.toString(), {
                method: "DELETE",
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
                setpdf(new Uint8Array([37, 80, 68, 70, 45, 49, 46, 55, 10, 10, 49, 32, 48, 32, 111, 98, 106, 32, 32, 37, 32, 101, 110, 116,
                    114, 121, 32, 112, 111, 105, 110, 116, 10, 60, 60, 10, 32, 32, 47, 84, 121, 112, 101, 32, 47, 67,
                    97, 116, 97, 108, 111, 103, 10, 32, 32, 47, 80, 97, 103, 101, 115, 32, 50, 32, 48, 32, 82, 10, 62,
                    62, 10, 101, 110, 100, 111, 98, 106, 10, 10, 50, 32, 48, 32, 111, 98, 106, 10, 60, 60, 10, 32, 32,
                    47, 84, 121, 112, 101, 32, 47, 80, 97, 103, 101, 115, 10, 32, 32, 47, 77, 101, 100, 105, 97, 66,
                    111, 120, 32, 91, 32, 48, 32, 48, 32, 50, 48, 48, 32, 50, 48, 48, 32, 93, 10, 32, 32, 47, 67, 111,
                    117, 110, 116, 32, 49, 10, 32, 32, 47, 75, 105, 100, 115, 32, 91, 32, 51, 32, 48, 32, 82, 32, 93,
                    10, 62, 62, 10, 101, 110, 100, 111, 98, 106, 10, 10, 51, 32, 48, 32, 111, 98, 106, 10, 60, 60, 10,
                    32, 32, 47, 84, 121, 112, 101, 32, 47, 80, 97, 103, 101, 10, 32, 32, 47, 80, 97, 114, 101, 110, 116,
                    32, 50, 32, 48, 32, 82, 10, 32, 32, 47, 82, 101, 115, 111, 117, 114, 99, 101, 115, 32, 60, 60, 10,
                    32, 32, 32, 32, 47, 70, 111, 110, 116, 32, 60, 60, 10, 32, 32, 32, 32, 32, 32, 47, 70, 49, 32, 52,
                    32, 48, 32, 82, 32, 10, 32, 32, 32, 32, 62, 62, 10, 32, 32, 62, 62, 10, 32, 32, 47, 67, 111, 110,
                    116, 101, 110, 116, 115, 32, 53, 32, 48, 32, 82, 10, 62, 62, 10, 101, 110, 100, 111, 98, 106, 10,
                    10, 52, 32, 48, 32, 111, 98, 106, 10, 60, 60, 10, 32, 32, 47, 84, 121, 112, 101, 32, 47, 70, 111,
                    110, 116, 10, 32, 32, 47, 83, 117, 98, 116, 121, 112, 101, 32, 47, 84, 121, 112, 101, 49, 10, 32,
                    32, 47, 66, 97, 115, 101, 70, 111, 110, 116, 32, 47, 84, 105, 109, 101, 115, 45, 82, 111, 109, 97,
                    110, 10, 62, 62, 10, 101, 110, 100, 111, 98, 106, 10, 10, 53, 32, 48, 32, 111, 98, 106, 32, 32, 37,
                    32, 112, 97, 103, 101, 32, 99, 111, 110, 116, 101, 110, 116, 10, 60, 60, 10, 32, 32, 47, 76, 101,
                    110, 103, 116, 104, 32, 52, 52, 10, 62, 62, 10, 115, 116, 114, 101, 97, 109, 10, 66, 84, 10, 55, 48,
                    32, 53, 48, 32, 84, 68, 10, 47, 70, 49, 32, 49, 50, 32, 84, 102, 10, 40, 72, 101, 108, 108, 111, 44,
                    32, 119, 111, 114, 108, 100, 33, 41, 32, 84, 106, 10, 69, 84, 10, 101, 110, 100, 115, 116, 114, 101,
                    97, 109, 10, 101, 110, 100, 111, 98, 106, 10, 10, 120, 114, 101, 102, 10, 48, 32, 54, 10, 48, 48,
                    48, 48, 48, 48, 48, 48, 48, 48, 32, 54, 53, 53, 51, 53, 32, 102, 32, 10, 48, 48, 48, 48, 48, 48, 48,
                    48, 49, 48, 32, 48, 48, 48, 48, 48, 32, 110, 32, 10, 48, 48, 48, 48, 48, 48, 48, 48, 55, 57, 32, 48,
                    48, 48, 48, 48, 32, 110, 32, 10, 48, 48, 48, 48, 48, 48, 48, 49, 55, 51, 32, 48, 48, 48, 48, 48, 32,
                    110, 32, 10, 48, 48, 48, 48, 48, 48, 48, 51, 48, 49, 32, 48, 48, 48, 48, 48, 32, 110, 32, 10, 48,
                    48, 48, 48, 48, 48, 48, 51, 56, 48, 32, 48, 48, 48, 48, 48, 32, 110, 32, 10, 116, 114, 97, 105, 108,
                    101, 114, 10, 60, 60, 10, 32, 32, 47, 83, 105, 122, 101, 32, 54, 10, 32, 32, 47, 82, 111, 111, 116,
                    32, 49, 32, 48, 32, 82, 10, 62, 62, 10, 115, 116, 97, 114, 116, 120, 114, 101, 102, 10, 52, 57, 50,
                    10, 37, 37, 69, 79, 70]));
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
                    <Viewer fileUrl={pdf}/>
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
                    <h1 className="fw-bold">Validation de offre</h1>
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
                        <tbody role="tbody" className="bg-light">
                        {offers.map((offer, index) => {
                            return (
                                <tr key={index} data-testid="offre-container">
                                    <td>{offer.nomDeCompagnie}</td>
                                    <td>{offer.department} <br/> {offer.position}</td>
                                    <td>{offer.heureParSemaine} <br/> {offer.adresse}</td>
                                    <td><Button className="btn btn-warning" onClick={() => getPDF(offer.id)}>pdf</Button></td>
                                    <td>
                                        <Button className="btn btn-success mx-5" onClick={() => valideOffre(offer.id, index, true)}>O</Button>
                                    </td>
                                    <td>
                                        <Button  className="btn btn-danger" onClick={() => valideOffre(offer.id, index, false)}>X</Button>
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

