import { Viewer } from "@react-pdf-viewer/core";
import React, { useEffect, useState } from "react";
import { Button, Col, Container, Row, Table } from "react-bootstrap";
import { Link } from "react-router-dom";
import IUser from "../models/IUser";

const OffersListPage = ({ connectedUser, deconnexion }: { connectedUser: IUser, deconnexion: Function }): JSX.Element => {
    const [offers, setOffers] = useState<any[]>([]);
    const [pdf, setpdf] = useState<Uint8Array>(new Uint8Array([]))
    const [showPdf, setShowPdf] = useState<boolean>(false)

    // useEffect(() => {
    //     const fetchOffers = async () => {
    //         try {
    //             //TODO
    //             const response = await fetch("http://localhost:8080/TODO", {
    //                 method: "PUT",
    //                 headers: {
    //                     'Accept': 'application/json',
    //                     'Content-Type': 'application/json',
    //                 },
    //                 body: JSON.stringify({ "token": connectedUser.token })
    //             });
    //             if (response.ok) {
    //                 const data = await response.json();
    //                 setOffers(data);
    //             }
    //             else if (response.status === 403) {
    //                 alert("Session expiré");
    //                 deconnexion();
    //             }
    //             else {
    //                 console.log(response.status)
    //                 throw new Error("Error code not handled");
    //             }
    //         }
    //         catch {
    //             alert("Une erreur est survenue, ressayez.");
    //             window.location.href = "/"
    //         }
    //     }
    //     fetchOffers()
    //     setShowPdf(pdf.length > 0)
    // }, [connectedUser, pdf]);

    async function getPDF(offerID: number): Promise<void> {
        try {
            //TODO
            const response = await fetch("http://localhost:8080/offerPdf/" + offerID.toString(), {
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

    if (showPdf) {
        return (
            <Container>
                <div className="bg-dark p-2">
                    <Button className="Btn btn-primary" onClick={() => setShowPdf(false)}>
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
                                <th>Departement</th>
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
                                        <td>{offer.department}</td>
                                        <td>{offer.position}</td>
                                        <td>{offer.heureParSemaine}</td>
                                        <td>{offer.adresse}</td>
                                        <td><Button className="btn btn-warning" onClick={() => getPDF(offer.id)}>CV</Button></td>
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