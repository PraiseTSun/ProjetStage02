import React, {useEffect, useState} from "react"
import IUser from "../../models/IUser";
import {Button, Col, Container, Row, Table} from "react-bootstrap";
import PageHeader from "../../components/universalComponents/PageHeader";
import IOffer from "../../models/IOffer";
import {generateAlert} from "../../services/universalServices/UniversalUtilService";
import {
    putAcceptedStudentsForOffer,
    putCompanyOffers,
    putOfferApplications,
    putStudentAcceptation,
    putStudentCv
} from "../../services/companyServices/CompanyFetchService";
import {Viewer} from "@react-pdf-viewer/core";
import IAcceptedStudents from "../../models/IAcceptedStudents";

const CompanyOffersPage = ({connectedUser}: { connectedUser: IUser }): JSX.Element => {
    const [offers, setOffers] = useState<IOffer[]>([]);
    const [students, setStudents] = useState<IUser[] | null>(null);
    const [cv, setCv] = useState<Uint8Array>(new Uint8Array([]))
    const [showCv, setShowCv] = useState<boolean>(false)
    const [acceptedStudents, setAcceptedStudents] = useState<string[]>([])
    const [currentlySelectedOffer, setCurrentlySelectedOffer] = useState<string>("")

    useEffect(() => {
        const fetchOffers = async (): Promise<void> => {
            try {
                const response: Response = await putCompanyOffers(connectedUser.id, connectedUser.token);

                if (response.ok) {
                    const data: IOffer[] = await response.json()
                    setOffers(data)
                } else {
                    generateAlert()
                }
            } catch {
                generateAlert()
            }
        }
        fetchOffers()
    }, [connectedUser]);

    const fetchOfferApplications = async (offerId: string): Promise<void> => {
        try {
            const response: Response = await putOfferApplications(offerId, connectedUser.token);

            if (response.ok) {
                const data = await response.json();
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
                setCurrentlySelectedOffer(data.offerId)
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
        } catch {
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

    if (showCv) {
        return (
            <Container className="min-vh-100 bg-white p-0">
                <div className="bg-dark p-2">
                    <Button className="Btn btn-primary" onClick={() => setShowCv(false)}>
                        Fermer
                    </Button>
                </div>
                <div>
                    <Viewer fileUrl={cv}/>
                </div>
            </Container>
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
                                    <th><Button disabled={currentlySelectedOffer == offer.id} variant="warning"
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
                                <th>Engager</th>
                            </tr>
                            </thead>
                            <tbody>
                            {students.length === 0 &&
                                <tr>
                                    <td colSpan={4}>
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
                                                {acceptedStudents.includes(student.id) ?
                                                    <h1 className="text-primary">Déjà engagé</h1> :
                                                    <Button variant="success" onClick={async () => {
                                                        await hireStudent(student.id)
                                                    }}>Engager</Button>}
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