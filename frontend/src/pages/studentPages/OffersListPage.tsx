import {Viewer} from "@react-pdf-viewer/core";
import React, {useCallback, useEffect, useState} from "react";
import {Button, Col, Container, Row, Table} from "react-bootstrap";
import {Link} from "react-router-dom";
import IUser from "../../models/IUser";
import IOffer from "../../models/IOffer";
import IStudentApplys from "../../models/IStudentApplys";
import {
    putApplyToOffer,
    putGetOffers,
    putGetOfferStudent,
    putGetStudentInterviews,
    putStudentApplys,
    putStudentSelectDate
} from "../../services/studentServices/StudentFetchService";
import {generateAlert} from "../../services/universalServices/UniversalUtilService";
import IInterview from "../../models/IInterview";

const OffersListPage = ({connectedUser}:
                            { connectedUser: IUser }): JSX.Element => {
    const [offers, setOffers] = useState<IOffer[]>([]);
    const [pdf, setPDF] = useState<Uint8Array>(new Uint8Array([]))
    const [showPdf, setShowPDF] = useState<boolean>(false)
    const [interviews, setInterviews] = useState<IInterview[]>([])
    const [studentApplys, setStudentApplys] =
        useState<IStudentApplys>({studentId: connectedUser.id, offersId: new Array<string>()});

    const fetchInterviews = useCallback(async () => {
        try {
            const response: Response = await putGetStudentInterviews(connectedUser.id, connectedUser.token);
            if (response.ok) {
                const data: IInterview[] = await response.json();
                setInterviews(data);
                console.log(data)
            } else {
                generateAlert()
            }
        } catch {
            generateAlert()
        }
    }, [connectedUser]);


    const fetchOffers = useCallback(async () => {
        try {
            const response: Response = await putGetOffers(connectedUser.id, connectedUser.token);
            if (response.ok) {
                const data: IOffer[] = await response.json();
                setOffers(data);
            } else {
                generateAlert()
            }
        } catch {
            generateAlert()
        }
    }, [connectedUser]);

    const fetchStudentApplys = useCallback(async () => {
        try {
            const response = await putStudentApplys(connectedUser.id, connectedUser.token);
            if (response.ok) {
                const data = await response.json();
                setStudentApplys(data);
            } else {
                generateAlert()
            }
        } catch (exception) {
            generateAlert()
        }
    }, [connectedUser]);

    useEffect(() => {
        fetchStudentApplys();
        fetchOffers();
        fetchInterviews();
    }, [connectedUser])

    const applyToOffer = async (offerId: string): Promise<void> => {
        try {
            const response = await putApplyToOffer(connectedUser.id, offerId, connectedUser.token)

            if (response.ok) {
                setStudentApplys(
                    {
                        studentId: connectedUser.id,
                        offersId: [...studentApplys.offersId, offerId]
                    });
            } else {
                generateAlert()
            }
        } catch {
            generateAlert()
        }
    }

    const confirmInterview = async (interviewId: string, selectedDate: string): Promise<void> => {
        try {
            const response = await putStudentSelectDate(connectedUser.id, interviewId, selectedDate, connectedUser.token)
            if (response.ok) {
                interviews.find(interview => interview.interviewId === interviewId)!.studentSelectedDate = selectedDate;
                setInterviews([...interviews]);
            } else {
                generateAlert()
            }
        } catch {
            generateAlert()
        }
    }

    const getPDF = async (offerId: string): Promise<void> => {
        try {
            const response = await putGetOfferStudent(offerId, connectedUser.token)

            if (response.ok) {
                const data = await response.json();
                setPDF(new Uint8Array(JSON.parse(data.pdf)));
                setShowPDF(true);
            } else {
                generateAlert()
            }
        } catch (exception) {
            generateAlert()
        }
    }

    const hasInterview = (offerId: string): boolean => {
        return interviews.some(interview => interview.offerId === offerId);
    }

    const getInterview = (offerId: string): IInterview | undefined => {
        return interviews.find(interview => interview.offerId === offerId);
    }

    if (showPdf) {
        return (
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
        );
    }

    return (
        <Container className="min-vh-100">
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
                            <th>Adresse</th>
                            <th>Position</th>
                            <th>Heures par semaine</th>
                            <th>Salaire</th>
                            <th>Date de début</th>
                            <th>Date de fin</th>
                            <th>Entrevue</th>
                            <th>Offre</th>
                            <th>Appliquer</th>
                        </tr>
                        </thead>
                        <tbody className="bg-light">
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
                                    <td>
                                        {!hasInterview(offer.id) && "En attente des entrevues"}
                                        {hasInterview(offer.id) && getInterview(offer.id)?.studentSelectedDate === "" &&
                                            <Col>
                                                <Button
                                                    className="mb-2" onClick={async () => {
                                                    await confirmInterview(getInterview(offer.id)!.interviewId, getInterview(offer.id)!.companyDateOffers[0])
                                                }
                                                }>{getInterview(offer.id)!.companyDateOffers[0].replace("T", " ") + "h"}</Button>
                                                <br/>
                                                <Button
                                                    className="mb-2" onClick={async () => {
                                                    await confirmInterview(getInterview(offer.id)!.interviewId, getInterview(offer.id)!.companyDateOffers[1])
                                                }
                                                }>{getInterview(offer.id)!.companyDateOffers[1].replace("T", " ") + "h"}</Button>
                                                <br/>
                                                <Button onClick={async () => {
                                                    await confirmInterview(getInterview(offer.id)!.interviewId, getInterview(offer.id)!.companyDateOffers[2])
                                                }
                                                }>{getInterview(offer.id)!.companyDateOffers[2].replace("T", " ") + "h"}</Button>
                                            </Col>}
                                        {hasInterview(offer.id) && getInterview(offer.id)?.studentSelectedDate !== "" &&
                                            "Entrevue confirmée pour le " + getInterview(offer.id)!.studentSelectedDate}
                                    </td>
                                    <td><Button className="btn btn-warning" onClick={
                                        async () => await getPDF(offer.id)
                                    }>PDF</Button></td>
                                    <td>
                                        {connectedUser.cv === null &&
                                            <p className="h4 text-danger">Vous n'avez pas de CV</p>}
                                        {connectedUser.cv !== null &&
                                            <>
                                                {studentApplys.offersId.includes(offer.id) &&
                                                    <p className="h4 text-success">Déjà Postulé</p>}
                                                <Button disabled={studentApplys.offersId.includes(offer.id)}
                                                        className="btn btn-success" onClick={async () => {
                                                    await applyToOffer(offer.id)
                                                }}>Postuler</Button>
                                            </>}
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

export default OffersListPage;