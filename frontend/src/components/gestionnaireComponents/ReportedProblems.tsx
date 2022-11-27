import React, {useEffect, useState} from "react";
import {
    putGetUnresolvedProblems,
    putResolveProblem
} from "../../services/gestionnaireServices/GestionnaireFetchService";
import IUser from "../../models/IUser";
import {Button, Col, Table} from "react-bootstrap";
import {IReportedProblem} from "../../models/IReportedProblem";

const ReportedProblems = ({user: connectedUser}: { user: IUser }) => {
    const [problems, setProblems] = useState<IReportedProblem[]>([]);
    const resolve = async (id: number, index: number) => {
        await putResolveProblem(connectedUser.token, id);
        setProblems(problems.splice(index + 1, 1));
    }
    useEffect(() => {
        const fetchProblems = async () => {
            const response = await putGetUnresolvedProblems(connectedUser.token)
            if (response.ok) {
                const data = await response.json();
                setProblems(data);
            }
        }
        fetchProblems()
    }, [connectedUser]);

    const getBody = () => {
        if (problems.length === 0) {
            return <h1 className="text-light text-center">Aucun problèmes signalés</h1>
        }
        return table()
    }
    const table = () => {
        return (
            <Table className="text-center" hover responsive>
                {tableHeader()}
                {tableBody()}
            </Table>
        )
    }
    const tableHeader = () => {
        return (
            <thead className="bg-primary text-white">
            <tr>
                <th className="col-3">Catégorie</th>
                <th className="col-3">Email</th>
                <th className="col-3">Détails</th>
                <th className="col-3">Résoudre</th>
            </tr>
            </thead>)
    }
    const tableBody = () => {
        return (
            <tbody className="bg-light">
            {
                problems.map((problem: IReportedProblem, idx: number) => {
                    return (
                        <tr key={idx}>
                            <td className="col-3">{problem.problemCategory}</td>
                            <td className="col-3">{problem.email}</td>
                            <td className="col-3">{problem.problemDetails}</td>
                            <td className="col-3">
                                <Button variant="primary" onClick={() => resolve(problem.id, idx)}>Problème
                                    résolu</Button>
                            </td>
                        </tr>
                    );
                })
            }
            </tbody>)
    }
    return (
        <>
            <Col className="mx-3">
                {getBody()}
            </Col>
        </>)
}
export default ReportedProblems