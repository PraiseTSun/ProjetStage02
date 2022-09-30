import React, {useEffect, useState} from "react";
import {useParams} from "react-router-dom";

const PdfPage = ({ouvrirPDF} : {ouvrirPDF:Function}) => {
   // const {id} = useParams()
    useEffect(()=>{
        // ouvrirPDF(id)
    })
    //source : https://stackoverflow.com/questions/54814373/how-to-use-react-pdf-library-with-typescript
    // https://stackoverflow.com/questions/65338762/react-pdf-from-bytearray-response
    // <Document file="test.pdf" onLoadSuccess={onDocumentLoadSuccess}><Page pageNumber={pageNumber} /> </Document>
    const {Document, Page, pdfjs} = require('react-pdf');
    pdfjs.GlobalWorkerOptions.workerSrc = `//cdnjs.cloudflare.com/ajax/libs/pdf.js/${pdfjs.version}/pdf.worker.js`;

    const [numPages, setNumPages] = useState(null);
    const [pageNumber, setPageNumber] = useState(1);

    const onDocumentLoadSuccess = ({ numPages } : { numPages: any }) => {
        setNumPages(numPages);
    }

    return (
        <>
            <Document
                file="/ex.pdf"
                onLoadSuccess={onDocumentLoadSuccess}
            >
                <Page pageNumber={pageNumber} width={600} />
            </Document>
            <p>
                Page {pageNumber} of {numPages}
            </p>
        </>

    );
}
export default PdfPage