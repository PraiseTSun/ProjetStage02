import {Button, Col, Container, Row} from "react-bootstrap";
import SignaturePad from "react-signature-canvas";
import React from "react";

const SignaturePopup = ({setSigning, onSignature}: { setSigning: Function, onSignature: Function }): JSX.Element => {
    const [signed, setSigned] = React.useState<boolean>(true);
    let sigPad: SignaturePad | null;

    return (
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
                                        setSigned(false)
                                    } else {
                                        onSignature(sigPad!.toDataURL())
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
                            {!signed && <p className="text-danger fw-bold h5">Vous devez signer!</p>}
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
    );
}

export default SignaturePopup;