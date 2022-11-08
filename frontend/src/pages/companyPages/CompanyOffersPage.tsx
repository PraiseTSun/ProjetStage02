import React, {useCallback, useEffect, useState} from "react"
import IUser from "../../models/IUser";
import {Button, Col, Container, Row, Table} from "react-bootstrap";
import PageHeader from "../../components/universalComponents/PageHeader";
import IOffer from "../../models/IOffer";
import {generateAlert} from "../../services/universalServices/UniversalUtilService";
import {
    putAcceptedStudentsForOffer,
    putCompanyOffers,
    putGetCompanyInterviews,
    putOfferApplications,
    putStudentAcceptation,
    putStudentCv
} from "../../services/companyServices/CompanyFetchService";
import IAcceptedStudents from "../../models/IAcceptedStudents";
import PdfComponent from "../../components/universalComponents/PdfComponent";
import InterviewDateForm from "../../components/companyComponents/InterviewDateForm";
import IInterview from "../../models/IInterview";

const CompanyOffersPage = ({connectedUser}: { connectedUser: IUser }): JSX.Element => {
    const [offers, setOffers] = useState<IOffer[]>([]);
    const [students, setStudents] = useState<IUser[] | null>(null);
    const [interviews, setInterviews] = useState<IInterview[]>([]);
    const [cv, setCv] = useState<Uint8Array>(new Uint8Array([]))
    const [showCv, setShowCv] = useState<boolean>(false)
    const [showDateSelector, setShowDateSelector] = useState<boolean>(false)
    const [acceptedStudents, setAcceptedStudents] = useState<string[]>([])
    const [currentlySelectedOffer, setCurrentlySelectedOffer] = useState<string>("")
    const [currentlySelectedStudent, setCurrentlySelectedStudent] = useState<string>("")

    const fetchOffers = useCallback(async (): Promise<void> => {
        try {
            const response: Response = await putCompanyOffers(connectedUser.id, connectedUser.token);

            if (response.ok) {
                const data: IOffer[] = await response.json();
                setOffers(data);
            } else {
                generateAlert();
            }
        } catch {
            generateAlert();
        }
    }, [connectedUser]);

    const fetchInterviews = useCallback(async (): Promise<void> => {
        try {
            const response: Response = await putGetCompanyInterviews(connectedUser.id, connectedUser.token);

            if (response.ok) {
                const data: IInterview[] = await response.json();
                setInterviews(data);
            } else {
                generateAlert();
            }
        } catch {
            generateAlert();
        }
    }, [connectedUser]);

    useEffect(() => {
        fetchInterviews();
        fetchOffers()
    }, [connectedUser]);

    const fetchOfferApplications = async (offerId: string): Promise<void> => {
        try {
            const response: Response = await putOfferApplications(offerId, connectedUser.token);

            if (response.ok) {
                const data = await response.json();
                setCurrentlySelectedOffer(offerId)
                setStudents(data.applicants);
                await fetchAcceptedStudents(offerId);
            } else {
                generateAlert()
            }
        } catch {
            generateAlert()
        }
    }

    const fetchAcceptedStudents = async (offerId: string): Promise<void> => {
        try {
            const response: Response = await putAcceptedStudentsForOffer(offerId, connectedUser.token);

            if (response.ok) {
                const data: IAcceptedStudents = await response.json();
                setAcceptedStudents(data.studentsId);
            } else {
                generateAlert()
            }
        } catch {
            generateAlert()
        }
    }

    const hireStudent = async (studentId: string): Promise<void> => {
        try {
            const response: Response =
                await putStudentAcceptation(currentlySelectedOffer, studentId, connectedUser.token);

            if (response.ok) {
                setAcceptedStudents([...acceptedStudents, studentId])
            } else {
                generateAlert()
            }
        } catch (exception) {
            generateAlert()
        }
    }

    const fetchStudentCv = async (studentId: string): Promise<void> => {
        try {
            const response: Response = await putStudentCv(studentId, connectedUser.token);

            if (response.ok) {
                const data: any = await response.json();
                setCv(new Uint8Array(JSON.parse(data.pdf)));
                setShowCv(true);
            } else {
                generateAlert()
            }
        } catch {
            generateAlert()
        }
    }

    const hasInterview = (offerId: string, studentId: string): boolean => {
        return interviews.some(interview => interview.offerId === offerId && interview.studentId === studentId)
    }

    const getInterview = (offerId: string, studentId: string): IInterview | undefined => {
        return interviews.find(interview => interview.offerId === offerId && interview.studentId === studentId)
    }

    if (showCv) {
        return (
            <PdfComponent cv={cv} setShowPdf={setShowCv}/>
        );
    }

    if (showDateSelector) {
        return (
            <InterviewDateForm connectedUser={connectedUser}
                               offerId={currentlySelectedOffer}
                               studentId={currentlySelectedStudent}
                               interviews={interviews}
                               setInterviews={setInterviews}
                               setShowDateSelector={setShowDateSelector}/>
        );
    }

    return (
        <Container className="min-vh-100">
            <PageHeader title={"Mes offres"}/>
            <Row>
                <Col className="bg-light p-0 mx-2" style={{height: 300}}>
                    <Table className="text-center" hover>
                        <thead className="bg-primary text-white">
                        <tr>
                            <th>Nom de compagnie</th>
                            <th>Position</th>
                            <th>Departement</th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        {offers.map((offer, index) => {
                            return (
                                <tr key={index}>
                                    <td>{offer.nomDeCompagnie}</td>
                                    <td>{offer.position}</td>
                                    <td>{offer.department}</td>
                                    <th><Button disabled={currentlySelectedOffer === offer.id} variant="warning"
                                                onClick={async () => {
                                                    await fetchOfferApplications(offer.id)
                                                }}>Applicants</Button></th>
                                </tr>
                            );
                        })}
                        </tbody>
                    </Table>
                </Col>
                {students !== null &&
                    <Col className="bg-light p-0 mx-2" style={{height: 300}}>
                        <Table className="text-center" hover>
                            <thead className="bg-primary text-white">
                            <tr>
                                <th>Prénom</th>
                                <th>Nom</th>
                                <th>CV</th>
                                <th>Entrevue</th>
                                <th>Engager</th>
                            </tr>
                            </thead>
                            <tbody>
                            {students.length === 0 &&
                                <tr>
                                    <td colSpan={5}>
                                        <h1>Aucun applicants</h1>
                                    </td>
                                </tr>
                            }
                            {students.length !== 0 &&
                                students!.map((student, index) => {
                                    return (
                                        <tr key={index}>
                                            <td>{student.firstName}</td>
                                            <td>{student.lastName}</td>
                                            <td><Button variant="warning" onClick={async () => {
                                                await fetchStudentCv(student.id)
                                            }}>CV</Button></td>
                                            <td>
                                                {
                                                    hasInterview(currentlySelectedOffer, student.id) && getInterview(currentlySelectedOffer, student.id)!.studentSelectedDate !== ""
                                                        ? <p>Entrevue confirmé pour
                                                            le {getInterview(currentlySelectedOffer, student.id)?.studentSelectedDate}</p>
                                                        : hasInterview(currentlySelectedOffer, student.id)
                                                            ? <p>En attente de confirmation</p>
                                                            : <Button variant="primary" onClick={
                                                                () => {
                                                                    setCurrentlySelectedStudent(student.id)
                                                                    setShowDateSelector(true)
                                                                }}>Soumettre mes disponibilités</Button>
                                                }
                                            </td>
                                            <td>
                                                <Button disabled={acceptedStudents.includes(student.id)}
                                                        variant="success" onClick={async () => {
                                                    await hireStudent(student.id)
                                                }}>Engager</Button>
                                                {acceptedStudents.includes(student.id) &&
                                                    <p className="text-primary">En attente du contrat</p>}
                                            </td>
                                        </tr>
                                    );
                                })}
                            </tbody>
                        </Table>
                    </Col>}
            </Row>
        </Container>
    );
}

export default CompanyOffersPage;