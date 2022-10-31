import IUser from "../../models/IUser";
import {Button, Col, Container, Row, Table} from "react-bootstrap";
import React, {useCallback, useEffect, useState} from "react";
import PageHeader from "../../components/universalComponents/PageHeader";
import IContract from "../../models/IContract";
import {putStudentContracts} from "../../services/studentServices/StudentFetchService";
import {generateAlert} from "../../services/universalServices/UniversalUtilService";
import SignaturePad from "react-signature-canvas"

const StudentContractsPage = ({connectedUser}: { connectedUser: IUser }): JSX.Element => {
    const nextYear = new Date().getFullYear() + 1
    const [signing, setSigning] = useState<boolean>(false);
    const [contrats, setContrats] = useState<IContract[]>([]);
    let sigPad: SignaturePad | null

    const fetchContracts = useCallback(async () => {
        try {
            const response: Response = await putStudentContracts(connectedUser.id, `Hiver ${nextYear}`, connectedUser.token);

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
                            sigPad?.getCanvas().toBlob((blob:any) => {
                                blob?.arrayBuffer().then((data:any) => {
                                    console.log(new Uint8Array(data));
                                })
                            })
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
                            {/* TODO NEED MORE STUFF */}
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
                            : contrats.map((contrat, index) => {
                                return (
                                    <tr key={index}>
                                        <td>{contrat.description}</td>
                                        <td>
                                            {contrat.studentSignature.length > 2 ?
                                                <p className="text-success">Vous avez signé</p> :
                                                <p className="text-danger">Vous n'avez pas signé!</p>}
                                            <Button disabled={contrat.studentSignature.length > 2}
                                                    onClick={() => {
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