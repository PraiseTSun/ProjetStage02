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
import {generateAlert} from "../../services/universalServices/UniversalUtilService";
import IAcceptedStudents from "../../models/IAcceptedStudents";
import IInterview from "../../models/IInterview";
import PdfComponent from "../universalComponents/PdfComponent";
import InterviewDateForm from "./InterviewDateForm";

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
        try {
            const response: Response = await putOfferApplications(offerId, connectedUser.token);

            if (response.ok) {
                const data = await response.json();
                setStudents(data.applicants);
            } else {
                generateAlert()
            }
        } catch {
            generateAlert()
        }
    }, [offerId]);

    const fetchAcceptedStudents = useCallback(async (): Promise<void> => {
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
    }, [offerId]);

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
        fetchOfferApplications();
        fetchAcceptedStudents();
        fetchInterviews();
    }, [offerId]);

    const hireStudent = async (studentId: string): Promise<void> => {
        try {
            const response: Response =
                await putStudentAcceptation(offerId, studentId, connectedUser.token);

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
                                        ? <p>Entrevue confirmé pour
                                            le {getInterview(offerId, student.id)?.studentSelectedDate}</p>
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