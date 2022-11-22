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

    return (
        <Container className="min-vh-100">
            {signing &&
                <div>
                    <Container
                        className="position-absolute min-vh-100 p-0 m-0 min-vw-100 bg-dark p-0 top-0 start-0 end-0 opacity-50">
                    </Container>
                    <Container
                        className="position-absolute min-vh-100 min-vw-100 p-0 top-0 start-0 end-0">
                        <Row className="min-vh-100 m-0">
                            <Col sm={4} className="rounded m-auto bg-white">
                                <Row className="bg-dark rounded-top p-2">
                                    <Col className="p-0">
                                        <Button variant="danger" onClick={() => {
                                            setSigning(false)
                                        }}>Fermer</Button>
                                    </Col>
                                    <Col className="p-0 text-end">
                                        <Button variant="success" onClick={() => {
                                            if (sigPad!.isEmpty()) {
                                                alert("Vous devez signer!")
                                            } else {
                                                signContract(sigPad!.toDataURL())
                                            }
                                        }}>Signer</Button>
                                    </Col>
                                </Row>
                                <Col className="px-2 pt-2 text-center">
                                    <SignaturePad
                                        canvasProps={{height: 300, className: 'border col-12 border-5 bg-light'}}
                                        ref={(ref) => {
                                            sigPad = ref
                                        }}/>
                                    <Row className="p-2">
                                        <Button onClick={() => {
                                            sigPad!.clear()
                                        }}>Recommencer</Button>
                                    </Row>
                                </Col>
                            </Col>
                        </Row>
                    </Container>
                </div>
            }
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