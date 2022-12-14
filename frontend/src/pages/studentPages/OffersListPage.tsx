import React, {useCallback, useEffect, useState} from "react";
import {Button, Col, Container, Row, Table} from "react-bootstrap";
import IUser from "../../models/IUser";
import IOffer from "../../models/IOffer";
import IStudentApplys from "../../models/IStudentApplys";
import {
    putApplyToOffer,
    putGetOffers,
    putGetOfferStudent,
    putGetStudentInterviews,
    putStudentApplys,
    putStudentSelectDate,
    removeStudentApplication
} from "../../services/studentServices/StudentFetchService";
import IInterview from "../../models/IInterview";
import PdfComponent from "../../components/universalComponents/PdfComponent";
import PageHeader from "../../components/universalComponents/PageHeader";
import {universalFetch} from "../../services/universalServices/UniversalFetchService";
import {hasCv} from "../../services/universalServices/UniversalUtilService";

const OffersListPage = ({connectedUser}:
                            { connectedUser: IUser }): JSX.Element => {
    const [offers, setOffers] = useState<IOffer[]>([]);
    const [pdf, setPdf] = useState<Uint8Array>(new Uint8Array([]))
    const [showPdf, setShowPDF] = useState<boolean>(false)
    const [interviews, setInterviews] = useState<IInterview[]>([])
    const [studentApplys, setStudentApplys] =
        useState<IStudentApplys>({
            studentId: connectedUser.id, offersId: new Array<string>(),
            removableOffersId: new Array<string>()
        });

    const fetchInterviews = useCallback(async () => {
        universalFetch(async () => await putGetStudentInterviews(connectedUser.id, connectedUser.token),
            async (response: Response) => {
                const data: IInterview[] = await response.json();
                setInterviews(data);
            });
    }, [connectedUser]);


    const fetchOffers = useCallback(async () => {
        universalFetch(async () => await putGetOffers(connectedUser.id, connectedUser.token),
            async (response: Response) => {
                const data: IOffer[] = await response.json();
                setOffers(data);
            });
    }, [connectedUser]);

    const fetchStudentApplys = useCallback(async () => {
        universalFetch(async () => await putStudentApplys(connectedUser.id, connectedUser.token),
            async (response: Response) => {
                const data = await response.json();
                setStudentApplys(data);
            })
    }, [connectedUser]);


    useEffect(() => {
        fetchStudentApplys();
        fetchOffers();
        fetchInterviews();
    }, [connectedUser, fetchOffers, fetchInterviews, fetchStudentApplys]);

    const applyToOffer = useCallback(async (offerId: string): Promise<void> => {
        universalFetch(async () => await putApplyToOffer(connectedUser.id, offerId, connectedUser.token),
            async (response: Response) => {
                setStudentApplys(
                    {
                        ...studentApplys,
                        offersId: [...studentApplys.offersId, offerId],
                        removableOffersId: [...studentApplys.removableOffersId, offerId]
                    });
            });
    }, [connectedUser, studentApplys]);

    const retirerOffre = useCallback(async (removableOffersId: string): Promise<void> => {
        universalFetch(async () => await removeStudentApplication(connectedUser.token,
                Number(removableOffersId), Number(connectedUser.id)),
            async (response: Response) => {
                studentApplys.removableOffersId = studentApplys.removableOffersId.filter((id) =>
                    id !== removableOffersId);
                studentApplys.offersId = studentApplys.offersId.filter((id) =>
                    id !== removableOffersId);
                setStudentApplys(
                    {...studentApplys});
            });
    }, [connectedUser, studentApplys]);

    const confirmInterview = async (interviewId: string, selectedDate: string): Promise<void> => {
        universalFetch(async () => await putStudentSelectDate(
                connectedUser.id,
                interviewId,
                selectedDate,
                connectedUser.token),
            async (response: Response) => {
                interviews.find(interview =>
                    interview.interviewId === interviewId)!.studentSelectedDate = selectedDate;
                setInterviews([...interviews]);
            });
    }

    const getPDF = async (offerId: string): Promise<void> => {
        universalFetch(async () => await putGetOfferStudent(offerId, connectedUser.token),
            async (response: Response) => {
                const data = await response.json();
                setPdf(new Uint8Array(JSON.parse(data.pdf)));
                setShowPDF(true);
            });
    }

    const hasInterview = (offerId: string): boolean => {
        return interviews.some(interview => interview.offerId === offerId);
    }

    const getInterview = (offerId: string): IInterview | undefined => {
        return interviews.find(interview => interview.offerId === offerId);
    }

    if (showPdf) {
        return (
            <PdfComponent pdf={pdf} setShowPdf={setShowPDF}/>
        );
    }

    const getInterviewTableCell = (offerId: string): JSX.Element | JSX.Element[] => {
        if (!studentApplys.offersId.some(applyOfferId => applyOfferId === offerId)) {
            return <p>Pas encore appliqu??</p>;
        }

        if (!hasInterview(offerId)) {
            return <p>En attente des entrevues</p>
        }

        if (getInterview(offerId)!.studentSelectedDate === "") {
            return (
                getInterview(offerId)!.companyDateOffers.map((date: string, index: number) =>
                    <div key={index}>
                        <Button className="Btn btn-primary mb-2"
                                onClick={() => {
                                    confirmInterview(
                                        getInterview(offerId)!.interviewId, date)
                                }}>
                            {date.replace("T", " ")}
                        </Button>
                        <br/>
                    </div>
                )
            );
        }

        // Entrevue confirm??e par l'??tudiant
        return (
            <p>
                Entrevue confirm??e pour le {getInterview(offerId)!
                .studentSelectedDate.replace("T", " ")}
            </p>
        );
    }

    return (
        <Container className="min-vh-100 pb-5">
            <PageHeader title={"Offres de stages"}/>
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
                            <th>Entrevue</th>
                            <th>Offre</th>
                            <th>Appliquer</th>
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
                                        <td>{getInterviewTableCell(offer.id)}</td>
                                        <td><Button className="btn btn-warning" onClick={
                                            async () => {
                                                await getPDF(offer.id)
                                            }
                                        }>PDF</Button></td>
                                        <td>
                                            {!hasCv(connectedUser) &&
                                                <p className="h4 text-danger">Vous n'avez pas de CV</p>}
                                            {hasCv(connectedUser) &&
                                                <>
                                                    {studentApplys.offersId.includes(offer.id) ?
                                                        <Button className="btn btn-danger"
                                                                disabled={!studentApplys.removableOffersId.includes(offer.id)}
                                                                onClick={() => {
                                                                    retirerOffre(offer.id)
                                                                }
                                                                }>Retirer</Button>
                                                        :
                                                        <Button className="btn btn-success" onClick={() => {
                                                            applyToOffer(offer.id)
                                                        }}>Postuler</Button>
                                                    }
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