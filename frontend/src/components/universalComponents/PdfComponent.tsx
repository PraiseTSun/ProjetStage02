import {Button, Container} from "react-bootstrap";
import {Viewer} from "@react-pdf-viewer/core";
import React from "react";

const PdfComponent = ({pdf, setShowPdf}: { pdf: Uint8Array, setShowPdf: Function }): JSX.Element => {
    return (
        <Container className="position-absolute bg-white p-0 top-0 start-0 end-0 mw-100">
            <div className="bg-dark p-2">
                <Button variant="danger" onClick={() => setShowPdf(false)}>
                    Fermer
                </Button>
            </div>
            <div>
                <Viewer fileUrl={pdf}/>
            </div>
        </Container>
    );
}

export default PdfComponent;