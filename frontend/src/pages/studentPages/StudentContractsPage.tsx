import IUser from "../../models/IUser";
import {Button, Col, Container, Row, Table} from "react-bootstrap";
import React, {useCallback, useEffect, useState} from "react";
import PageHeader from "../../components/universalComponents/PageHeader";
import IContract from "../../models/IContract";
import {putStudentContracts, putStudentSignatureContract} from "../../services/studentServices/StudentFetchService";
import {generateAlert} from "../../services/universalServices/UniversalUtilService";
import SignaturePad from "react-signature-canvas"

const StudentContractsPage = ({connectedUser}: { connectedUser: IUser }): JSX.Element => {
    const nextYear = new Date().getFullYear() + 1
    const [signing, setSigning] = useState<boolean>(false);
    const [contrats, setContrats] = useState<IContract[]>([]);
    const [lastSelectedContract, setLastSelectedContract] = useState<IContract>();
    let sigPad: SignaturePad | null

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

    if (signing) {
        return (
            <Container className="vh-100">
                <Row className="bg-dark p-2">
                    <Col sm={1}><Button variant="danger" onClick={() => {
                        setSigning(false)
                    }}>Fermer</Button></Col>
                    <Col sm={10}></Col>
                    <Col sm={1}><Button variant="success" onClick={() => {
                        if (sigPad!.isEmpty()) {
                            alert("Vous devez signer!")
                        } else {
                            signContract(sigPad!.toDataURL())
                        }
                    }}>Signer</Button></Col>
                </Row>
                <Row>
                    <Col sm={4} className="mx-auto mt-3">
                        <SignaturePad canvasProps={{width: 500, height: 200, className: 'border border-5 bg-light'}}
                                      ref={(ref) => {
                                          sigPad = ref
                                      }}/>
                        <Button onClick={() => {
                            sigPad!.clear()
                        }}>Recommencer</Button>
                    </Col>
                </Row>
            </Container>
        );
    }

    return (
        <Container className="vh-100">
            <PageHeader title="Mes contrats"/>
            <Row>
                <Col className="bg-light p-0" style={{height: 400}}>
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
                                            <Button disabled={contract.studentSignature.length > 0}
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