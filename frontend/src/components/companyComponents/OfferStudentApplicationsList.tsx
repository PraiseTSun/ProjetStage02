import IUser from "../../models/IUser";
import {Button, Table} from "react-bootstrap";
import React, {useCallback, useEffect, useState} from "react";
import {
    putAcceptedStudentsForOffer,
    putGetCompanyInterviews,
    putOfferApplications,
    putStudentAcceptation,
    putStudentCv
} from "../../services/companyServices/CompanyFetchService";
import IAcceptedStudents from "../../models/IAcceptedStudents";
import IInterview from "../../models/IInterview";
import PdfComponent from "../universalComponents/PdfComponent";
import InterviewDateForm from "./InterviewDateForm";
import {universalFetch} from "../../services/universalServices/UniversalFetchService";

const OfferStudentApplicationsList = ({
                                          offerId,
                                          connectedUser
                                      }: { offerId: string, connectedUser: IUser }): JSX.Element => {
    const [interviews, setInterviews] = useState<IInterview[]>([]);
    const [showDateSelector, setShowDateSelector] = useState<boolean>(false)
    const [students, setStudents] = useState<IUser[]>([]);
    const [acceptedStudents, setAcceptedStudents] = useState<string[]>([])
    const [cv, setCv] = useState<Uint8Array>(new Uint8Array([]))
    const [showCv, setShowCv] = useState<boolean>(false)
    const [currentlySelectedStudent, setCurrentlySelectedStudent] = useState<string>("")


    const fetchOfferApplications = useCallback(async (): Promise<void> => {
        universalFetch(async () => await putOfferApplications(offerId, connectedUser.token),
            async (response: Response) => {
                const data = await response.json();
                setStudents(data.applicants);
            });
    }, [offerId, connectedUser]);

    const fetchAcceptedStudents = useCallback(async (): Promise<void> => {
        universalFetch(async () => await putAcceptedStudentsForOffer(offerId, connectedUser.token),
            async (response: Response) => {
                const data: IAcceptedStudents = await response.json();
                setAcceptedStudents(data.studentsId);
            });
    }, [offerId, connectedUser]);

    const fetchInterviews = useCallback(async (): Promise<void> => {
        universalFetch(async () => await putGetCompanyInterviews(connectedUser.id, connectedUser.token),
            async (response: Response) => {
                const data: IInterview[] = await response.json();
                setInterviews(data);
            }
        );
    }, [connectedUser]);

    useEffect(() => {
        fetchOfferApplications();
        fetchAcceptedStudents();
        fetchInterviews();
    }, [offerId, fetchOfferApplications, fetchAcceptedStudents, fetchInterviews]);

    const hireStudent = async (studentId: string): Promise<void> => {
        universalFetch(async () => await putStudentAcceptation(offerId, studentId, connectedUser.token),
            async (response: Response) => {
                setAcceptedStudents([...acceptedStudents, studentId])
            }
        )
    }


    const fetchStudentCv = async (studentId: string): Promise<void> => {
        universalFetch(async () => await putStudentCv(studentId, connectedUser.token),
            async (response: Response) => {
                const data: any = await response.json();
                setCv(new Uint8Array(JSON.parse(data.pdf)));
                setShowCv(true);
            });
    }

    const hasInterview = (offerId: string, studentId: string): boolean => {
        return interviews.some(interview => interview.offerId === offerId && interview.studentId === studentId)
    }

    const getInterview = (offerId: string, studentId: string): IInterview | undefined => {
        return interviews.find(interview => interview.offerId === offerId && interview.studentId === studentId)
    }

    if (showCv) {
        return (
            <PdfComponent pdf={cv} setShowPdf={setShowCv}/>
        );
    }

    if (showDateSelector) {
        return (
            <InterviewDateForm connectedUser={connectedUser}
                               offerId={offerId}
                               studentId={currentlySelectedStudent}
                               interviews={interviews}
                               setInterviews={setInterviews}
                               setShowDateSelector={setShowDateSelector}/>
        );
    }

    return (
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
                                    hasInterview(offerId, student.id) &&
                                    getInterview(offerId, student.id)!.studentSelectedDate !== ""
                                        ? <p>
                                            Entrevue confirmée pour le {getInterview(offerId, student.id)!
                                            .studentSelectedDate.replace("T", " ")}
                                        </p>
                                        : hasInterview(offerId, student.id)
                                            ? <p>En attente de confirmation de l'étudiant</p>
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
    );
}

export default OfferStudentApplicationsList;