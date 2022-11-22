import IUser from "../../models/IUser";
import {Button, Col, Container, Row, Table} from "react-bootstrap";
import React, {useCallback, useEffect, useState} from "react";
import PageHeader from "../../components/universalComponents/PageHeader";
import IContract from "../../models/IContract";
import {putStudentContracts, putStudentSignatureContract} from "../../services/studentServices/StudentFetchService";
import {generateAlert} from "../../services/universalServices/UniversalUtilService";
import SignaturePopup from "../../components/universalComponents/SignaturePopup";

const StudentContractsPage = ({connectedUser}: { connectedUser: IUser }): JSX.Element => {
    const nextYear = new Date().getFullYear() + 1
    const [signing, setSigning] = useState<boolean>(false);
    const [contrats, setContrats] = useState<IContract[]>([]);
    const [lastSelectedContract, setLastSelectedContract] = useState<IContract>();

    const fetchContracts = useCallback(async () => {
        try {
            const response: Response = await putStudentContracts(connectedUser.id,
                `Hiver ${nextYear}`, connectedUser.token);

            if (response.ok) {
                const data: IContract[] = await response.json();
                setContrats(data);
            } else {
                generateAlert()
            }
        } catch {
            generateAlert()
        }
    }, [connectedUser, nextYear]);

    useEffect(() => {
        fetchContracts();
    }, [fetchContracts, connectedUser])

    const signContract = async (signature: string): Promise<void> => {
        try {
            const response: Response = await
                putStudentSignatureContract(connectedUser.id,
                    lastSelectedContract!.contractId,
                    signature,
                    connectedUser.token)

            if (response.ok) {
                contrats[contrats.indexOf(lastSelectedContract!)].studentSignature = signature;
                setContrats([...contrats])
                setSigning(false)
            } else {
                generateAlert()
            }

        } catch {
            generateAlert()
        }
    }

    return (
        <Container className="min-vh-100">
            {signing && <SignaturePopup setSigning={setSigning} onSignature={signContract}/>}
            <PageHeader title="Mes contrats"/>
            <Row>
                <Col className="bg-light p-0" style={{minHeight: 400}}>
                    <Table className="text-center" hover>
                        <thead className="bg-primary text-white">
                        <tr>
                            <th>Description</th>
                            <th>Status de signature</th>
                        </tr>
                        </thead>
                        <tbody>
                        {contrats.length === 0
                            ? <tr>
                                <td colSpan={2}>
                                    <p className="h1">Aucun contrats</p>
                                </td>
                            </tr>
                            : contrats.map((contract, index) => {
                                return (
                                    <tr key={index}>
                                        <td>{contract.description}</td>
                                        <td>
                                            {contract.studentSignature.length > 0 ?
                                                <p className="text-success">Vous avez signé</p> :
                                                <p className="text-danger">Vous n'avez pas signé!</p>}
                                            <Button variant="success" disabled={contract.studentSignature.length > 0}
                                                    onClick={() => {
                                                        setLastSelectedContract(contract);
                                                        setSigning(true)
                                                    }}>Signer</Button>
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

export default StudentContractsPage